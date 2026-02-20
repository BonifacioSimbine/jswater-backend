package com.kivora.JsWater.infrastructure.web.dto.tariff;

import com.kivora.JsWater.domain.model.tariff.Tariff;
import com.kivora.JsWater.domain.model.tariff.TariffStatus;

import java.math.BigDecimal;
import java.time.YearMonth;
import java.util.UUID;

public record TariffResponse(
        UUID id,
        YearMonth validFrom,
        YearMonth validTo,
        BigDecimal pricePerCubicMeter,
        TariffStatus status,
        int minimumConsumption
) {
    public static TariffResponse from(Tariff tariff) {
        return new TariffResponse(
                tariff.getId().value(),
                tariff.getValidFrom().value(),
                tariff.getValidTo() != null ? tariff.getValidTo().value() : null,
                tariff.pricePerCubicMeter().getValue(),
                tariff.getStatus(),
                tariff.getMinimumConsumption()
        );
    }
}
