package com.kivora.JsWater.application.usecase.user;

import com.kivora.JsWater.domain.exception.AuthenticationException;
import com.kivora.JsWater.domain.model.user.User;
import com.kivora.JsWater.domain.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;

public class AdminResetUserPasswordUseCase {

    private final UserRepository repo;
    private final PasswordEncoder encoder;

    public AdminResetUserPasswordUseCase(UserRepository repo, PasswordEncoder encoder) {
        this.repo = repo;
        this.encoder = encoder;
    }

    public void execute(String username, String newPassword) {
        User user = repo.findByUsername(username)
                .orElseThrow(() -> new AuthenticationException("Utilizador não encontrado"));

        String hashed = encoder.encode(newPassword);
        user.changePassword(hashed);
        repo.save(user);
    }
}
