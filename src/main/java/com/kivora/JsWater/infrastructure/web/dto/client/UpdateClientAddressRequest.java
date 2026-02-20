package com.kivora.JsWater.infrastructure.web.dto.client;

import jakarta.validation.constraints.NotBlank;

public record UpdateClientAddressRequest(
        @NotBlank String bairro,
        @NotBlank String localidade,
        @NotBlank String rua,
        String referencia
) {
}
