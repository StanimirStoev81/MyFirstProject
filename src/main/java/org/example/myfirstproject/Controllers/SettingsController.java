package org.example.myfirstproject.Controllers;

import org.example.myfirstproject.Models.DTO.SettingsDTO;
import org.example.myfirstproject.Models.Entities.User;
import org.example.myfirstproject.Services.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import jakarta.validation.Valid;

@Controller
public class SettingsController {

    private final UserService userService;

    public SettingsController(UserService userService) {
        this.userService = userService;
    }

    // Показване на страницата с настройките
    @GetMapping("/admin/settings")
    public String showAdminSettings(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        User admin = (User) userService.findByUsername(username);  // Намираме админа
        SettingsDTO settingsDTO = new SettingsDTO();

        // Попълваме DTO-то с текущите данни
        settingsDTO.setFirstName(admin.getFirstName());
        settingsDTO.setLastName(admin.getLastName());
        settingsDTO.setEmail(admin.getEmail());
        settingsDTO.setPhoneNumber(admin.getPhoneNumber());
        settingsDTO.setUsername(admin.getUsername());

        model.addAttribute("settingsDTO", settingsDTO);
        return "admin-settings";
    }

    // Обработване на обновяването на данните
    @PostMapping("/admin/settings")
    public String updateAdminSettings(
            @Valid @ModelAttribute("settingsDTO") SettingsDTO settingsDTO,
            BindingResult bindingResult,
            Model model) {

        if (bindingResult.hasErrors()) {
            return "admin-settings";
        }

        userService.updateAdminSettings(settingsDTO);
        model.addAttribute("successMessage", "Settings updated successfully!");
        return "adminHome";
    }
    @GetMapping("/user/settings")
    public String showUserSettings(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        User user = (User) userService.findByUsername(username);  // Намираме админа
        SettingsDTO settingsDTO = new SettingsDTO();

        // Попълваме DTO-то с текущите данни
        settingsDTO.setFirstName(user.getFirstName());
        settingsDTO.setLastName(user.getLastName());
        settingsDTO.setEmail(user.getEmail());
        settingsDTO.setPhoneNumber(user.getPhoneNumber());
        settingsDTO.setUsername(user.getUsername());

        model.addAttribute("settingsDTO", settingsDTO);
        return "user-settings";
    }

    // Обработване на обновяването на данните
    @PostMapping("/user/settings")
    public String updateUserSettings(
            @Valid @ModelAttribute("settingsDTO") SettingsDTO settingsDTO,
            BindingResult bindingResult,
            Model model) {

        if (bindingResult.hasErrors()) {
            return "user-settings";
        }

        userService.updateUserSettings(settingsDTO);
        model.addAttribute("successMessage", "Settings updated successfully!");
        return "userHome";
    }
}
