package com.jonathan.modern_design.user_module.user.infra;

import com.jonathan.modern_design.user_module.user.domain.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserSpringRepo extends JpaRepository<User, Long> {
    Optional<User> findByUuid(User.ID id);
}
