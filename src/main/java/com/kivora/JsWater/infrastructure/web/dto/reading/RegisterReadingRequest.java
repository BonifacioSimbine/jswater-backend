package com.kivora.JsWater.infrastructure.web.dto.reading;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;
import java.util.UUID;

public record RegisterReadingRequest(
        @NotNull UUID meterId,
        @Positive BigDecimal currentReading
) {}
