package org.example.myfirstproject.Services;

public interface KafkaProducerService {
    void sendNotification(Long userId, String notificationMessage);
}
