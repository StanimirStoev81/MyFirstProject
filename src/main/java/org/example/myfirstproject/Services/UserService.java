package org.example.myfirstproject.Services;

import org.example.myfirstproject.Models.DTO.UserRegisterDTO;
import org.example.myfirstproject.Models.Entities.User;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService extends UserDetailsService {
    boolean registerUser(UserRegisterDTO userRegisterDTO);

    Object findByUsername(String username);

    boolean isPasswordCorrect(User user, String password);
}
