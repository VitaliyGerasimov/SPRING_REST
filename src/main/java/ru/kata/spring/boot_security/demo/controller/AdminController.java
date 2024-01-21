package ru.kata.spring.boot_security.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.service.UserService;

import java.security.Principal;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final UserService userService;
    private static final String REDIRECT_ADMIN = "redirect:/admin";


    @Autowired
    public AdminController(UserService userService) {
        this.userService = userService;
    }


    @GetMapping
    public String showAllUsers(Principal principal, Model model, @AuthenticationPrincipal User user) {
        model.addAttribute("users", userService.allUsers());
        model.addAttribute("admin", userService.findByUsername(principal.getName()));
        model.addAttribute("newUser", new User());
        model.addAttribute("rolesAdd", userService.getAllRoles());
        return "admin";
    }

    @PostMapping("/delete/{id}")
    public String delete(@PathVariable("id") long id) {
        userService.deleteUser(id);
        return REDIRECT_ADMIN;
    }

    @PostMapping("/{id}")
    public String update(@ModelAttribute("user") User user, @PathVariable("id") long id) {
        userService.update(id, user);
        return REDIRECT_ADMIN;
    }

    @PostMapping()
    public String addCreateNewUser(@ModelAttribute("user") User user) {
        userService.createNewUser(user);
        return REDIRECT_ADMIN;
    }
}
