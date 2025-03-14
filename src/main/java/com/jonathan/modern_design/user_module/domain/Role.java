package com.jonathan.modern_design.user_module.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;

import static lombok.AccessLevel.PRIVATE;

@Entity
@Table(name = "roles", schema = "md")
@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE) //For Hibernate
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Role {
    @EmbeddedId
    private Role.Code code;
    private String description;

    public static Role of(Roles role) {
        return new Role(new Code(role.getCode()), role.getDescription());
    }

    //Avoid indirect link with user

    @Data //Not a record because ORM needs mutability
    @Setter(PRIVATE)
    @AllArgsConstructor
    @NoArgsConstructor(access = AccessLevel.PROTECTED) //For Hibernate
    @Embeddable
    public static class Code implements Serializable {
        @Serial private static final long serialVersionUID = -491353586550215623L;
        @Column(name = "code", updatable = false)
        @NotNull
        private String roleCode;
    }
}
