package com.kivora.JsWater.infrastructure.persistence.entity.meter;

import com.kivora.JsWater.domain.model.meter.MeterStatus;
import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(
        name = "meters",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = "meter_number")
        }
)
public class MeterJpaEntity {

    @Id
    @Column(nullable = false, updatable = false)
    private UUID id;

    @Column(name = "meter_number", nullable = false, unique = true)
    private String meterNumber;

    @Column(name = "client_id", nullable = false)
    private UUID clientId;

    @Column(name = "installation_date", nullable = false)
    private LocalDate installationDate;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private MeterStatus status;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getMeterNumber() {
        return meterNumber;
    }

    public void setMeterNumber(String meterNumber) {
        this.meterNumber = meterNumber;
    }

    public UUID getClientId() {
        return clientId;
    }

    public void setClientId(UUID clientId) {
        this.clientId = clientId;
    }

    public LocalDate getInstallationDate() {
        return installationDate;
    }

    public void setInstallationDate(LocalDate installationDate) {
        this.installationDate = installationDate;
    }

    public MeterStatus getStatus() {
        return status;
    }

    public void setStatus(MeterStatus status) {
        this.status = status;
    }


}
