package com.kivora.JsWater.application.usecase.tariff;

import com.kivora.JsWater.domain.model.tariff.Tariff;
import com.kivora.JsWater.domain.repository.TariffRepository;
import com.kivora.JsWater.domain.valueobject.invoice.InvoicePeriod;
import com.kivora.JsWater.domain.valueobject.money.Money;

import java.math.BigDecimal;
import java.time.YearMonth;

public class RegisterTariffUseCase {

    private final TariffRepository tariffRepository;

    public RegisterTariffUseCase(TariffRepository tariffRepository) {
        this.tariffRepository = tariffRepository;
    }

    public Tariff execute(YearMonth validFrom, BigDecimal pricePerCubicMeter) {
        InvoicePeriod newPeriod = InvoicePeriod.of(validFrom);
        Money price = new Money(pricePerCubicMeter);

        tariffRepository.getActiveTariff().ifPresent(active -> {
            YearMonth activeFrom = active.getValidFrom().value();

            YearMonth endYearMonth = validFrom.isAfter(activeFrom)
                    ? validFrom.minusMonths(1)
                    : activeFrom;

            InvoicePeriod endPeriod = InvoicePeriod.of(endYearMonth);
            active.desactivate(endPeriod);
            tariffRepository.save(active);
        });

        Tariff tariff = Tariff.create(newPeriod, price);

        tariffRepository.save(tariff);

        return tariff;
    }
}
