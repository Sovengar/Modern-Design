package com.jonathan.modern_design.user_module.infra;

import com.jonathan.modern_design.user_module.domain.User;
import com.jonathan.modern_design.user_module.domain.User.UserId;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

interface UserSpringRepo extends JpaRepository<User, Long> {
    Optional<User> findByUuid(UserId userId);
}
