package com.kivora.JsWater.application.usecase.meter;

import com.kivora.JsWater.domain.model.meter.Meter;
import com.kivora.JsWater.domain.repository.MeterRepository;

import java.util.List;

public class ListMetersUseCase {

    private final MeterRepository meterRepository;

    public ListMetersUseCase(MeterRepository meterRepository) {
        this.meterRepository = meterRepository;
    }

    public List<Meter> execute() {
        return meterRepository.findAll();
    }
}
