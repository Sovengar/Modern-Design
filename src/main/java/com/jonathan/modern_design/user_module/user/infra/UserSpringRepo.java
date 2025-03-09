package com.jonathan.modern_design.user_module.user.infra;

import com.jonathan.modern_design.user_module.user.domain.model.User;
import com.jonathan.modern_design.user_module.user.domain.model.User.UserId;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserSpringRepo extends JpaRepository<User, Long> {
    Optional<User> findByUuid(UserId userId);
}
