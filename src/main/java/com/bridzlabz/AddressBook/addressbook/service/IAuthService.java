package com.bridzlabz.AddressBook.addressbook.service;

import com.bridzlabz.AddressBook.addressbook.dto.AuthUserDTO;
import com.bridzlabz.AddressBook.addressbook.dto.LoginDTO;

public interface IAuthService {
    String registerUser(AuthUserDTO userDTO);
    String loginUser(LoginDTO loginDTO);
    String forgotPassword(String email, String newPassword);
    String resetPassword(String email, String currentPassword, String newPassword);
    void sendLoginNotification(String email);
}
