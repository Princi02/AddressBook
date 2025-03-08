package com.bridzlabz.AddressBook.addressbook.controller;

import com.bridzlabz.AddressBook.addressbook.dto.AddressDTO;
import com.bridzlabz.AddressBook.addressbook.model.Address;
import com.bridzlabz.AddressBook.addressbook.service.AddressService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/addresses")
@Slf4j // Lombok Logging
public class AddressController {

    @Autowired
    private AddressService addressService;


    // Get all addresses
    @GetMapping("/")
    public ResponseEntity<List<Address>> getAllAddresses() {
        log.info("Fetching all addresses");
        return ResponseEntity.ok(addressService.getAllAddresses());
    }


    // Get address by ID
    @GetMapping("/{id}")
    public ResponseEntity<Address> getAddressById(@PathVariable Long id) {
        log.info("Fetching address with ID: {}", id);
        Address address = addressService.getAddressById(id);
        return address != null ? ResponseEntity.ok(address) : ResponseEntity.notFound().build();
    }


    // Add new address
    @PostMapping("/create")
    public ResponseEntity<Address> addAddress(@Valid @RequestBody AddressDTO addressDTO) {
        log.info("Creating new address: {}", addressDTO);
        return ResponseEntity.ok(addressService.addAddress(addressDTO));
    }


    // Update address by ID
    @PutMapping("/{id}")
    public ResponseEntity<Address> updateAddress(@PathVariable Long id, @Valid @RequestBody AddressDTO addressDTO) {
        log.info("Updating address with ID: {}", id);
        Address updatedAddress = addressService.updateAddress(id, addressDTO);
        return updatedAddress != null ? ResponseEntity.ok(updatedAddress) : ResponseEntity.notFound().build();
    }


    // delete address by ID
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteAddress(@PathVariable Long id) {
        log.warn("Deleting address with ID: {}", id);
        return addressService.deleteAddress(id) ? ResponseEntity.ok("Address deleted successfully") : ResponseEntity.notFound().build();
    }
}

