package com.kivora.JsWater.infrastructure.web.dto.user;

import com.kivora.JsWater.domain.model.user.User;
import com.kivora.JsWater.domain.model.user.UserStatus;

import java.util.UUID;

public record UserAdminResponse(
        UUID id,
        String username,
        String role,
        UserStatus status
) {
    public static UserAdminResponse from(User user) {
        return new UserAdminResponse(
                user.getId(),
                user.getName(),
                user.getRole().name(),
                user.getStatus()
        );
    }
}
