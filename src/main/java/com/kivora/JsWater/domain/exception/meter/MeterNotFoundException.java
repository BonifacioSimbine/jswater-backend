package com.kivora.JsWater.domain.exception.meter;

import com.kivora.JsWater.domain.exception.DomainException;
import com.kivora.JsWater.domain.valueobject.meter.MeterId;

import java.util.UUID;

public class MeterNotFoundException extends DomainException {
    public MeterNotFoundException(MeterId meterId) {
        super("Contador nao encontrado" + meterId);
    }

    public MeterNotFoundException(String meterId) {
        super("Contador nao encontrado" + meterId);
    }
}
