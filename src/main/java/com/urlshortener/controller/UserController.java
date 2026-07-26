package com.urlshortener.controller;

import com.urlshortener.dto.LoginRegistrationRequest;
import com.urlshortener.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@RequestBody LoginRegistrationRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.registerUser(request));
    }

    @GetMapping("/login")
    public ResponseEntity<String> loginUser(@RequestBody LoginRegistrationRequest request) {
        return ResponseEntity.ok(userService.loginUser(request));
    }

}
