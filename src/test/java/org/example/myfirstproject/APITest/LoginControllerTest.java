package org.example.myfirstproject.APITest;

import org.example.myfirstproject.Models.DTO.UserLoginDTO;
import org.example.myfirstproject.Models.Entities.User;
import org.example.myfirstproject.Models.Enums.RoleEnum;
import org.example.myfirstproject.Repositories.NotificationRepository;
import org.example.myfirstproject.Repositories.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class LoginControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private NotificationRepository notificationRepository;

    @BeforeEach
    void setUp() {
        notificationRepository.deleteAll();
        userRepository.deleteAll();
        // Добавяме потребител за тестове
        User admin = new User();
        admin.setUsername("admin");
        admin.setPassword(passwordEncoder.encode("adminpass"));
        admin.setEmail("admin@example.com");
        admin.setPhoneNumber("0888123456");
        admin.setFirstName("Admin");
        admin.setLastName("User");
        admin.setRole(RoleEnum.ADMIN);
        userRepository.save(admin);

        User user = new User();
        user.setUsername("user");
        user.setPassword(passwordEncoder.encode("userpass"));
        user.setEmail("user@example.com");
        user.setPhoneNumber("0888000111");
        user.setFirstName("Regular");
        user.setLastName("User");
        user.setRole(RoleEnum.USER);
        userRepository.save(user);
    }


    @Test
    void showLoginForm_ShouldReturnLoginPage() throws Exception {
        mockMvc.perform(get("/login"))
                .andExpect(status().isOk())
                .andExpect(view().name("login"))
                .andExpect(model().attributeExists("userLoginDTO"));
    }

    @Test
    void loginUser_Admin_ShouldRedirectToAdminHome() throws Exception {
        mockMvc.perform(post("/login")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("username", "admin")
                        .param("password", "adminpass"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/adminHome"));
    }

    @Test
    void loginUser_User_ShouldRedirectToUserHome() throws Exception {
        mockMvc.perform(post("/login")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("username", "user")
                        .param("password", "userpass"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/userHome"));
    }

    @Test
    void loginWithInvalidUsername_ShouldRedirectToLoginPage() throws Exception {
        mockMvc.perform(post("/login")
                        .param("username", "wronguser")
                        .param("password", "wrongpass"))
                .andExpect(status().isFound())  // Очакваме статус 302 (пренасочване)
                .andExpect(redirectedUrl("/login?error"));  // Проверяваме дали пренасочва към логин с грешка
    }

    @Test
    void loginWithInvalidPassword_ShouldRedirectToLoginPage() throws Exception {
        mockMvc.perform(post("/login")
                        .param("username", "admin")  // Съществуващ потребител
                        .param("password", "wrongpass"))  // Грешна парола
                .andExpect(status().isFound())  // Очакваме статус 302 (пренасочване)
                .andExpect(redirectedUrl("/login?error"));  // Проверяваме дали пренасочва към логин с грешка
    }

}
