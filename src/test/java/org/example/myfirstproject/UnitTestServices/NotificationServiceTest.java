package org.example.myfirstproject.UnitTestServices;

import org.example.myfirstproject.Models.Entities.Notification;
import org.example.myfirstproject.Models.Entities.User;
import org.example.myfirstproject.Models.Enums.NotificationStatusEnum;
import org.example.myfirstproject.Repositories.NotificationRepository;
import org.example.myfirstproject.Repositories.UserRepository;
import org.example.myfirstproject.Services.Impl.NotificationServiceImpl;
import org.example.myfirstproject.Services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class NotificationServiceTest {

    @Mock
    private NotificationRepository notificationRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserService userService;

    @InjectMocks
    private NotificationServiceImpl notificationService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getUnreadNotificationsCount_ShouldReturnCorrectCount() {
        // Arrange
        User user = new User();
        user.setId(1L);
        user.setUsername("john");

        when(userRepository.findByUsername("john")).thenReturn(Optional.of(user));
        when(notificationRepository.countByUserIdAndStatus(1L, NotificationStatusEnum.SENT)).thenReturn(5);

        // Act
        int unreadCount = notificationService.getUnreadNotificationsCount("john");

        // Assert
        assertEquals(5, unreadCount);
        verify(notificationRepository, times(1)).countByUserIdAndStatus(1L, NotificationStatusEnum.SENT);
    }

    @Test
    void getUserNotifications_ShouldReturnListOfNotifications() {
        // Arrange
        User user = new User();
        user.setId(1L);
        user.setUsername("john");

        List<Notification> notifications = new ArrayList<>();
        notifications.add(new Notification());
        notifications.add(new Notification());

        when(userRepository.findByUsername("john")).thenReturn(Optional.of(user));
        when(notificationRepository.findByUserId(1L)).thenReturn(notifications);

        // Act
        List<Notification> result = notificationService.getUserNotifications("john");

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        verify(notificationRepository, times(1)).findByUserId(1L);
    }

    @Test
    void markAllAsRead_ShouldUpdateNotificationStatus() {
        // Arrange
        User user = new User();
        user.setId(1L);
        user.setUsername("john");

        List<Notification> notifications = new ArrayList<>();
        Notification notification1 = new Notification();
        notification1.setStatus(NotificationStatusEnum.SENT);
        Notification notification2 = new Notification();
        notification2.setStatus(NotificationStatusEnum.SENT);
        notifications.add(notification1);
        notifications.add(notification2);

        when(userService.getUserByUsername("john")).thenReturn(user);
        when(notificationRepository.findByUserIdAndStatus(1L, NotificationStatusEnum.SENT)).thenReturn(notifications);

        // Act
        notificationService.markAllAsRead("john");

        // Assert
        assertEquals(NotificationStatusEnum.READ, notifications.get(0).getStatus());
        assertEquals(NotificationStatusEnum.READ, notifications.get(1).getStatus());
        verify(notificationRepository, times(1)).saveAll(notifications);
    }

    @Test
    void deleteNotification_ShouldCallRepositoryDeleteById() {
        // Act
        notificationService.deleteNotification(1L);

        // Assert
        verify(notificationRepository, times(1)).deleteById(1L);
    }

    @Test
    void getUnreadNotificationsCount_UserNotFound_ShouldThrowException() {
        // Arrange
        when(userRepository.findByUsername("unknown")).thenReturn(Optional.empty());

        // Act & Assert
        Exception exception = assertThrows(IllegalArgumentException.class, () -> notificationService.getUnreadNotificationsCount("unknown"));
        assertTrue(exception.getMessage().contains("User not found"));
    }

    @Test
    void getUserNotifications_UserNotFound_ShouldThrowException() {
        // Arrange
        when(userRepository.findByUsername("unknown")).thenReturn(Optional.empty());

        // Act & Assert
        Exception exception = assertThrows(IllegalArgumentException.class, () -> notificationService.getUserNotifications("unknown"));
        assertTrue(exception.getMessage().contains("User not found"));
    }
}
