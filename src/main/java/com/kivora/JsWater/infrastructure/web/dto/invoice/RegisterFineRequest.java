package com.kivora.JsWater.infrastructure.web.dto.invoice;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;
import java.util.UUID;

public record RegisterFineRequest(
        @NotNull UUID clientId,
        @NotNull @Positive BigDecimal amount,
        String period
) {}
