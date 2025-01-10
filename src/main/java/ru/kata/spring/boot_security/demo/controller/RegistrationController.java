package ru.kata.spring.boot_security.demo.controller;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import ru.kata.spring.boot_security.demo.controller.error.EmailAlreadyExistsException;
import ru.kata.spring.boot_security.demo.entity.User;
import ru.kata.spring.boot_security.demo.repository.RoleRepository;
import ru.kata.spring.boot_security.demo.service.UserService;

@Controller
@RequestMapping("/register")
@RequiredArgsConstructor
public class RegistrationController {

    private final UserService userService;
    private final RoleRepository roleRepository;

    @GetMapping
    public String registerForm(Model model) {
        model.addAttribute("user", new User());
        model.addAttribute("roles", roleRepository.findAll());
        return "register";
    }

    @PostMapping
    public String registerUser(@ModelAttribute("user") User user, Model model,
                               @RequestParam String password, RedirectAttributes redirectAttributes) {
        try {
            userService.saveUser(user, password);
            redirectAttributes.addFlashAttribute("message", "User registered successfully, Please log in.");
            return "redirect:/login";
        } catch (EmailAlreadyExistsException e) {
            model.addAttribute("errorMessage", "Email is already registered.");
            model.addAttribute("roles", roleRepository.findAll());
            return "register";
        }
    }
}

