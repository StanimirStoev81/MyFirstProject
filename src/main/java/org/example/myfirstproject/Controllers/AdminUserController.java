package org.example.myfirstproject.Controllers;

import org.example.myfirstproject.Models.Entities.User;
import org.example.myfirstproject.Services.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/admin/users")
public class AdminUserController {

    private final UserService userService;

    public AdminUserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public String getAllUsers(Model model) {
        List<User> users = userService.getAllUsers()
                .stream()
                .filter(user -> !user.getId().equals(1L)) // Филтрираме потребителя с ID = 1
                .collect(Collectors.toList());
        model.addAttribute("users", users);
        return "admin-users";
    }

    @PostMapping("/delete/{id}")
    public String deleteUser(@PathVariable Long id) {
        userService.deleteUserById(id);
        return "redirect:/admin/users";
    }
}
