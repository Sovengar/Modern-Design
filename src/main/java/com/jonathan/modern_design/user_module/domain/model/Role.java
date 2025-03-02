package com.jonathan.modern_design.user_module.domain.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

enum Roles {
    ADMIN("ADM", "Admin"),
    TECHNICIAN("TEC", "Technician"),
    USER("USER", "User");

    private final String code;
    private final String description;

    Roles(String code, String description) {
        this.code = code;
        this.description = description;
    }

    public String getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }
}

@Entity
@Table(name = "roles", schema = "md")
@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
class Role {
    private final String description;
    @Id
    private final String code;

    public static Role of(String description, String code) {
        return new Role(description, code);
    }

    //Avoid indirect link with user
}
