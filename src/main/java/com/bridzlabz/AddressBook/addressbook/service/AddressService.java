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

@Service
@Slf4j
public class AddressService implements IAddressService {

    @Autowired
    private AddressRepository addressRepository;

    private final List<Address> addressList = new ArrayList<>();

    @PostConstruct
    public void loadDataFromDatabase() {
        addressList.clear();
        addressList.addAll(addressRepository.findAll());
        log.info("Loaded {} addresses from database", addressList.size());
    }

    @Override
    public List<Address> getAllAddresses() {
        log.debug("Fetching all addresses");
        return addressList;
    }

    @Override
    public Address getAddressById(Long id) {
        log.debug("Searching for address with ID: {}", id);
        return addressList.stream()
                .filter(address -> address.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new AddressNotFoundException(id));
    }

    @Override
    public Address addAddress(AddressDTO addressDTO) {
        log.info("Adding new address: {}", addressDTO);
        Address address = new Address();
        address.setName(addressDTO.getName());
        address.setCity(addressDTO.getCity());
        address.setState(addressDTO.getState());
        address.setPhoneNumber(addressDTO.getPhoneNumber());

        Address savedAddress = addressRepository.save(address);
        addressList.add(savedAddress);
        log.info("New address added with ID: {}", savedAddress.getId());
        return savedAddress;
    }

    @Override
    public Address updateAddress(Long id, AddressDTO addressDTO) {
        log.info("Updating address with ID: {}", id);
        Address address = addressRepository.findById(id)
                .orElseThrow(() -> new AddressNotFoundException(id));

        address.setName(addressDTO.getName());
        address.setCity(addressDTO.getCity());
        address.setState(addressDTO.getState());
        address.setPhoneNumber(addressDTO.getPhoneNumber());

        Address updatedAddress = addressRepository.save(address);
        addressList.replaceAll(addr -> addr.getId().equals(id) ? updatedAddress : addr);
        log.info("Address with ID: {} updated successfully", id);
        return updatedAddress;
    }

    @Override
    public boolean deleteAddress(Long id) {
        if (!addressRepository.existsById(id)) {
            throw new AddressNotFoundException(id);
        }
        log.warn("Deleting address with ID: {}", id);
        addressRepository.deleteById(id);
        addressList.removeIf(address -> address.getId().equals(id));
        log.info("Address with ID: {} deleted successfully", id);
        return true;
    }
}
