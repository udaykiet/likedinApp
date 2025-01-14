package com.ups.notification_service.consumer;

import com.ups.notification_service.clients.ConnectionsClient;
import com.ups.notification_service.dto.PersonDto;
import com.ups.notification_service.entity.Notification;
import com.ups.notification_service.repository.NotificationRepository;
import com.ups.notification_service.service.SendNotification;
import com.ups.post_service.event.PostCreatedEvent;
import com.ups.post_service.event.PostLikedEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class PostServiceConsumer {

    @Autowired
    private ConnectionsClient connectionsClient;


    @Autowired
    private SendNotification sendNotification;

    @KafkaListener(topics = "post-created-topic")
    public void handlePostCreated(PostCreatedEvent postCreatedEvent){
        log.info("sending notification for handlePostCreated");
        List<PersonDto> connections = connectionsClient.getAllFirstDegreeConnections(postCreatedEvent.getUserId());
        for(PersonDto connection : connections ){
            sendNotification.send(connection.getUserId() ,  " Your conection : " + postCreatedEvent.getUserId() + " " +
                        " created a new post .... ");}
    }


    @KafkaListener(topics = "post-liked-topic")
    public void handlePostLiked(PostLikedEvent postLikedEvent){
        log.info("sending notification for postLikedEvent");
        String message = "your post " + postLikedEvent.getPostId() + " is liked by " + postLikedEvent.getLikedByUserId();
        sendNotification.send(postLikedEvent.getCreatorId(), message);
    }


}
