package com.kivora.JsWater.domain.exception.tariff;

import com.kivora.JsWater.domain.exception.DomainException;

public class InvalidTariffPeriodException extends DomainException{
    public InvalidTariffPeriodException(String message) {
        super(message);
    }
}
