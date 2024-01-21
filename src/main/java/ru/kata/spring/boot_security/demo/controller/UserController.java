package ru.kata.spring.boot_security.demo.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.service.UserService;




@Controller
@RequestMapping("/")
public class UserController {
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }
    @GetMapping("/")
    public String welcome() {
        return "welcome";
    }

    @GetMapping("/login")
    public String loginPage() {
        return "loginPage";
    }

    @GetMapping("/user")
    public String userHome(@AuthenticationPrincipal UserDetails userDetails, Model model) {
        if (userDetails != null) {
            User user = userService.findByUsername(userDetails.getUsername());
            if (user != null) {
                model.addAttribute("user", userService.getUser(user.getId()));
                System.out.println("Успешно: user id" + user.getId());
                model.addAttribute("titleTable", "Страница пользователя: ");
            }
        }
        return "user";
    }
}
