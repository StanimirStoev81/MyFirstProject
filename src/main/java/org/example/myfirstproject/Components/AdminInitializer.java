package org.example.myfirstproject.Components;

import jakarta.transaction.Transactional;
import org.example.myfirstproject.Models.Entities.User;
import org.example.myfirstproject.Models.Enums.RoleEnum;
import org.example.myfirstproject.Repositories.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class AdminInitializer implements CommandLineRunner {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public AdminInitializer(UserRepository userRepository, @Lazy PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional
    public void run(String... args) {
        if (userRepository.findByUsername("Stambeto_81").isEmpty()) {
            User masterAdmin = new User();
            masterAdmin.setFirstName("Stanimir");
            masterAdmin.setLastName("Stoev");
            masterAdmin.setEmail("stambqsko@gmail.com");
            masterAdmin.setPhoneNumber("+359895495497");
            masterAdmin.setUsername("Stambeto_81");
            masterAdmin.setPassword(passwordEncoder.encode("Stambeto07"));
            masterAdmin.setRole(RoleEnum.ADMIN); // Използваме директно RoleEnum.ADMIN

            userRepository.save(masterAdmin);
        }
    }
}
