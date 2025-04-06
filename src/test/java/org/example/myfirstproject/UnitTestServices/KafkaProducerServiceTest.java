package org.example.myfirstproject.UnitTestServices;




import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.myfirstproject.Services.Impl.KafkaProducerServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.kafka.core.KafkaTemplate;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class KafkaProducerServiceTest {

    @Mock
    private KafkaTemplate<String, String> kafkaTemplate;

    @Mock
    private ObjectMapper objectMapper;

    @InjectMocks
    private KafkaProducerServiceImpl kafkaProducerService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        kafkaProducerService = new KafkaProducerServiceImpl(kafkaTemplate, objectMapper);  // Инжектираме ObjectMapper
    }

    @Test
    void sendNotification_ShouldSendMessageToKafka() {
        // Arrange
        Long userId = 1L;
        String message = "Test notification";
        Map<String, Object> notificationData = new HashMap<>();
        notificationData.put("userId", userId);
        notificationData.put("message", message);

        String jsonMessage = "{\"userId\":1,\"message\":\"Test notification\"}";
        try {
            when(objectMapper.writeValueAsString(notificationData)).thenReturn(jsonMessage);
        } catch (JsonProcessingException e) {
            fail("Mocking failed");
        }

        // Act
        kafkaProducerService.sendNotification(userId, message);

        // Assert
        verify(kafkaTemplate, times(1)).send("notifications", jsonMessage);
    }

    @Test
    void sendNotification_WhenJsonProcessingException_ShouldThrowRuntimeException() {
        // Arrange
        Long userId = 1L;
        String message = "Invalid Message";

        try {
            when(objectMapper.writeValueAsString(any())).thenThrow(new JsonProcessingException("Test Exception") {});
        } catch (JsonProcessingException e) {
            fail("Mocking failed");
        }

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> kafkaProducerService.sendNotification(userId, message));
        assertTrue(exception.getMessage().contains("Error serializing Kafka message"));
    }
}
