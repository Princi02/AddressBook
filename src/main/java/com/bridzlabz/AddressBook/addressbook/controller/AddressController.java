package com.bridzlabz.AddressBook.addressbook.controller;

import com.bridzlabz.AddressBook.addressbook.dto.AddressDTO;
import com.bridzlabz.AddressBook.addressbook.model.Address;
import com.bridzlabz.AddressBook.addressbook.service.AddressService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/addresses")
public class AddressController {

    @Autowired
    private AddressService addressService;

    @GetMapping("/")
    public ResponseEntity<List<Address>> getAllAddresses() {
        return ResponseEntity.ok(addressService.getAllAddresses());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Address> getAddressById(@PathVariable Long id) {
        Address address = addressService.getAddressById(id);
        return address != null ? ResponseEntity.ok(address) : ResponseEntity.notFound().build();
    }

    @PostMapping("/create")
    public ResponseEntity<Address> addAddress(@Valid @RequestBody AddressDTO addressDTO) {
        return ResponseEntity.ok(addressService.addAddress(addressDTO));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Address> updateAddress(@PathVariable Long id, @Valid @RequestBody AddressDTO addressDTO) {
        Address updatedAddress = addressService.updateAddress(id, addressDTO);
        return updatedAddress != null ? ResponseEntity.ok(updatedAddress) : ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteAddress(@PathVariable Long id) {
        return addressService.deleteAddress(id) ? ResponseEntity.ok("Address deleted successfully") : ResponseEntity.notFound().build();
    }
}
