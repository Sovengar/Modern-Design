package com.jonathan.modern_design.user_module.domain;

import com.jonathan.modern_design.user_module.domain.model.User;

public interface UserRepository {
    User createUser(User user);
}
