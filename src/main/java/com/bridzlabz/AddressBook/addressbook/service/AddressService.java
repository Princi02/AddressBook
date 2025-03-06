package com.bridzlabz.AddressBook.addressbook.service;

import com.bridzlabz.AddressBook.addressbook.dto.AddressDTO;
import com.bridzlabz.AddressBook.addressbook.model.Address;
import com.bridzlabz.AddressBook.addressbook.repository.AddressRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class AddressService {

    @Autowired
    private AddressRepository addressRepository;

    private final List<Address> addressList = new ArrayList<>();

    // Load data from DB into memory at startup
    @PostConstruct
    public void loadDataFromDatabase() {
        addressList.clear(); // Clear existing data in case of reload
        addressList.addAll(addressRepository.findAll());
        System.out.println("Loaded " + addressList.size() + " addresses from database.");
    }

    // Get all addresses
    public List<Address> getAllAddresses() {
        return addressList;
    }

    // Get address by ID
    public Address getAddressById(Long id) {
        return addressList.stream()
                .filter(address -> address.getId().equals(id))
                .findFirst()
                .orElse(null);
    }

    // Add new address (Save to DB and In-Memory List)
    public Address addAddress(AddressDTO addressDTO) {
        Address address = new Address();
        address.setName(addressDTO.getName());
        address.setCity(addressDTO.getCity());
        address.setState(addressDTO.getState());
        address.setPhoneNumber(addressDTO.getPhoneNumber());

        Address savedAddress = addressRepository.save(address); // Save to database
        addressList.add(savedAddress); // Add to in-memory list
        return savedAddress;
    }

    // Update address by ID (Update in DB and In-Memory List)
    public Address updateAddress(Long id, AddressDTO addressDTO) {
        Optional<Address> optionalAddress = addressRepository.findById(id);
        if (optionalAddress.isPresent()) {
            Address address = optionalAddress.get();
            address.setName(addressDTO.getName());
            address.setCity(addressDTO.getCity());
            address.setState(addressDTO.getState());
            address.setPhoneNumber(addressDTO.getPhoneNumber());

            Address updatedAddress = addressRepository.save(address); // Update in DB
            addressList.replaceAll(addr -> addr.getId().equals(id) ? updatedAddress : addr); // Update in-memory list
            return updatedAddress;
        }
        return null;
    }

    // Delete address by ID (Remove from DB and In-Memory List)
    public boolean deleteAddress(Long id) {
        if (addressRepository.existsById(id)) {
            addressRepository.deleteById(id); // Delete from DB
            addressList.removeIf(address -> address.getId().equals(id)); // Remove from in-memory list
            return true;
        }
        return false;
    }
}
