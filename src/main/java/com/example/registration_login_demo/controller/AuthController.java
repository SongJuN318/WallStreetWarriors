package com.example.registration_login_demo.controller;

import java.security.Principal;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.example.registration_login_demo.dto.UserDto;
import com.example.registration_login_demo.entity.User;
import com.example.registration_login_demo.service.NotificationService;
import com.example.registration_login_demo.service.UserService;

import jakarta.validation.Valid;

@Controller
public class AuthController {

    private UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/settings")
    public String settings() {
        return "settings";
    }


    // handler method to handle home page request
    @GetMapping("/")
    public String home() {
        return "index";
    }

    // handler method to handle user registration form request
    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        // create model object to store form data
        UserDto user = new UserDto();
        model.addAttribute("user", user);
        return "register";
    }

    // handler method to handle user registration form submit request
    @PostMapping("/register/save")
    public String registration(@Valid @ModelAttribute("user") UserDto userDto,
            BindingResult result,
            Model model) {
        User existingUser = userService.findUserByEmail(userDto.getEmail());

        if (existingUser != null && existingUser.getEmail() != null && !existingUser.getEmail().isEmpty()) {
            result.rejectValue("email", null,
                    "There is already an account registered with the same email");
        }

        if (result.hasErrors()) {
            model.addAttribute("user", userDto);
            return "/register";
        }
        userService.saveUser(userDto);

        String recipientEmail = userDto.getEmail();
        NotificationService notificationService = new NotificationService(recipientEmail);
        notificationService.sendRegistrationEmail(recipientEmail, userDto.getLastName());

        return "redirect:/register?success";
    }

    // handler method to handle list of users
    @GetMapping("/users")
    public String users(Model model) {
        List<UserDto> users = userService.findAllUsers();
        model.addAttribute("users", users);
        return "users";
    }

    // handler method to handle login request
    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/delete/{id}")
    public String deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return "redirect:/users?success";
    }

    @GetMapping("/homepage")
    public String homepage(Model model, Principal principal) {
        model.addAttribute("profileName", currentUserName(principal));
        return "homepage";
    }

    @RequestMapping(value = "/username", method = RequestMethod.GET)
    @ResponseBody
    public String currentUserName(Principal principal) {
        User user = userService.findUserByEmail(principal.getName());
        String profileName = user.getName();
        return profileName;
    }

    @RequestMapping(value = "/uid", method = RequestMethod.GET)
    @ResponseBody
    public Long currentUserId(Principal principal) {
        User user = userService.findUserByEmail(principal.getName());
        Long userId = user.getId();
        return userId;
    }

}
