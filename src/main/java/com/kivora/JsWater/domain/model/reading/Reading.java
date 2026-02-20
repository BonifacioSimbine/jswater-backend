package com.kivora.JsWater.domain.model.reading;

import com.kivora.JsWater.domain.valueobject.consumption.Consumption;
import com.kivora.JsWater.domain.valueobject.meter.MeterId;
import com.kivora.JsWater.domain.valueobject.reading.ReadingDate;
import com.kivora.JsWater.domain.valueobject.reading.ReadingId;
import com.kivora.JsWater.domain.valueobject.reading.ReadingValue;

public class Reading {

    private final ReadingId id;
    private final MeterId meterId;
    private final ReadingValue previous;
    private final ReadingValue current;
    private final Consumption consumption;
    private final ReadingDate readingDate;
    private ReadingStatus readingStatus;

    private Reading(
            ReadingId id,
            MeterId meterId,
            ReadingValue previous,
            ReadingValue current,
            ReadingDate readingDate,
            ReadingStatus readingStatus
    ) {
        if (current.getValue().compareTo(previous.getValue()) <= 0) {
            throw new IllegalStateException(
                    "A leitura actual deve ser maior que a anterior"
            );
        }

        this.id = id;
        this.meterId = meterId;
        this.previous = previous;
        this.current = current;
        this.consumption = new Consumption(
                current.getValue().subtract(previous.getValue())
        );
        this.readingDate = readingDate;
        this.readingStatus = readingStatus;
    }

    /* ========= FACTORIES ========= */

    public static Reading register(
            MeterId meterId,
            ReadingValue previous,
            ReadingValue current
    ) {
        return new Reading(
                ReadingId.generate(),
                meterId,
                previous,
                current,
                ReadingDate.now(),
                ReadingStatus.PENDING
        );
    }

    public static Reading restore(
            ReadingId id,
            MeterId meterId,
            ReadingValue previous,
            ReadingValue current,
            ReadingDate readingDate,
            ReadingStatus status
    ) {
        return new Reading(
                id,
                meterId,
                previous,
                current,
                readingDate,
                status
        );
    }

    /* ========= COMPORTAMENTOS ========= */

    public void markAsInvoiced() {
        this.readingStatus = ReadingStatus.INVOICED;
    }

    public boolean isInvoiced() {
        return this.readingStatus == ReadingStatus.INVOICED;
    }

    /* ========= GETTERS ========= */

    public ReadingId getId() {
        return id;
    }

    public MeterId getMeterId() {
        return meterId;
    }

    public ReadingValue getPrevious() {
        return previous;
    }

    public ReadingValue getCurrent() {
        return current;
    }

    public ReadingDate getReadingDate() {
        return readingDate;
    }

    public ReadingStatus getReadingStatus() {
        return readingStatus;
    }

    public Consumption getConsumption() {
        return consumption;
    }
}
