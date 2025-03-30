package org.example.myfirstproject.Services.Impl;

import jakarta.transaction.Transactional;
import org.example.myfirstproject.Models.DTO.SettingsDTO;
import org.example.myfirstproject.Models.DTO.UserRegisterDTO;
import org.example.myfirstproject.Models.Entities.User;
import org.example.myfirstproject.Models.Enums.RoleEnum;
import org.example.myfirstproject.Repositories.UserRepository;
import org.example.myfirstproject.Services.UserService;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepository, @Lazy PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    // Регистрация на потребител
    public boolean registerUser(UserRegisterDTO userRegisterDTO) {
        if (userRepository.findByUsername(userRegisterDTO.getUsername()).isPresent()) {
            return false; // Потребителят вече съществува
        }

        User newUser = new User();
        newUser.setFirstName(userRegisterDTO.getFirstName());
        newUser.setLastName(userRegisterDTO.getLastName());
        newUser.setEmail(userRegisterDTO.getEmail());
        newUser.setPhoneNumber(userRegisterDTO.getPhoneNumber());
        newUser.setUsername(userRegisterDTO.getUsername());

        // Криптиране на паролата
        newUser.setPassword(passwordEncoder.encode(userRegisterDTO.getPassword()));

        // По подразбиране дава роля USER
        newUser.setRole(RoleEnum.USER);

        userRepository.save(newUser);
        return true;
    }

    // Търсене на потребител по username
    public User findByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));
    }


    @Override
    public boolean isPasswordCorrect(User user, String password) {
        return false;
    }

    // Проверка дали паролата е вярна
    public boolean isPasswordCorrect(String username, String rawPassword) {
        User user = findByUsername(username);
        return passwordEncoder.matches(rawPassword, user.getPassword());
    }

    // Зареждане на потребител за Spring Security
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = findByUsername(username);

        return new org.springframework.security.core.userdetails.User(
                user.getUsername(),
                user.getPassword(),
                Collections.singletonList(new SimpleGrantedAuthority(user.getRole().name()))
        );
    }
    @Transactional
    @Override
    public void updateAdminSettings(SettingsDTO settingsDTO) {
        User admin = findByUsername(settingsDTO.getUsername());

        admin.setFirstName(settingsDTO.getFirstName());
        admin.setLastName(settingsDTO.getLastName());
        admin.setEmail(settingsDTO.getEmail());
        admin.setPhoneNumber(settingsDTO.getPhoneNumber());

        // Ако паролата не е празна, обновяваме я
        if (settingsDTO.getPassword() != null && !settingsDTO.getPassword().isEmpty()) {
            admin.setPassword(passwordEncoder.encode(settingsDTO.getPassword()));
        }

        userRepository.save(admin);
    }

    @Override
    public void updateUserSettings(SettingsDTO settingsDTO) {
        User user = findByUsername(settingsDTO.getUsername());

        user.setFirstName(settingsDTO.getFirstName());
        user.setLastName(settingsDTO.getLastName());
        user.setEmail(settingsDTO.getEmail());
        user.setPhoneNumber(settingsDTO.getPhoneNumber());

        // Ако паролата не е празна, обновяваме я
        if (settingsDTO.getPassword() != null && !settingsDTO.getPassword().isEmpty()) {
            user.setPassword(passwordEncoder.encode(settingsDTO.getPassword()));
        }

        userRepository.save(user);
    }
    @Override
    public User getUserByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found: " + username));
    }
}
