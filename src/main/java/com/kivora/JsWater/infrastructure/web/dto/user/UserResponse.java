package com.kivora.JsWater.infrastructure.web.dto.user;

public record UserResponse(
        String token,
        String username,
        String role
) {


}
