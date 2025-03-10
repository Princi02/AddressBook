package com.bridzlabz.AddressBook.addressbook.service;

import com.bridzlabz.AddressBook.addressbook.dto.AddressDTO;
import com.bridzlabz.AddressBook.addressbook.exception.AddressNotFoundException;
import com.bridzlabz.AddressBook.addressbook.model.Address;
import com.bridzlabz.AddressBook.addressbook.repository.AddressRepository;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class AddressService {

    @Autowired
    private AddressRepository addressRepository;

    private final List<Address> addressList = new ArrayList<>();

    @PostConstruct
    public void loadDataFromDatabase() {
        addressList.clear();
        addressList.addAll(addressRepository.findAll());
        log.info("Loaded {} addresses from database", addressList.size());
    }

    public List<Address> getAllAddresses() {
        log.debug("Fetching all addresses");
        return addressList;
    }

    public Address getAddressById(Long id) {
        log.debug("Searching for address with ID: {}", id);
        return addressList.stream()
                .filter(address -> address.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new AddressNotFoundException(id)); // Throw exception if not found
    }

    public Address addAddress(AddressDTO addressDTO) {
        log.info("Adding new address: {}", addressDTO);
        Address address = new Address();
        address.setName(addressDTO.getName());
        address.setCity(addressDTO.getCity());
        address.setState(addressDTO.getState());
        address.setPhoneNumber(addressDTO.getPhoneNumber());

        Address savedAddress = addressRepository.save(address);
        addressList.add(savedAddress);
        return savedAddress;
    }

    public Address updateAddress(Long id, AddressDTO addressDTO) {
        log.info("Updating address with ID: {}", id);
        Address address = addressRepository.findById(id)
                .orElseThrow(() -> new AddressNotFoundException(id)); // Throw exception if not found

        address.setName(addressDTO.getName());
        address.setCity(addressDTO.getCity());
        address.setState(addressDTO.getState());
        address.setPhoneNumber(addressDTO.getPhoneNumber());

        Address updatedAddress = addressRepository.save(address);
        addressList.replaceAll(addr -> addr.getId().equals(id) ? updatedAddress : addr);
        return updatedAddress;
    }

    public boolean deleteAddress(Long id) {
        if (!addressRepository.existsById(id)) {
            throw new AddressNotFoundException(id); // Throw exception if not found
        }
        log.warn("Deleting address with ID: {}", id);
        addressRepository.deleteById(id);
        addressList.removeIf(address -> address.getId().equals(id));
        return true;
    }
}
