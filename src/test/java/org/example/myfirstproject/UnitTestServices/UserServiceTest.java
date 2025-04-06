package org.example.myfirstproject.UnitTestServices;

import org.example.myfirstproject.Models.DTO.SettingsDTO;
import org.example.myfirstproject.Models.DTO.UserRegisterDTO;
import org.example.myfirstproject.Models.Entities.User;
import org.example.myfirstproject.Repositories.NotificationRepository;
import org.example.myfirstproject.Repositories.UserRepository;
import org.example.myfirstproject.Services.Impl.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private NotificationRepository notificationRepository;

    @InjectMocks
    private UserServiceImpl userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void registerUser_ShouldReturnTrue_WhenUserDoesNotExist() {
        // Arrange
        UserRegisterDTO userDTO = new UserRegisterDTO();
        when(userRepository.findByUsername("john")).thenReturn(Optional.empty());
        when(passwordEncoder.encode("pass")).thenReturn("encodedPass");

        // Act
        boolean result = userService.registerUser(userDTO);

        // Assert
        assertTrue(result);
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void registerUser_ShouldReturnFalse_WhenUserAlreadyExists() {
        // Arrange
        UserRegisterDTO userDTO = new UserRegisterDTO("John", "Doe", "john@example.com", "123456789", "john", "pass", "pass");
        User existingUser = new User();
        existingUser.setUsername("john");

        when(userRepository.findByUsername("john")).thenReturn(Optional.of(existingUser));

        // Act
        boolean result = userService.registerUser(userDTO);

        // Assert
        assertFalse(result);
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void findByUsername_ShouldReturnUser_WhenUserExists() {
        // Arrange
        User user = new User();
        user.setUsername("john");
        when(userRepository.findByUsername("john")).thenReturn(Optional.of(user));

        // Act
        User result = userService.findByUsername("john");

        // Assert
        assertNotNull(result);
        assertEquals("john", result.getUsername());
    }

    @Test
    void findByUsername_ShouldThrowException_WhenUserDoesNotExist() {
        // Arrange
        when(userRepository.findByUsername("nonexistent")).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(RuntimeException.class, () -> userService.findByUsername("nonexistent"));
    }

    @Test
    void isPasswordCorrect_ShouldReturnTrue_WhenPasswordMatches() {
        // Arrange
        String username = "user1";
        String rawPassword = "password123";
        String encodedPassword = "encodedPassword123";

        User user = new User();
        user.setUsername(username);
        user.setPassword(encodedPassword);

        // Мокираме връщането на потребителя
        when(userRepository.findByUsername(username)).thenReturn(Optional.of(user));

        // Мокираме проверката на паролата
        when(passwordEncoder.matches(rawPassword, encodedPassword)).thenReturn(true);

        // Act
        boolean result = userService.isPasswordCorrect(username, rawPassword);

        // Assert
        assertTrue(result);
    }


    @Test
    void updateAdminSettings_ShouldUpdateAdminDetails() {
        // Arrange
        String username = "admin";
        SettingsDTO settingsDTO = new SettingsDTO();
        settingsDTO.setUsername(username);
        settingsDTO.setFirstName("AdminFirst");
        settingsDTO.setLastName("AdminLast");
        settingsDTO.setEmail("admin@example.com");
        settingsDTO.setPhoneNumber("123456789");
        settingsDTO.setPassword("newpassword");

        User admin = new User();
        admin.setUsername(username);
        admin.setFirstName("OldFirst");
        admin.setLastName("OldLast");
        admin.setEmail("oldadmin@example.com");
        admin.setPhoneNumber("987654321");

        // Мокираме връщането на администратора при заявка
        when(userRepository.findByUsername(username)).thenReturn(Optional.of(admin));

        // Act
        userService.updateAdminSettings(settingsDTO);

        // Assert
        assertEquals("AdminFirst", admin.getFirstName());
        assertEquals("AdminLast", admin.getLastName());
        assertEquals("admin@example.com", admin.getEmail());
        assertEquals("123456789", admin.getPhoneNumber());
        verify(userRepository, times(1)).save(admin);
    }

    @Test
    void deleteUserById_ShouldDeleteUserAndNotifications() {
        // Act
        userService.deleteUserById(1L);

        // Assert
        verify(notificationRepository, times(1)).deleteByUserId(1L);
        verify(userRepository, times(1)).deleteById(1L);
    }

    @Test
    void getAllUsers_ShouldReturnListOfUsers() {
        // Act
        userService.getAllUsers();

        // Assert
        verify(userRepository, times(1)).findAll();
    }
}
