package org.example.myfirstproject.Controllers;

import org.example.myfirstproject.Models.Entities.Notification;
import org.example.myfirstproject.Services.NotificationService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Collections;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/notifications")
public class NotificationController {

    private final NotificationService notificationService;

    public NotificationController(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @GetMapping("/unread-count")
    public ResponseEntity<Map<String, Integer>> getUnreadNotificationCount(Authentication authentication) {
        String username = authentication.getName();
        int count = notificationService.getUnreadNotificationsCount(username);
        return ResponseEntity.ok(Collections.singletonMap("count", count));
    }

    @GetMapping
    public String showNotifications(Model model, Authentication authentication) {
        String username = authentication.getName();
        List<Notification> notifications = notificationService.getUserNotifications(username);
        model.addAttribute("notifications", notifications);
        return "unread-count"; // Зарежда Thymeleaf шаблона unread-count.html
    }

    @PostMapping("/mark-as-read")
    public String markAllAsRead(Authentication authentication) {
        String username = authentication.getName();
        notificationService.markAllAsRead(username);
        return "redirect:/notifications"; // Презарежда страницата
    }
}
