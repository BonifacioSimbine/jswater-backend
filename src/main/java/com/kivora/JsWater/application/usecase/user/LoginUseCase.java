package com.kivora.JsWater.application.usecase.user;

import com.kivora.JsWater.domain.exception.AuthenticationException;
import com.kivora.JsWater.domain.model.user.User;
import com.kivora.JsWater.domain.repository.UserRepository;
import com.kivora.JsWater.infrastructure.security.jwt.JwtService;
import com.kivora.JsWater.infrastructure.web.dto.user.UserResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class LoginUseCase {

    private final UserRepository repo;
    private final PasswordEncoder encoder;
    private final JwtService jwt;

    private static final Logger logger = LoggerFactory.getLogger(LoginUseCase.class);

    public LoginUseCase(UserRepository repo, PasswordEncoder encoder, JwtService jwt) {
        this.repo = repo;
        this.encoder = encoder;
        this.jwt = jwt;
    }

    public UserResponse execute(String username, String password) {

        User user = repo.findByUsername(username)
            .orElseThrow(() -> new AuthenticationException("Credenciais inválidas"));

        if (!encoder.matches(password, user.getPassword())) {
            logger.warn("Falha de login para utilizador: {}", username);
            throw new AuthenticationException("Credenciais inválidas");
        }

        logger.info("Login bem-sucedido para utilizador: {}", username);

        return new UserResponse(
                jwt.generateToken(user),
                user.getName(),
                user.getRole().name()
        );
    }
}

