package ru.kata.spring.boot_security.demo.controller;


import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.entity.User;
import ru.kata.spring.boot_security.demo.repository.RoleRepository;
import ru.kata.spring.boot_security.demo.service.UserService;

import java.security.Principal;

@Controller
@RequestMapping("/user")
public class UserController {
private final UserService userService;
private final RoleRepository roleRepository;

    public UserController(UserService userService, RoleRepository roleRepository) {
        this.userService = userService;
        this.roleRepository = roleRepository;
    }

    @GetMapping("/profile")
    public String viewProfile(Principal principal, Model model) {
        String username = principal.getName();
        User user = userService.findByEmail(username);
        if (user != null) {
            model.addAttribute("user", user);
            return "user-profile";
        } else {
            return "redirect:/logout";
        }
    }
    @GetMapping("/update-profile")
    public String updateUserForm(Principal principal, Model model) {
        String username = principal.getName();
        User user = userService.findByEmail(username);
        if (user != null) {
            model.addAttribute("user", user);
            model.addAttribute("roles", roleRepository.findAll());
            return "profile-update";
        }
        return "redirect:/logout";
    }

    @PostMapping("/update-profile")
    public String updateUser(@ModelAttribute("user") User user, @RequestParam String password, Principal principal) {
        String username = principal.getName();
        User currentUser = userService.findByEmail(username);
        if (currentUser != null && currentUser.getId().equals(user.getId())) {
            userService.updateUser(user, password);
            return "redirect:/user/profile";
        }
        return "redirect:/logout";
    }
}