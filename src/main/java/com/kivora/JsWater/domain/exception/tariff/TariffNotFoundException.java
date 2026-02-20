package com.kivora.JsWater.domain.exception.tariff;

import com.kivora.JsWater.domain.exception.DomainException;

import java.util.UUID;

public class TariffNotFoundException extends DomainException {

    public TariffNotFoundException(UUID id) {
        super("Tarifa não encontrada: " + id);
    }
}
