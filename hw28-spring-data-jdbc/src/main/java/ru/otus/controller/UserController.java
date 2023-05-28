package ru.otus.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.otus.model.User;
import ru.otus.service.UserService;

@Controller
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping
    public String getAll(Model model) {
        model.addAttribute("users", userService.findAllUsers());
        return "users";
    }

    @PostMapping("/save")
    public String addUser(@RequestParam(value = "name") String name) {
        userService.createuser(new User(null, name));
        return "redirect:/users";
    }
}
