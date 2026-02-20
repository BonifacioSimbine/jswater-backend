package com.kivora.JsWater.infrastructure.web.dto.tariff;

import jakarta.validation.constraints.NotNull;

import java.time.YearMonth;

public record DeactivateTariffRequest(
        @NotNull YearMonth endPeriod
) {
}
