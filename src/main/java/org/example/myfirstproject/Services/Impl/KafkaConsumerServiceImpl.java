package org.example.myfirstproject.Services.Impl;


import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.example.myfirstproject.Models.Entities.Notification;
import org.example.myfirstproject.Models.Entities.User;
import org.example.myfirstproject.Models.Enums.NotificationStatusEnum;
import org.example.myfirstproject.Repositories.NotificationRepository;
import org.example.myfirstproject.Repositories.UserRepository;
import org.example.myfirstproject.Services.KafkaConsumerService;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class KafkaConsumerServiceImpl implements KafkaConsumerService {

    private final NotificationRepository notificationRepository;
    private final UserRepository userRepository;
    private final ObjectMapper objectMapper;

    public KafkaConsumerServiceImpl(NotificationRepository notificationRepository, UserRepository userRepository, ObjectMapper objectMapper) {
        this.notificationRepository = notificationRepository;
        this.userRepository = userRepository;
        this.objectMapper = objectMapper;
    }

    @KafkaListener(topics = "notifications", groupId = "notification-group")
    public void consumeNotification(String message) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonNode = objectMapper.readTree(message);

            Long userId = jsonNode.get("userId").asLong();
            String notificationMessage = jsonNode.get("message").asText();

            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new RuntimeException("User not found: " + userId));

            Notification notification = new Notification();
            notification.setMessage(notificationMessage);
            notification.setStatus(NotificationStatusEnum.SENT);
            notification.setUser(user);

            notificationRepository.save(notification);
        } catch (Exception e) {
            throw new RuntimeException("Error processing Kafka message: " + message, e);
        }
    }



}
