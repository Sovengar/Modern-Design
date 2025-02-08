package com.jonathan.modern_design.user_module.vo;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class UserRealNameVO {
    private final String name;

    public static UserRealNameVO of(String name) {
        if (name.matches(".*\\d.*")) {
            throw new IllegalArgumentException("El nombre no debe contener números.");
        }

        return new UserRealNameVO(name);
    }

    @Override
    public String toString() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UserRealNameVO nameVO)) return false;
        return name.equals(nameVO.name);
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }

    public String getName() {
        return name;
    }

}
