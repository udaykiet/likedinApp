package com.ups.connection_service.consumer;

import com.ups.connection_service.entity.Person;
import com.ups.connection_service.repository.PersonRepository;
import com.ups.user_service.event.UserCreatedEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class UserCreatedConsumer {

    @Autowired
    private PersonRepository personRepository;

    @KafkaListener(topics = "user-created-topic")
    public void handleUserCreatedEvent(UserCreatedEvent userCreatedEvent){
        log.info("from connection service,.... creating the new node in db");
        Person person = new Person();
        person.setUserId(userCreatedEvent.getUserId());
        person.setName(userCreatedEvent.getName());
        personRepository.save(person);

    }
}
