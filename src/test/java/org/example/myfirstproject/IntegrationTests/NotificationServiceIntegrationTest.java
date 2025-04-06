package org.example.myfirstproject.IntegrationTests;

import org.example.myfirstproject.Models.Entities.Notification;
import org.example.myfirstproject.Models.Entities.User;
import org.example.myfirstproject.Models.Enums.NotificationStatusEnum;
import org.example.myfirstproject.Models.Enums.RoleEnum;
import org.example.myfirstproject.Repositories.NotificationRepository;
import org.example.myfirstproject.Repositories.UserRepository;
import org.example.myfirstproject.Services.KafkaProducerService;
import org.example.myfirstproject.Services.NotificationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@EmbeddedKafka(partitions = 1, topics = {"notifications"})
public class NotificationServiceIntegrationTest {

    @Autowired
    private KafkaProducerService kafkaProducerService;

    @Autowired
    private NotificationService notificationService;

    @Autowired
    private NotificationRepository notificationRepository;

    @Autowired
    private UserRepository userRepository;

    private User testUser;

    @BeforeEach
    void setUp() {
        // Създаване на тестов потребител
        testUser = new User();
        testUser.setUsername("testuser");
        testUser.setFirstName("Test");
        testUser.setLastName("User");
        testUser.setEmail("testuser@example.com");
        testUser.setPassword("password");
        testUser.setPhoneNumber("0888123456");
        testUser.setRole(RoleEnum.USER);
        userRepository.save(testUser);
    }




    @Test
    void markAllAsRead_ShouldUpdateStatusToRead() {
        // Arrange - добавяме тестова нотификация директно
        Notification notification = new Notification();
        notification.setUser(testUser);
        notification.setMessage("Unread Notification");
        notification.setStatus(NotificationStatusEnum.SENT);
        notificationRepository.save(notification);

        // Act - маркираме всички като прочетени
        notificationService.markAllAsRead(testUser.getUsername());

        // Assert - проверка на статуса
        List<Notification> notifications = notificationRepository.findByUserId(testUser.getId());
        assertFalse(notifications.isEmpty());
        notifications.forEach(n -> assertEquals(NotificationStatusEnum.READ, n.getStatus()));
    }

    @Test
    void deleteNotification_ShouldRemoveFromDatabase() {
        // Arrange - добавяме тестова нотификация директно
        Notification notification = new Notification();
        notification.setUser(testUser);
        notification.setMessage("Notification to be deleted");
        notification.setStatus(NotificationStatusEnum.SENT);
        notificationRepository.save(notification);

        // Act - изтриваме нотификацията
        notificationService.deleteNotification(notification.getId());

        // Assert - проверка дали нотификацията е изтрита
        List<Notification> notifications = notificationRepository.findByUserId(testUser.getId());
        assertTrue(notifications.isEmpty());
    }

    @Test
    void getUserNotifications_ShouldReturnAllNotifications() {
        // Arrange - добавяне на две нотификации
        Notification notification1 = new Notification();
        notification1.setUser(testUser);
        notification1.setMessage("First Notification");
        notification1.setStatus(NotificationStatusEnum.SENT);
        notificationRepository.save(notification1);

        Notification notification2 = new Notification();
        notification2.setUser(testUser);
        notification2.setMessage("Second Notification");
        notification2.setStatus(NotificationStatusEnum.SENT);
        notificationRepository.save(notification2);

        // Act - извличане на всички нотификации
        List<Notification> notifications = notificationService.getUserNotifications(testUser.getUsername());

        // Assert - проверка на броя и съдържанието на нотификациите
        assertEquals(2, notifications.size());
        assertEquals("First Notification", notifications.get(0).getMessage());
        assertEquals("Second Notification", notifications.get(1).getMessage());
    }
}
