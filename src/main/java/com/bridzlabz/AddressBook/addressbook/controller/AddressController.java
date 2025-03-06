package com.bridzlabz.AddressBook.addressbook.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/addresses")
public class AddressController {

    @GetMapping("/")
    public ResponseEntity<String> getAllAddresses() {
        return ResponseEntity.ok("GET: Fetching all addresses...");
    }

    @GetMapping("/{id}")
    public ResponseEntity<String> getAddressById(@PathVariable Long id) {
        return ResponseEntity.ok("GET: Fetching address with ID " + id);
    }

    @PostMapping("/create")
    public ResponseEntity<String> addAddress() {
        System.out.println("Adding a new contact...");
        return ResponseEntity.ok("POST: New contact added successfully!");
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> updateAddress(@PathVariable Long id) {
        return ResponseEntity.ok("PUT: Contact with ID " + id + " updated!");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteAddress(@PathVariable Long id) {
        return ResponseEntity.ok("DELETE: Contact with ID " + id + " deleted!");
    }
}
