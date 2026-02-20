package com.kivora.JsWater.domain.exception.tariff;

import com.kivora.JsWater.domain.exception.DomainException;

public class TariffAlreadyInactiveException extends DomainException {
    public TariffAlreadyInactiveException() {
        super("A tarifa já está inativa.");;
    }
}
