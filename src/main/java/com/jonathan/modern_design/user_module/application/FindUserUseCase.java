package com.jonathan.modern_design.user_module.application;

import java.util.UUID;

public interface FindUserUseCase {
    UserResource findUser(UUID uuid);
}
