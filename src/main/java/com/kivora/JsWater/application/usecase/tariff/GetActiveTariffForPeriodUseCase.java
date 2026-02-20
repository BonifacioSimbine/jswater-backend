package com.kivora.JsWater.application.usecase.tariff;

import com.kivora.JsWater.domain.exception.tariff.TariffNotFoundForPeriodException;
import com.kivora.JsWater.domain.model.tariff.Tariff;
import com.kivora.JsWater.domain.repository.TariffRepository;
import com.kivora.JsWater.domain.valueobject.invoice.InvoicePeriod;

public class GetActiveTariffForPeriodUseCase {

    private final TariffRepository tariffRepository;

    public GetActiveTariffForPeriodUseCase(TariffRepository tariffRepository) {
        this.tariffRepository = tariffRepository;
    }

    public Tariff execute(InvoicePeriod period) {
        return tariffRepository.findActiveFor(period)
                .orElseThrow(() -> new TariffNotFoundForPeriodException(period));
    }
}
