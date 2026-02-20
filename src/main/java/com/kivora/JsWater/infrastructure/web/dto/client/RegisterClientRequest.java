package com.kivora.JsWater.infrastructure.web.dto.client;

import com.kivora.JsWater.domain.model.client.DocumentType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record RegisterClientRequest(
        @NotBlank String fullName,
        @NotNull DocumentType documentType,
        @NotBlank String documentNumber,
        @NotBlank String phoneNumber,
        @NotBlank String bairro,
        @NotBlank String localidade,
        @NotBlank String rua,
        String referencia
) {}
