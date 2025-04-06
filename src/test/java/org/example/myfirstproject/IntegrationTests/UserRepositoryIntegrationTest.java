package org.example.myfirstproject.IntegrationTests;



import jakarta.transaction.Transactional;
import org.example.myfirstproject.Models.Entities.User;
import org.example.myfirstproject.Models.Enums.RoleEnum;
import org.example.myfirstproject.Repositories.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
public class UserRepositoryIntegrationTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    void testSaveAndFindUser() {
        // Създаване на нов потребител
        User user = new User();
        user.setUsername("testuser");
        user.setFirstName("John");
        user.setLastName("Doe");
        user.setEmail("john.doe@example.com");
        user.setPhoneNumber("123456789");
        user.setRole(RoleEnum.USER);
        user.setPassword("password");

        // Запазване в базата
        User savedUser = userRepository.save(user);

        // Проверка дали е запазен успешно
        assertNotNull(savedUser.getId());

        // Извличане на потребителя по username
        Optional<User> foundUser = userRepository.findByUsername("testuser");
        assertTrue(foundUser.isPresent());
        assertEquals("John", foundUser.get().getFirstName());
    }

    @Test
    void testDeleteUser() {
        // Създаване и запазване на потребител
        User user = new User();
        user.setUsername("deleteuser");
        user.setFirstName("Jane");
        user.setLastName("Smith");
        user.setEmail("jane.smith@example.com");
        user.setPhoneNumber("987654321");
        user.setRole(RoleEnum.USER);
        user.setPassword("password");

        User savedUser = userRepository.save(user);
        Long userId = savedUser.getId();

        // Изтриване на потребителя
        userRepository.deleteById(userId);

        // Проверка дали потребителят е изтрит
        Optional<User> foundUser = userRepository.findById(userId);
        assertFalse(foundUser.isPresent());
    }
}

