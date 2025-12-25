package com.congty9a4.backend.controller;

import com.congty9a4.backend.entity.Userchan;
import com.congty9a4.backend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping
    public List<Userchan> getAllUsers() {
        return userService.getAllUsers();
    }

    @GetMapping("/{id}")
    public Userchan getUserById(@PathVariable UUID id) {
        return userService.getUserById(id);
    }

    @PostMapping
    public Userchan createUser(@RequestBody Userchan user) {
        return userService.createUser(user);
    }

    @PutMapping("/{id}")
    public Userchan updateUser(@PathVariable UUID id, @RequestBody Userchan user) {
        return userService.updateUser(id, user);
    }

    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable UUID id) {
        userService.deleteUser(id);
    }
}

