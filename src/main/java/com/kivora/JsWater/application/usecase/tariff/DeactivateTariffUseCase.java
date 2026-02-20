package com.kivora.JsWater.application.usecase.tariff;

import com.kivora.JsWater.domain.exception.tariff.TariffNotFoundException;
import com.kivora.JsWater.domain.model.tariff.Tariff;
import com.kivora.JsWater.domain.repository.TariffRepository;
import com.kivora.JsWater.domain.valueobject.invoice.InvoicePeriod;

import java.time.YearMonth;
import java.util.UUID;

public class DeactivateTariffUseCase {

    private final TariffRepository tariffRepository;

    public DeactivateTariffUseCase(TariffRepository tariffRepository) {
        this.tariffRepository = tariffRepository;
    }

    public Tariff execute(UUID tariffId, YearMonth endPeriod) {
        Tariff tariff = tariffRepository.findById(tariffId)
                .orElseThrow(() -> new TariffNotFoundException(tariffId));

        InvoicePeriod end = InvoicePeriod.of(endPeriod);
        tariff.desactivate(end);

        tariffRepository.save(tariff);
        return tariff;
    }
}
