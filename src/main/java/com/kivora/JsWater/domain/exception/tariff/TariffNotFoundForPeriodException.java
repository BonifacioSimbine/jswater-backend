package com.kivora.JsWater.domain.exception.tariff;

import com.kivora.JsWater.domain.exception.DomainException;
import com.kivora.JsWater.domain.valueobject.invoice.InvoicePeriod;

public class TariffNotFoundForPeriodException extends DomainException {
    public TariffNotFoundForPeriodException(InvoicePeriod invoicePeriod) {
        super("Nenhuma Tariffa activa encontrada neste periodo" + invoicePeriod);
    }
}
