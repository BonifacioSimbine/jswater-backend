package com.kivora.JsWater.infrastructure.persistence.mapper.meter;

import com.kivora.JsWater.domain.model.meter.Meter;
import com.kivora.JsWater.domain.valueobject.client.ClientId;
import com.kivora.JsWater.domain.valueobject.meter.MeterId;
import com.kivora.JsWater.domain.valueobject.meter.MeterNumber;
import com.kivora.JsWater.infrastructure.persistence.entity.meter.MeterJpaEntity;

public class MeterMapper {

    public static MeterJpaEntity toJpa(Meter meter) {
        MeterJpaEntity entity = new MeterJpaEntity();

        entity.setId(meter.getId().value());
        entity.setMeterNumber(meter.getMeterNumber().getValue());
        entity.setClientId(meter.getClientId().value());
        entity.setInstallationDate(meter.getInstalationDate());
        entity.setStatus(meter.getStatus());

        return entity;
    }

    public static Meter toDomain(MeterJpaEntity entity) {
        return Meter.restore(
                new MeterId(entity.getId()),
                new MeterNumber(entity.getMeterNumber()),
                new ClientId(entity.getClientId()),
                entity.getInstallationDate(),
                entity.getStatus()
        );
    }
}
