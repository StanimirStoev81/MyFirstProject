package org.example.myfirstproject.Services;

import jakarta.transaction.Transactional;
import org.example.myfirstproject.Models.DTO.SettingsDTO;
import org.example.myfirstproject.Models.DTO.UserRegisterDTO;
import org.example.myfirstproject.Models.Entities.BaseEntity;
import org.example.myfirstproject.Models.Entities.User;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;

public interface UserService extends UserDetailsService {
    boolean registerUser(UserRegisterDTO userRegisterDTO);

    Object findByUsername(String username);

    boolean isPasswordCorrect(User user, String password);

    @Transactional
    void updateAdminSettings(SettingsDTO settingsDTO);

    void updateUserSettings(SettingsDTO settingsDTO);

    User getUserByUsername(String username);


        List<User> getAllUsers();
        void deleteUserById(Long id);

}
