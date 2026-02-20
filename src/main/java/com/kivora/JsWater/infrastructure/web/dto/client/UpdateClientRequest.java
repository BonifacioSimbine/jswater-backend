package com.kivora.JsWater.infrastructure.web.dto.client;

import jakarta.validation.constraints.NotBlank;

public record UpdateClientRequest(
        @NotBlank String fullName,
        @NotBlank String phoneNumber
) {}
