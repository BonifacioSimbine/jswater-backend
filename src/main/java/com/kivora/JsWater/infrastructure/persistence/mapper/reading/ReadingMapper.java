package com.kivora.JsWater.infrastructure.persistence.mapper.reading;

import com.kivora.JsWater.domain.model.reading.Reading;
import com.kivora.JsWater.domain.model.reading.ReadingStatus;
import com.kivora.JsWater.domain.valueobject.meter.MeterId;
import com.kivora.JsWater.domain.valueobject.reading.ReadingDate;
import com.kivora.JsWater.domain.valueobject.reading.ReadingId;
import com.kivora.JsWater.domain.valueobject.reading.ReadingValue;
import com.kivora.JsWater.infrastructure.persistence.entity.reading.ReadingJpaEntity;

public class ReadingMapper {

    public static ReadingJpaEntity toJpa(Reading reading) {
        ReadingJpaEntity entity = new ReadingJpaEntity();

        entity.setId(reading.getId().value());
        entity.setMeterId(reading.getMeterId().value());
        entity.setPreviousReading(reading.getPrevious().getValue());
        entity.setCurrentReading(reading.getCurrent().getValue());
        entity.setConsumption(reading.getConsumption().getMetrodCubicos());
        entity.setReadingDate(reading.getReadingDate().getValue());
        entity.setInvoiced(reading.isInvoiced());

        return entity;
    }

    public static Reading toDomain(ReadingJpaEntity entity) {

        ReadingStatus status = entity.isInvoiced()
                ? ReadingStatus.INVOICED
                : ReadingStatus.PENDING;

        return Reading.restore(
                new ReadingId(entity.getId()),
                new MeterId(entity.getMeterId()),
                new ReadingValue(entity.getPreviousReading()),
                new ReadingValue(entity.getCurrentReading()),
                ReadingDate.of(entity.getReadingDate()),
                status
        );
    }
}
