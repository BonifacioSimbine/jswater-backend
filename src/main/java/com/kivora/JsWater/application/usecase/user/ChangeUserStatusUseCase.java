package com.kivora.JsWater.application.usecase.user;

import com.kivora.JsWater.domain.exception.AuthenticationException;
import com.kivora.JsWater.domain.model.user.User;
import com.kivora.JsWater.domain.model.user.UserStatus;
import com.kivora.JsWater.domain.repository.UserRepository;

import java.util.UUID;

public class ChangeUserStatusUseCase {

    private final UserRepository repo;

    public ChangeUserStatusUseCase(UserRepository repo) {
        this.repo = repo;
    }

    public void execute(UUID userId, UserStatus newStatus) {
        User user = repo.findById(userId)
                .orElseThrow(() -> new AuthenticationException("Utilizador não encontrado"));

        user.changeStatus(newStatus);
        repo.save(user);
    }
}
