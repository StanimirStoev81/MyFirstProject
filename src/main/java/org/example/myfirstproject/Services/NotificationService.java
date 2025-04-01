package org.example.myfirstproject.Services;

import org.example.myfirstproject.Models.Entities.Notification;

import java.util.List;

public interface NotificationService {
    int getUnreadNotificationsCount(String username);

    List<Notification> getUserNotifications(String username);

    void markAllAsRead(String username);

    void deleteNotification(Long id);
}
