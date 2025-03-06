package org.example.myfirstproject.Services;

import org.example.myfirstproject.Models.DTO.UserRegisterDTO;

public interface UserService {
    boolean registerUser(UserRegisterDTO userRegisterDTO);
}
