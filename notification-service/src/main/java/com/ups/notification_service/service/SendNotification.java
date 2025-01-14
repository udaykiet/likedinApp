package com.ups.notification_service.service;

import com.ups.notification_service.entity.Notification;
import com.ups.notification_service.repository.NotificationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SendNotification {


    @Autowired
    private NotificationRepository notificationRepository;


    public void send(Long userId , String message){

        Notification notification = new Notification();
        notification.setMessage(message);
        notification.setUserId(userId);

        notificationRepository.save(notification);

    }
}
