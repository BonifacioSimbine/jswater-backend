package com.kivora.JsWater.domain.exception.tariff;

import com.kivora.JsWater.domain.exception.DomainException;

public class InvalidTariffCalculationException extends DomainException {



    public InvalidTariffCalculationException(String message) {
        super("Invalid tariff calculation: " + message);
    }
}
