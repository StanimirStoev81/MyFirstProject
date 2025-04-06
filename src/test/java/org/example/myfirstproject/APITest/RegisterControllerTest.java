package org.example.myfirstproject.APITest;

import org.example.myfirstproject.Models.DTO.UserRegisterDTO;
import org.example.myfirstproject.Services.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoSpyBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class RegisterControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoSpyBean
    private UserService userService;

    @Test
    void showRegistrationForm_ShouldReturnRegisterPage() throws Exception {
        mockMvc.perform(get("/register"))
                .andExpect(status().isOk())
                .andExpect(view().name("register"))
                .andExpect(model().attributeExists("userRegisterDTO"));
    }

    @Test
    void registerUser_SuccessfulRegistration_ShouldRedirectToLogin() throws Exception {
        // Arrange
        UserRegisterDTO userRegisterDTO = new UserRegisterDTO("John", "Doe", "john@example.com", "0888123456", "john_doe", "password", "password");
        doReturn(true).when(userService).registerUser(any(UserRegisterDTO.class));

        // Act & Assert
        mockMvc.perform(post("/register")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("firstName", "John")
                        .param("lastName", "Doe")
                        .param("email", "john@example.com")
                        .param("phoneNumber", "0888123456")
                        .param("username", "john_doe")
                        .param("password", "password")
                        .param("confirmPassword", "password"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/login"));

        verify(userService, times(1)).registerUser(any(UserRegisterDTO.class));
    }

    @Test
    void registerUser_InvalidData_ShouldReturnRegisterPage() throws Exception {
        // Act & Assert
        mockMvc.perform(post("/register")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("firstName", "")  // Празно поле
                        .param("lastName", "Doe")
                        .param("email", "invalid-email")  // Невалиден email
                        .param("phoneNumber", "0888123456")
                        .param("username", "john_doe")
                        .param("password", "password")
                        .param("confirmPassword", "differentPassword"))  // Различни пароли
                .andExpect(status().isOk())
                .andExpect(view().name("register"))
                .andExpect(model().attributeHasFieldErrors("userRegisterDTO", "firstName", "email"))
                .andExpect(model().attributeHasErrors("userRegisterDTO"));  // Проверка за глобална грешка
    }



    @Test
    void registerUser_AlreadyExists_ShouldReturnError() throws Exception {
        // Arrange
        doReturn(false).when(userService).registerUser(any(UserRegisterDTO.class));

        // Act & Assert
        mockMvc.perform(post("/register")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("firstName", "John")
                        .param("lastName", "Doe")
                        .param("email", "john@example.com")
                        .param("phoneNumber", "0888123456")
                        .param("username", "john_doe")
                        .param("password", "password")
                        .param("confirmPassword", "password"))
                .andExpect(status().isOk())
                .andExpect(view().name("register"))
                .andExpect(model().attributeExists("registrationError"));

        verify(userService, times(1)).registerUser(any(UserRegisterDTO.class));
    }
}
