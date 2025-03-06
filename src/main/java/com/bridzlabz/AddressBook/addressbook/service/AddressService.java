package com.bridzlabz.AddressBook.addressbook.service;

import com.bridzlabz.AddressBook.addressbook.dto.AddressDTO;
import com.bridzlabz.AddressBook.addressbook.model.Address;
import com.bridzlabz.AddressBook.addressbook.repository.AddressRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AddressService {

    @Autowired
    private AddressRepository addressRepository;

    // Get all addresses
    public List<Address> getAllAddresses() {
        return addressRepository.findAll();
    }

    // Get address by ID
    public Address getAddressById(Long id) {
        return addressRepository.findById(id).orElse(null);
    }

    // Add new address
    public Address addAddress(AddressDTO addressDTO) {
        Address address = new Address();
        address.setName(addressDTO.getName());
        address.setCity(addressDTO.getCity());
        address.setState(addressDTO.getState());
        address.setPhoneNumber(addressDTO.getPhoneNumber());
        return addressRepository.save(address);
    }

    // Update address by ID
    public Address updateAddress(Long id, AddressDTO addressDTO) {
        Optional<Address> optionalAddress = addressRepository.findById(id);
        if (optionalAddress.isPresent()) {
            Address address = optionalAddress.get();
            address.setName(addressDTO.getName());
            address.setCity(addressDTO.getCity());
            address.setState(addressDTO.getState());
            address.setPhoneNumber(addressDTO.getPhoneNumber());
            return addressRepository.save(address);
        }
        return null;
    }

    // Delete address by ID
    public boolean deleteAddress(Long id) {
        if (addressRepository.existsById(id)) {
            addressRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
