package com.kivora.JsWater.infrastructure.persistence.repository.user;

import com.kivora.JsWater.domain.model.user.User;
import com.kivora.JsWater.domain.repository.UserRepository;
import com.kivora.JsWater.infrastructure.persistence.mapper.user.UserMapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public class UserRepositoryImpl implements UserRepository {

    private final UserJpaRepository jpa;

    public UserRepositoryImpl(UserJpaRepository jpa) {
        this.jpa = jpa;
    }

    @Override
    public void save(User user) {
        jpa.save(UserMapper.toJpa(user));
    }

    @Override
    public boolean existsByUsername(String username) {
        return jpa.existsByName(username);
    }

    @Override
    public Optional<User> findByUsername(String username) {
        return jpa.findByName(username).map(UserMapper::toDomain);
    }

    @Override
    public Optional<User> findById(UUID id) {
        return jpa.findById(id).map(UserMapper::toDomain);
    }

    @Override
    public List<User> findAll() {
        return jpa.findAll().stream()
                .map(UserMapper::toDomain)
                .toList();
    }
}
