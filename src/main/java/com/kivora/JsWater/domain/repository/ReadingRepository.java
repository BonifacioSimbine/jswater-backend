package com.kivora.JsWater.domain.repository;

import com.kivora.JsWater.domain.model.reading.Reading;
import com.kivora.JsWater.domain.valueobject.invoice.InvoicePeriod;
import com.kivora.JsWater.domain.valueobject.meter.MeterId;
import com.kivora.JsWater.domain.valueobject.reading.ReadingId;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ReadingRepository {

    void save(Reading reading);

    Optional<Reading> findById(ReadingId readingId);

    Optional<Reading> findLastByMeterId(MeterId meterId);

    List<Reading> findByMeterId(MeterId meterId);

    List<Reading> findAll();

    boolean existsForPeriod(MeterId meterId, InvoicePeriod period);

    void delete(ReadingId readingId);

    boolean existsById(ReadingId readingId);
}

