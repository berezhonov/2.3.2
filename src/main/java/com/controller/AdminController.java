package com.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import com.model.User;
import com.service.UserService;

import java.util.List;

@Controller
@RequestMapping(value = "/admin")
public class AdminController {

    private final UserService userService;

    public AdminController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public String getUsers(Model model) {
        model.addAttribute("userList", userService.findAll());
        model.addAttribute("allRoles", userService.getAllRoles());
        return "admin/admin_panel";
    }

    @PostMapping
    public String addUser(@RequestParam String username,
                          @RequestParam String lastname,
                          @RequestParam String email,
                          @RequestParam int age,
                          @RequestParam String password,
                          @RequestParam(required = false) List<String> roleNames) {
        userService.saveUserWithRoles(
                new User(username, lastname, age, email),
                password,
                roleNames);
        return "redirect:/admin";
    }

    @GetMapping("/update")
    public String showUserUpdatePage(@RequestParam("id") Long id,
                                     Model model) {
        model.addAllAttributes(userService.makeAllUserModelAttributes(id));
        return "admin/admin_panel";
    }

    @PostMapping("/update")
    public String updateUser(@RequestParam("id") Long id,
                             @RequestParam(value = "username", required = false) String username,
                             @RequestParam(value = "lastname", required = false) String lastname,
                             @RequestParam(value = "email", required = false) String email,
                             @RequestParam(value = "age", required = false) Integer age,
                             @RequestParam(value = "password", required = false) String password,
                             @RequestParam(value = "roles", required = false) List<String> roleNames) {
        userService.update(
                id,
                new User(username, lastname, age, email),
                password,
                roleNames);
        return "redirect:/admin";
    }

    @PostMapping("/delete")
    public String deleteUser(@RequestParam Long id) {
        userService.delete(id);
        return "redirect:/admin";
    }

    @GetMapping("/user")
    public String findUserById(@RequestParam("id") Long id, Model model) {
        model.addAttribute("user", userService.findById(id));
        return "admin/admin_panel";
    }
}
