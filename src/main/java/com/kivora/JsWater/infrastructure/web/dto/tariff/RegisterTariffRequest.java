package com.kivora.JsWater.infrastructure.web.dto.tariff;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;
import java.time.YearMonth;

public record RegisterTariffRequest(
        @NotNull YearMonth validFrom,
        @NotNull @Positive BigDecimal pricePerCubicMeter
) {
}
