package com.ups.notification_service.consumer;

import com.ups.connection_service.event.AcceptConnectionRequestEvent;
import com.ups.connection_service.event.SendConnectionRequestEvent;
import com.ups.notification_service.service.SendNotification;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class ConnectionServiceConsumer {

    @Autowired
    private SendNotification sendNotification;

    @KafkaListener(topics = "send-connection-request-topic")
    public void handleSendConnectionRequest(SendConnectionRequestEvent sendConnectionRequestEvent){

        String message = "you have received a connection request from user: %d" + sendConnectionRequestEvent.getSenderId();
        sendNotification.send(sendConnectionRequestEvent.getReceiverId() , message);
    }




    @KafkaListener(topics = "accept-connection-request-topic")
    public void handleAcceptConnectionRequest(AcceptConnectionRequestEvent acceptConnectionRequestEvent){

        String message = "your connection request has been accepted by user: %d " + acceptConnectionRequestEvent.getReceiverId();
        sendNotification.send(acceptConnectionRequestEvent.getSenderId() , message);
    }
}
