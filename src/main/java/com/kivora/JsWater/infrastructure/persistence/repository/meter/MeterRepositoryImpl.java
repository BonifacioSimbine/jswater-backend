package com.kivora.JsWater.infrastructure.persistence.repository.meter;

import com.kivora.JsWater.domain.model.meter.Meter;
import com.kivora.JsWater.domain.repository.MeterRepository;
import com.kivora.JsWater.domain.valueobject.meter.MeterId;
import com.kivora.JsWater.domain.valueobject.meter.MeterNumber;
import com.kivora.JsWater.infrastructure.persistence.mapper.meter.MeterMapper;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class MeterRepositoryImpl implements MeterRepository {

    private final MeterJpaRepository jpaRepository;

    public MeterRepositoryImpl(MeterJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public void save(Meter meter) {
        jpaRepository.save(MeterMapper.toJpa(meter));
    }

    @Override
    public Optional<Meter> findById(MeterId meterId) {
        return jpaRepository.findById(meterId.value()) // MeterId -> UUID
                .map(MeterMapper::toDomain);
    }

    @Override
    public Optional<Meter> findByMeterNumber(MeterNumber meterNumber) {
        return jpaRepository.findByMeterNumber(meterNumber.getValue()) // MeterNumber -> String
                .map(MeterMapper::toDomain);
    }

    @Override
    public boolean existsByMeterNumber(MeterNumber meterNumber) {
        return jpaRepository.existsByMeterNumber(meterNumber.getValue());
    }

    @Override
    public java.util.List<Meter> findAll() {
        return jpaRepository.findAll().stream()
                .map(MeterMapper::toDomain)
                .toList();
    }
}
