package com.ups.connection_service.service;

import com.ups.connection_service.auth.UserContextHolder;
import com.ups.connection_service.entity.Person;
import com.ups.connection_service.event.AcceptConnectionRequestEvent;
import com.ups.connection_service.event.SendConnectionRequestEvent;
import com.ups.connection_service.repository.PersonRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class ConnectionService {

    @Autowired
    private PersonRepository personRepository;

    @Autowired
    private KafkaTemplate<Long , AcceptConnectionRequestEvent> acceptRequestKafkaTemplate;

    @Autowired
    private KafkaTemplate<Long , SendConnectionRequestEvent> sendRequestKafkaTemplate;

    public List<Person> getMyFirstDegreeConnections(){
        Long userId = UserContextHolder.getCurrentUserId();
        log.info("getting the first degree connections of the user:{}" , userId);
        List<Person> persons =  personRepository.getFirstDegreeConnections(userId);
        return persons;
    }

    public Person getNode(Long id) {
        return personRepository.findById(id).orElse(null);
    }




    public Boolean sendConnectionRequest(Long receiverId) {
        Long senderId = UserContextHolder.getCurrentUserId();
        log.info("trying to send the connection request senderId: {} , receiverId: {} ",senderId , receiverId );

        if(senderId.equals(receiverId))
            throw  new RuntimeException("sender and receiver are the same person");

        boolean alreadySentRequest = personRepository.connectionRequestExist(senderId  , receiverId);
        if(alreadySentRequest)
            throw new RuntimeException("request already sent");

        boolean alreadyConnected = personRepository.alreadyConnected(senderId , receiverId);
        if(alreadyConnected)
            throw new RuntimeException("already connected");

        log.info("successfully sent the connection request");
        personRepository.addConnectionRequest(senderId , receiverId);


        SendConnectionRequestEvent sendConnectionRequestEvent = SendConnectionRequestEvent.builder()
                .receiverId(receiverId)
                .senderId(senderId)
                .build();
        sendRequestKafkaTemplate.send("send-connection-request-topic" , sendConnectionRequestEvent);

        return true;

    }


    public Boolean acceptConnectionRequest(Long senderId) {
        Long receiverId = UserContextHolder.getCurrentUserId();
        log.info("trying to accept the connection request from(sender) : {}  , to(receiver) : {}" , senderId , receiverId);

        boolean connectionRequestExist = personRepository.connectionRequestExist(senderId , receiverId);
        if(!connectionRequestExist)
            throw  new RuntimeException("No connection request exists to accept");

        personRepository.acceptConnectionRequest(senderId  , receiverId);
        log.info("successfully accepted the connection request of sender:{} " , senderId);
        AcceptConnectionRequestEvent acceptConnectionRequestEvent = AcceptConnectionRequestEvent.builder()
                .receiverId(receiverId)
                .senderId(senderId)
                .build();
        acceptRequestKafkaTemplate.send("accept-connection-request-topic" , acceptConnectionRequestEvent);

        return true;
    }

    public Boolean rejectConnectionService(Long senderId) {
        Long receiverId = UserContextHolder.getCurrentUserId();
        log.info("trying to reject the connection request from(sender):{} , to(receiver):{} " , senderId , receiverId);

        boolean connectionRequestExist = personRepository.connectionRequestExist(senderId , receiverId);
        if(!connectionRequestExist)
            throw  new RuntimeException("No connection request exists to reject");

        personRepository.rejectConnectionRequest(senderId , receiverId);
        return true;

    }
}





















