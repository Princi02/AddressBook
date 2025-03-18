package com.bridzlabz.AddressBook.addressbook.service;

public interface IEmailService {
    void sendSimpleEmail(String to, String subject, String text);
}
