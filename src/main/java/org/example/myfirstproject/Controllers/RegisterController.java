package org.example.myfirstproject.Controllers;


import jakarta.validation.Valid;
import org.example.myfirstproject.Models.DTO.UserRegisterDTO;
import org.example.myfirstproject.Services.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.ui.Model;

@Controller
public class RegisterController {
    private final UserService userService;



    public RegisterController(UserService userService) {
        this.userService = userService;
    }

    // Показва страницата за регистрация
    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        model.addAttribute("userRegisterDTO", new UserRegisterDTO());
        return "register";
    }

    // Обработва регистрацията
    @PostMapping("/register")
    public String registerUser(
            @Valid @ModelAttribute("userRegisterDTO") UserRegisterDTO userRegisterDTO,
            BindingResult bindingResult,
            Model model) {

        // Проверяваме за грешки при валидация
        if (bindingResult.hasErrors()) {
            return "register";
        }

        // Опитваме се да регистрираме потребителя
        boolean isRegistered = userService.registerUser(userRegisterDTO);

        if (!isRegistered) {
            model.addAttribute("registrationError", "Registration failed! Try again.");
            return "register";
        }

        // Ако регистрацията е успешна, пренасочваме към login страницата
        return "redirect:/login";
    }
}
