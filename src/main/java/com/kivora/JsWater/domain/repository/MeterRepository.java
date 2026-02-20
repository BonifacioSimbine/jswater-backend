package com.kivora.JsWater.domain.repository;

import com.kivora.JsWater.domain.model.meter.Meter;
import com.kivora.JsWater.domain.valueobject.meter.MeterId;
import com.kivora.JsWater.domain.valueobject.meter.MeterNumber;

import java.util.Optional;
import java.util.UUID;

public interface MeterRepository {

    void save(Meter meter);

    Optional<Meter> findById(MeterId meterId);

    Optional<Meter> findByMeterNumber(MeterNumber meterNumber);

    boolean existsByMeterNumber(MeterNumber meterNumber);

    java.util.List<Meter> findAll();

}
