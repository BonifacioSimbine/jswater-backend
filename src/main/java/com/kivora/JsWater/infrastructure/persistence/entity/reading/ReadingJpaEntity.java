package com.kivora.JsWater.infrastructure.persistence.entity.reading;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(
        name = "readings",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"meter_id", "reading_date"})
        }
)

public class ReadingJpaEntity {

    @Id
    @Column(nullable = false, updatable = false)
    private UUID id;


    @Column(name = "meter_id", nullable = false)
    private UUID meterId;

    @Column(nullable = false)
    private BigDecimal previousReading;

    @Column(nullable = false)
    private BigDecimal currentReading;

    @Column(nullable = false)
    private BigDecimal consumption;

    @Column(name = "reading_date", nullable = false)
    private LocalDate readingDate;

    @Column(nullable = false)
    private boolean invoiced;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getMeterId() {
        return meterId;
    }

    public void setMeterId(UUID meterId) {
        this.meterId = meterId;
    }

    public BigDecimal getPreviousReading() {
        return previousReading;
    }

    public void setPreviousReading(BigDecimal previousReading) {
        this.previousReading = previousReading;
    }

    public BigDecimal getCurrentReading() {
        return currentReading;
    }

    public void setCurrentReading(BigDecimal currentReading) {
        this.currentReading = currentReading;
    }

    public BigDecimal getConsumption() {
        return consumption;
    }

    public void setConsumption(BigDecimal consumption) {
        this.consumption = consumption;
    }

    public LocalDate getReadingDate() {
        return readingDate;
    }

    public void setReadingDate(LocalDate readingDate) {
        this.readingDate = readingDate;
    }

    public boolean isInvoiced() {
        return invoiced;
    }

    public void setInvoiced(boolean invoiced) {
        this.invoiced = invoiced;
    }




}
