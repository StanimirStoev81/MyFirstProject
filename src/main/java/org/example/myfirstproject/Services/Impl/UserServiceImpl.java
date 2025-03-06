package org.example.myfirstproject.Services.Impl;

import org.example.myfirstproject.Models.DTO.UserRegisterDTO;
import org.example.myfirstproject.Models.Entities.User;
import org.example.myfirstproject.Models.Enums.RoleEnum;
import org.example.myfirstproject.Repositories.UserRepository;
import org.example.myfirstproject.Services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;


    public boolean registerUser(UserRegisterDTO userRegisterDTO) {
        // Проверка дали вече съществува такъв потребител
        if (userRepository.findByUsername(userRegisterDTO.getUsername()).isPresent()) {
            return false; // Вече съществува
        }

        // Създаваме нов обект User
        User newUser = new User();
        newUser.setFirstName(userRegisterDTO.getFirstName());
        newUser.setLastName(userRegisterDTO.getLastName());
        newUser.setEmail(userRegisterDTO.getEmail());
        newUser.setPhoneNumber(userRegisterDTO.getPhoneNumber());
        newUser.setUsername(userRegisterDTO.getUsername());

        // Хешираме паролата (ако използваш BCrypt)
        String encodedPassword = passwordEncoder.encode(userRegisterDTO.getPassword());
        newUser.setPassword(encodedPassword);

        // По подразбиране даваме роля USER
        newUser.setRole(RoleEnum.USER);

        // Запазваме потребителя в базата
        userRepository.save(newUser);

        return true;
    }

}
