package com.bridzlabz.AddressBook.addressbook.service;

import com.bridzlabz.AddressBook.addressbook.dto.AuthUserDTO;
import com.bridzlabz.AddressBook.addressbook.dto.LoginDTO;
import com.bridzlabz.AddressBook.addressbook.model.AuthUser;
import com.bridzlabz.AddressBook.addressbook.repository.AuthUserRepository;
import com.bridzlabz.AddressBook.addressbook.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthService implements IAuthService {

    @Autowired
    private AuthUserRepository userRepository;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private EmailService emailService;

    @Override
    public String registerUser(AuthUserDTO userDTO) {
        if (userRepository.findByEmail(userDTO.getEmail()).isPresent()) {
            throw new RuntimeException("Email is already registered");
        }

        AuthUser user = new AuthUser();
        user.setFirstName(userDTO.getFirstName());
        user.setLastName(userDTO.getLastName());
        user.setEmail(userDTO.getEmail());
        user.setPassword(passwordEncoder.encode(userDTO.getPassword()));

        userRepository.save(user);

        // Send welcome email
        emailService.sendSimpleEmail(user.getEmail(),
                "Welcome to Address Book",
                "Your account has been created successfully!");

        return "User registered successfully";
    }

    @Override
    public String loginUser(LoginDTO loginDTO) {
        AuthUser user = userRepository.findByEmail(loginDTO.getEmail())
                .orElseThrow(() -> new RuntimeException("Invalid email or password"));

        if (!passwordEncoder.matches(loginDTO.getPassword(), user.getPassword())) {
            throw new RuntimeException("Invalid email or password");
        }

        // ✅ Send login notification
        sendLoginNotification(user.getEmail());

        return jwtUtil.generateToken(user.getEmail());
    }

    @Override
    public void sendLoginNotification(String email) {
        emailService.sendSimpleEmail(email,
                "Login Alert",
                "You have successfully logged into your account.");
    }

    @Override
    public String forgotPassword(String email, String newPassword) {
        Optional<AuthUser> userOptional = userRepository.findByEmail(email);

        if (userOptional.isEmpty()) {
            throw new RuntimeException("User not found");
        }

        AuthUser user = userOptional.get();
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);

        emailService.sendSimpleEmail(email,
                "Password Changed",
                "Your password has been updated.");

        return "Password updated successfully";
    }

    @Override
    public String resetPassword(String email, String currentPassword, String newPassword) {
        AuthUser user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found with email: " + email));

        if (!passwordEncoder.matches(currentPassword, user.getPassword())) {
            throw new RuntimeException("Current password is incorrect!");
        }

        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);

        emailService.sendSimpleEmail(email,
                "Password Reset Confirmation",
                "Your password has been reset successfully. If you didn't perform this action, please contact support.");

        return "Password reset successfully!";
    }
}
