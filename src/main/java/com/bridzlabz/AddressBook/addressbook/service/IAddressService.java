package com.bridzlabz.AddressBook.addressbook.service;

import com.bridzlabz.AddressBook.addressbook.dto.AddressDTO;
import com.bridzlabz.AddressBook.addressbook.model.Address;

import java.util.List;

public interface IAddressService {
    List<Address> getAllAddresses();
    Address getAddressById(Long id);
    Address addAddress(AddressDTO addressDTO);
    Address updateAddress(Long id, AddressDTO addressDTO);
    boolean deleteAddress(Long id);
}
