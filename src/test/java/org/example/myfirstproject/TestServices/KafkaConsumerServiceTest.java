package org.example.myfirstproject.TestServices;



import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.myfirstproject.Models.Entities.Notification;
import org.example.myfirstproject.Models.Entities.User;
import org.example.myfirstproject.Models.Enums.NotificationStatusEnum;
import org.example.myfirstproject.Repositories.NotificationRepository;
import org.example.myfirstproject.Repositories.UserRepository;
import org.example.myfirstproject.Services.Impl.KafkaConsumerServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class KafkaConsumerServiceTest {

    @Mock
    private NotificationRepository notificationRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private ObjectMapper objectMapper;

    @InjectMocks
    private KafkaConsumerServiceImpl kafkaConsumerService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void consumeNotification_SuccessfulSave() throws Exception {
        // Arrange
        String message = "{\"userId\": 1, \"message\": \"Test notification\"}";
        User user = new User();
        user.setId(1L);
        user.setUsername("testuser");

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        // Act
        kafkaConsumerService.consumeNotification(message);

        // Assert
        verify(notificationRepository, times(1)).save(any(Notification.class));
    }

    @Test
    void consumeNotification_UserNotFound_ShouldThrowException() {
        // Arrange
        String message = "{\"userId\": 99, \"message\": \"Test notification\"}";

        when(userRepository.findById(99L)).thenReturn(Optional.empty());

        // Act & Assert
        Exception exception = assertThrows(RuntimeException.class, () -> kafkaConsumerService.consumeNotification(message));

        // Проверка дали основното съобщение съдържа очаквания текст
        String expectedMessage = "User not found";
        String actualMessage = exception.getCause() != null ? exception.getCause().getMessage() : exception.getMessage();

        // Логиране за проверка
        System.out.println("Actual exception message: " + actualMessage);

        assertTrue(actualMessage != null && actualMessage.contains(expectedMessage),
                "Expected message to contain: " + expectedMessage + ", but was: " + actualMessage);
    }



    @Test
    void consumeNotification_InvalidMessageFormat_ShouldThrowException() {
        // Arrange
        String invalidMessage = "invalid_json_format";

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> kafkaConsumerService.consumeNotification(invalidMessage));
        assertTrue(exception.getMessage().contains("Error processing Kafka message"));
    }

    @Test
    void consumeNotification_SaveNotification() {
        // Arrange
        String message = "{\"userId\": 1, \"message\": \"Sample notification\"}";
        User user = new User();
        user.setId(1L);
        user.setUsername("testuser");

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        // Act
        kafkaConsumerService.consumeNotification(message);

        // Assert
        verify(notificationRepository, times(1)).save(any(Notification.class));
    }
}

