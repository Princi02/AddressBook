package com.bridzlabz.AddressBook.addressbook.controller;

import com.bridzlabz.AddressBook.addressbook.dto.AuthUserDTO;
import com.bridzlabz.AddressBook.addressbook.dto.LoginDTO;
import com.bridzlabz.AddressBook.addressbook.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@RequestBody AuthUserDTO userDTO) {
        return ResponseEntity.ok(authService.registerUser(userDTO));
    }

    @PostMapping("/login")
    public ResponseEntity<String> loginUser(@RequestBody LoginDTO loginDTO) {
        String token = authService.loginUser(loginDTO);
        return ResponseEntity.ok("Bearer " + token);
    }
}
