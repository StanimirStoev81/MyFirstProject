package org.example.myfirstproject.Controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {
    @GetMapping("/")
    public String home() {
        return "index";
    }

    @GetMapping("/adminHome")
    public String adminHome() {
        return "adminHome"; // Трябва да имаш adminHome.html в templates
    }

    @GetMapping("/userHome")
    public String userHome() {
        return "userHome"; // Трябва да имаш userHome.html в templates
    }
}
