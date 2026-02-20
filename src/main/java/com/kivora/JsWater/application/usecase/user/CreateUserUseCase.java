package com.kivora.JsWater.application.usecase.user;

import com.kivora.JsWater.domain.model.user.User;
import com.kivora.JsWater.domain.model.user.UserRole;
import com.kivora.JsWater.domain.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CreateUserUseCase {

    private final UserRepository repo;
    private final PasswordEncoder encoder;

    public CreateUserUseCase(UserRepository repo, PasswordEncoder encoder) {
        this.repo = repo;
        this.encoder = encoder;
    }

    public User execute(String name, String password, UserRole role) {

        if (repo.existsByUsername(name))
            throw new RuntimeException("Usuário já existe");

        String hashed = encoder.encode(password);
        User user = User.create(name, hashed, role);

        repo.save(user);
        return user;
    }
}
