package com.kivora.JsWater.infrastructure.web.dto.user;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record ResetPasswordRequest(
        @NotBlank @Size(min = 6, max = 100) String currentPassword,
        @NotBlank @Size(min = 6, max = 100) String newPassword
) {
}
