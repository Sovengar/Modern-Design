package com.jonathan.modern_design.user_module.domain.model;

import lombok.Builder;

import java.util.UUID;

@Builder
public class User {

    private UUID uuid;
    private String name;
    private String firstname;
    private String lastname;
    private String email;
    private String password;
    private String country;

    private User(UUID uuid, String name, String firstname, String lastname, String email, String password, String country) {
        this.uuid = uuid;
        this.name = name;
        this.firstname = firstname;
        this.lastname = lastname;
        this.email = email;
        this.password = password; //TODO ENCRIPTAR
        this.country = country; //TODO ENUM
    }

    public static User create(String name, String firstname, String lastname, String email, String password, String country) {
        return new User(UUID.randomUUID(), name, firstname, lastname, email, password, country);
    }
}
