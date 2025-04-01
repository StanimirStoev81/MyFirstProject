package org.example.myfirstproject.Services.Impl;

import jakarta.transaction.Transactional;
import org.example.myfirstproject.Models.Entities.Notification;
import org.example.myfirstproject.Models.Entities.User;
import org.example.myfirstproject.Models.Enums.NotificationStatusEnum;
import org.example.myfirstproject.Repositories.NotificationRepository;
import org.example.myfirstproject.Repositories.UserRepository;
import org.example.myfirstproject.Services.NotificationService;
import org.example.myfirstproject.Services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NotificationServiceImpl implements NotificationService {


    private final NotificationRepository notificationRepository;
    private final UserRepository userRepository;
    private final UserService userService;

    public NotificationServiceImpl(NotificationRepository notificationRepository, UserRepository userRepository, UserService userService) {
        this.notificationRepository = notificationRepository;
        this.userRepository = userRepository;
        this.userService = userService;
    }
    @Override
    public int getUnreadNotificationsCount(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("User not found: " + username));

        return notificationRepository.countByUserIdAndStatus(user.getId(), NotificationStatusEnum.SENT); // 🔹 Брои само непрочетените
    }

    @Override
    public List<Notification> getUserNotifications(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("User not found: " + username));

        return notificationRepository.findByUserId(user.getId());
    }
    @Override
    @Transactional
    public void markAllAsRead(String username) {
        Long userId = userService.getUserByUsername(username).getId();
        List<Notification> unreadNotifications = notificationRepository.findByUserIdAndStatus(userId, NotificationStatusEnum.SENT);

        for (Notification notification : unreadNotifications) {
            notification.setStatus(NotificationStatusEnum.READ); // Променяме статуса на "read"
        }

        notificationRepository.saveAll(unreadNotifications); // Запазваме промените в базата
    }
    @Override
    @Transactional
    public void deleteNotification(Long id) {
        notificationRepository.deleteById(id);
    }
}
