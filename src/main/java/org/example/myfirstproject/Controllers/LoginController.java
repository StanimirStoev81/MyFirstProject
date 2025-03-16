package org.example.myfirstproject.Controllers;

import jakarta.validation.Valid;
import org.example.myfirstproject.Models.DTO.UserLoginDTO;
import org.example.myfirstproject.Models.Entities.User;
import org.example.myfirstproject.Services.Impl.UserServiceImpl;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.ui.Model;



@Controller
public class LoginController {

    private final UserServiceImpl userServiceImpl;
    private final AuthenticationManager authenticationManager;

    public LoginController(UserServiceImpl userServiceImpl, AuthenticationManager authenticationManager) {
        this.userServiceImpl = userServiceImpl;
        this.authenticationManager = authenticationManager;

    }

    // Показва логин страницата
    @GetMapping("/login")
    public String showLoginForm(Model model) {
        model.addAttribute("userLoginDTO", new UserLoginDTO());
        return "login";
    }

    // Обработва логина
    @PostMapping("/login")
    public String loginUser(
            @Valid @ModelAttribute("userLoginDTO") UserLoginDTO userLoginDTO,
            BindingResult bindingResult,
            Model model) {

        if (bindingResult.hasErrors()) {
            return "login";
        }

        try {
            // Извършваме аутентикацията чрез Spring Security
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            userLoginDTO.getUsername(),
                            userLoginDTO.getPassword()
                    )
            );

            // Ако аутентикацията е успешна, проверяваме ролята
            if (authentication.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ADMIN"))) {
                return "redirect:/adminHome";
            } else {
                return "redirect:/userHome";
            }

        } catch (UsernameNotFoundException e) {

            model.addAttribute("usernameError", "Invalid username.");
            return "login";
        } catch (AuthenticationException e) {

            // Ако паролата е грешна
            model.addAttribute("passwordError", "Invalid password.");
            return "login";
        }

    }
    }

