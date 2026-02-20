package com.kivora.JsWater.application.usecase.user;

import com.kivora.JsWater.domain.model.user.User;
import com.kivora.JsWater.domain.repository.UserRepository;

import java.util.List;

public class ListUsersUseCase {

    private final UserRepository repo;

    public ListUsersUseCase(UserRepository repo) {
        this.repo = repo;
    }

    public List<User> execute() {
        return repo.findAll();
    }
}
