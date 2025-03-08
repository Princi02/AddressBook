package com.bridzlabz.AddressBook.addressbook.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data  // Lombok will generate Getters, Setters, toString, equals, and hashCode
@NoArgsConstructor  // No-args constructor
@AllArgsConstructor // Constructor with all fields
public class Address {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Name is required")
    private String name;

    @NotBlank(message = "City is required")
    private String city;

    @NotBlank(message = "State is required")
    private String state;

    @NotBlank(message = "Phone number is required")
    private String phoneNumber;
}
