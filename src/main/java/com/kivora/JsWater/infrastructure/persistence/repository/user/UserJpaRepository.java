package com.kivora.JsWater.infrastructure.persistence.repository.user;

import com.kivora.JsWater.infrastructure.persistence.entity.user.UserJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface UserJpaRepository extends JpaRepository<UserJpaEntity, UUID> {

    boolean existsByName(String name);

    Optional<UserJpaEntity> findByName(String name);
}
