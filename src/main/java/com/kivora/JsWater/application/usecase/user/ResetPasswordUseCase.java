package com.kivora.JsWater.application.usecase.user;

import com.kivora.JsWater.domain.exception.AuthenticationException;
import com.kivora.JsWater.domain.model.user.User;
import com.kivora.JsWater.domain.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class ResetPasswordUseCase {

    private final UserRepository repo;
    private final PasswordEncoder encoder;

    public ResetPasswordUseCase(UserRepository repo, PasswordEncoder encoder) {
        this.repo = repo;
        this.encoder = encoder;
    }

    public void execute(String username, String currentPassword, String newPassword) {
        User user = repo.findByUsername(username)
                .orElseThrow(() -> new AuthenticationException("Utilizador não encontrado"));

        if (!encoder.matches(currentPassword, user.getPassword())) {
            throw new AuthenticationException("Palavra-passe actual inválida");
        }

        String hashed = encoder.encode(newPassword);
        user.changePassword(hashed);
        repo.save(user);
    }
}
