package com.kivora.JsWater.domain.exception.meter;

import com.kivora.JsWater.domain.exception.DomainException;

public class MeterAlreadyRegisteredException extends DomainException {
    public MeterAlreadyRegisteredException(String meterNumbeer) {

        super("Contador ja em uso" + meterNumbeer);
    }
}
