package com.kivora.JsWater.application.usecase.meter;

import com.kivora.JsWater.domain.exception.meter.MeterNotFoundException;
import com.kivora.JsWater.domain.model.meter.Meter;
import com.kivora.JsWater.domain.repository.MeterRepository;
import com.kivora.JsWater.domain.valueobject.meter.MeterId;

import java.util.UUID;

public class GetMeterByIdUseCase {

    private final MeterRepository meterRepository;

    public GetMeterByIdUseCase(MeterRepository meterRepository) {
        this.meterRepository = meterRepository;
    }

    public Meter execute(String meterId) {
        MeterId id = new MeterId(UUID.fromString(meterId));
        return meterRepository.findById(id)
                .orElseThrow(() -> new MeterNotFoundException(id));
    }
}
