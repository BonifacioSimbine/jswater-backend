package com.kivora.JsWater.infrastructure.persistence.repository.meter;

import com.kivora.JsWater.infrastructure.persistence.entity.meter.MeterJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface MeterJpaRepository extends JpaRepository<MeterJpaEntity, UUID> {

    Optional<MeterJpaEntity> findByMeterNumber(String meterNumber);

    boolean existsByMeterNumber(String meterNumber);
}
