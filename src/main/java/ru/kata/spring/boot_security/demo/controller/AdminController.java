package ru.kata.spring.boot_security.demo.controller;


import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import ru.kata.spring.boot_security.demo.entity.User;
import ru.kata.spring.boot_security.demo.repository.RoleRepository;
import ru.kata.spring.boot_security.demo.service.UserService;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/admin/")
public class AdminController {

    private final UserService userService;
    private final RoleRepository roleRepository;

    public AdminController(UserService userService, RoleRepository roleRepository) {
        this.userService = userService;
        this.roleRepository = roleRepository;
    }

    @GetMapping("/users")
    public String findAll(Model model) {
        List<User> users = userService.findAll();
        model.addAttribute("users", users.isEmpty() ? null : users);
        return "user-list";
    }

    @GetMapping("/user-create")
    public String createUserForm(Model model) {
        model.addAttribute("user", new User());
        model.addAttribute("roles", roleRepository.findAll());
        return "user-create";
    }

    @PostMapping("admin/user-create")
    public String createUser(@ModelAttribute User user, @RequestParam String password, BindingResult result, RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            return "user-create";
        }
        userService.saveUser(user, password);
        redirectAttributes.addFlashAttribute("message", "User created successfully!");
        return "redirect:/admin/users";
    }

    @GetMapping("/user-update")
    public String updateUserForm(@RequestParam("id") Long id, Model model) {
        Optional<User> userOptional = userService.findById(id);
        if (userOptional.isPresent()) {
            model.addAttribute("user", userOptional.get());
            model.addAttribute("roles", roleRepository.findAll());
            return "user-update";
        } else {
            return "redirect:/admin/users";
        }
    }

    @PostMapping("/user-update")
    public String updateUser(User user, @RequestParam String password) {
        userService.updateUser(user, password);
        return "redirect:/admin/users";
    }

    @GetMapping("/user-delete")
    public String deleteUser(@RequestParam("id") Long id, RedirectAttributes redirectAttributes) {
        userService.deleteUser(id);
        redirectAttributes.addFlashAttribute("message", "User deleted successfully!");
        return "redirect:/admin/users";
    }
}

