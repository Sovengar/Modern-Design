package com.jonathan.modern_design.user_module;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface SpringUserRepository extends JpaRepository<UserEntity, UUID> {
}
