package org.example.myfirstproject.Services.Impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.myfirstproject.Services.KafkaProducerService;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class KafkaProducerServiceImpl implements KafkaProducerService {

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper;
    public KafkaProducerServiceImpl(KafkaTemplate<String, String> kafkaTemplate, ObjectMapper objectMapper) {
        this.kafkaTemplate = kafkaTemplate;
        this.objectMapper = objectMapper;
    }

    @Override
    public void sendNotification(Long userId, String message) {
        Map<String, Object> notificationData = new HashMap<>();
        notificationData.put("userId", userId);
        notificationData.put("message", message);


        try {
            String jsonMessage = objectMapper.writeValueAsString(notificationData);
            kafkaTemplate.send("notifications", jsonMessage); // 🔹 Изпращаме JSON
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error serializing Kafka message", e);
        }
    }
}
