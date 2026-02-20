package com.kivora.JsWater.domain.repository;

import com.kivora.JsWater.domain.model.user.User;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserRepository {

    void save(User user);

    boolean existsByUsername(String username);

    Optional<User> findByUsername(String username);

    Optional<User> findById(UUID id);

    List<User> findAll();
}
