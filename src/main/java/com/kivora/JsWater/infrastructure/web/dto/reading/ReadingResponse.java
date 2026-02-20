package com.kivora.JsWater.infrastructure.web.dto.reading;

import com.kivora.JsWater.domain.model.reading.Reading;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

public record ReadingResponse(
        UUID id,
        UUID meterId,
        BigDecimal consumption,
        LocalDate readingDate
) {
    public static ReadingResponse from(Reading reading) {
        return new ReadingResponse(
                reading.getId().value(),
                reading.getMeterId().getValue(),
                reading.getConsumption().getMetrodCubicos(),
                reading.getReadingDate().getValue()
        );
    }
}
