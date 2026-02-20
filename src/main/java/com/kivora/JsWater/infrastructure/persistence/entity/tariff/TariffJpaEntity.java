package com.kivora.JsWater.infrastructure.persistence.entity.tariff;

import com.kivora.JsWater.domain.model.tariff.TariffStatus;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "tariffs")
public class TariffJpaEntity {

    @Id
    @Column(nullable = false, updatable = false)
    private UUID id;

    @Column(nullable = false)
    private BigDecimal price;

    @Column(nullable = false)
    private LocalDate validFrom;

    @Column
    private LocalDate validTo;


    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TariffStatus status;

    @Column(nullable = false)
    private int minimumConsumption;
    public int getMinimumConsumption() {
        return minimumConsumption;
    }

    public void setMinimumConsumption(int minimumConsumption) {
        this.minimumConsumption = minimumConsumption;
    }

    /* ===== Getters & Setters ===== */

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public LocalDate getValidFrom() {
        return validFrom;
    }

    public void setValidFrom(LocalDate validFrom) {
        this.validFrom = validFrom;
    }

    public LocalDate getValidTo() {
        return validTo;
    }

    public void setValidTo(LocalDate validTo) {
        this.validTo = validTo;
    }

    public TariffStatus getStatus() {
        return status;
    }

    public void setStatus(TariffStatus status) {
        this.status = status;
    }
}
