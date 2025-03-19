package com.bridzlabz.AddressBook.addressbook.service;

import com.bridzlabz.AddressBook.addressbook.dto.AddressDTO;
import com.bridzlabz.AddressBook.addressbook.exception.AddressNotFoundException;
import com.bridzlabz.AddressBook.addressbook.model.Address;
import com.bridzlabz.AddressBook.addressbook.repository.AddressRepository;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.CacheEvict;
//import org.springframework.cache.annotation.CachePut;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
@Slf4j
public class AddressService implements IAddressService {

    @Autowired
    private AddressRepository addressRepository;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    private static final String CACHE_KEY = "addresses";

    private final List<Address> addressList = new ArrayList<>();

    @PostConstruct
    public void loadDataFromDatabase() {
        addressList.clear();
        addressList.addAll(addressRepository.findAll());
        log.info("Loaded {} addresses from database", addressList.size());
        redisTemplate.opsForValue().set("addresses", addressList, 10, TimeUnit.MINUTES);
        System.out.println("Data cached in Redis.");
    }

    @Override
    @Cacheable(value = "addresses")
    public List<Address> getAllAddresses() {
        log.debug("Fetching all addresses");

        // Try fetching from Redis cache first
        List<Address> cachedAddresses = (List<Address>) redisTemplate.opsForValue().get(CACHE_KEY);
        if (cachedAddresses != null) {
            log.info("Fetched from Redis Cache");
            return cachedAddresses;
        }

        // If cache is empty, load from DB and cache it
        List<Address> addresses = addressRepository.findAll();
        redisTemplate.opsForValue().set(CACHE_KEY, addresses, 10, TimeUnit.MINUTES);
        log.info("Stored addresses in Redis Cache");
        return addresses;
    }

    @Override
    @Cacheable(value = "addresses", key = "#id")
    public Address getAddressById(Long id) {
        log.debug("Searching for address with ID: {}", id);

        // Try fetching from Redis
        Address cachedAddress = (Address) redisTemplate.opsForValue().get(CACHE_KEY + id);
        if (cachedAddress != null) {
            log.info("Fetched from Redis Cache");
            return cachedAddress;
        }

        // Fetch from database if not cached
        Address address = addressRepository.findById(id)
                .orElseThrow(() -> new AddressNotFoundException(id));

        // Cache the data
        redisTemplate.opsForValue().set(CACHE_KEY + id, address, 10, TimeUnit.MINUTES);
        log.info("Stored address with ID: {} in Redis Cache", id);
        return address;
    }

    @Override
    @CacheEvict(value = "addresses", allEntries = true) // Clear cache when data is added
    public Address addAddress(AddressDTO addressDTO) {
        log.info("Adding new address: {}", addressDTO);
        Address address = new Address();
        address.setName(addressDTO.getName());
        address.setCity(addressDTO.getCity());
        address.setState(addressDTO.getState());
        address.setPhoneNumber(addressDTO.getPhoneNumber());

        Address savedAddress = addressRepository.save(address);
        addressList.add(savedAddress);
        // Remove cache so new data is fetched from DB
        redisTemplate.delete(CACHE_KEY);
        log.info("New address added with ID: {}", savedAddress.getId());
//        System.out.println("address added");
        return savedAddress;
    }

    @Override
    @CacheEvict(value = "addresses", allEntries = true) // Clear cache when data is updated
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
        // Remove old cache and store updated data in cache
        redisTemplate.delete(CACHE_KEY);
        redisTemplate.opsForValue().set(CACHE_KEY + id, updatedAddress, 10, TimeUnit.MINUTES);

        log.info("Address with ID: {} updated successfully", id);
        return updatedAddress;
    }

    @Override
    @CacheEvict(value = "addresses", allEntries = true) // Clear cache when data is deleted
    public boolean deleteAddress(Long id) {
        if (!addressRepository.existsById(id)) {
            throw new AddressNotFoundException(id);
        }
        log.warn("Deleting address with ID: {}", id);

        addressRepository.deleteById(id);
        addressList.removeIf(address -> address.getId().equals(id));

        // Remove cache entry after deletion
        redisTemplate.delete(CACHE_KEY);
        redisTemplate.delete(CACHE_KEY + id);
        log.info("Address with ID: {} deleted successfully", id);
        return true;
    }


    @PostConstruct
    public void testRedisConnection() {
        try {
            redisTemplate.opsForValue().set("testKey", "Hello Redis", 10, TimeUnit.MINUTES);
            String value = (String) redisTemplate.opsForValue().get("testKey");
            System.out.println("Test Redis Value: " + value);
        } catch (Exception e) {
            System.err.println("Failed to connect to Redis: " + e.getMessage());
        }
    }

}
