package com.kivora.JsWater.infrastructure.web.dto.meter;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.util.UUID;

public record RegisterMeterRequest(
        @NotBlank String meterNumber,
        @NotNull UUID clientId,
        LocalDate installationDate
) {
}
