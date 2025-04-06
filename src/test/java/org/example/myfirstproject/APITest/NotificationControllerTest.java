package org.example.myfirstproject.APITest;

import org.example.myfirstproject.Models.Entities.Notification;
import org.example.myfirstproject.Models.Entities.User;
import org.example.myfirstproject.Models.Enums.NotificationStatusEnum;
import org.example.myfirstproject.Models.Enums.RoleEnum;
import org.example.myfirstproject.Repositories.UserRepository;
import org.example.myfirstproject.Services.NotificationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.bean.override.mockito.MockitoSpyBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class NotificationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoSpyBean
    private NotificationService notificationService;

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        // Проверка дали потребителят вече съществува, за да избегнем дублиране
        if (userRepository.findByUsername("testuser").isEmpty()) {
            User testUser = new User();
            testUser.setUsername("testuser");
            testUser.setFirstName("Test");
            testUser.setLastName("User");
            testUser.setEmail("testuser@example.com");
            testUser.setPassword("password");
            testUser.setPhoneNumber("0888123456");
            testUser.setRole(RoleEnum.USER);
            userRepository.save(testUser);
        }
    }


    @Test
    @WithMockUser(username = "testuser", roles = {"USER"})
    void getUnreadNotificationCount_ShouldReturnCount() throws Exception {
        // Arrange
        when(notificationService.getUnreadNotificationsCount("testuser")).thenReturn(5);

        // Act & Assert
        mockMvc.perform(get("/notifications/unread-count"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.count").value(5));

        verify(notificationService, times(1)).getUnreadNotificationsCount("testuser");
    }

    @Test
    @WithMockUser(username = "testuser", roles = {"USER"})
    void showNotifications_ShouldReturnNotificationsPage() throws Exception {
        // Arrange
        Notification notification = new Notification();
        notification.setMessage("New message");
        notification.setStatus(NotificationStatusEnum.SENT);

        when(notificationService.getUserNotifications("testuser")).thenReturn(List.of(notification));

        // Act & Assert
        mockMvc.perform(get("/notifications"))
                .andExpect(status().isOk())
                .andExpect(view().name("unread-count"))
                .andExpect(model().attributeExists("notifications"));

        verify(notificationService, times(1)).getUserNotifications("testuser");
    }

    @Test
    @WithMockUser(username = "testuser", roles = {"USER"})
    void markAllAsRead_ShouldRedirectToNotifications() throws Exception {
        // Act & Assert
        mockMvc.perform(post("/notifications/mark-as-read")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/notifications"));

        verify(notificationService, times(1)).markAllAsRead("testuser");
    }

    @Test
    @WithMockUser(username = "testuser", roles = {"USER"})
    void deleteNotification_ShouldRedirectToNotifications() throws Exception {
        // Act & Assert
        mockMvc.perform(post("/notifications/delete/1")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/notifications"));

        verify(notificationService, times(1)).deleteNotification(1L);
    }
}
