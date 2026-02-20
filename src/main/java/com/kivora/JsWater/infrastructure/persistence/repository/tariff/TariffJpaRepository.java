package com.kivora.JsWater.infrastructure.persistence.repository.tariff;

import com.kivora.JsWater.infrastructure.persistence.entity.tariff.TariffJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface TariffJpaRepository extends JpaRepository<TariffJpaEntity, UUID> {

}
