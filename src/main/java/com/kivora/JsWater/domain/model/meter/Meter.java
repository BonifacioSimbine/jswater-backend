package com.kivora.JsWater.domain.model.meter;

import com.kivora.JsWater.domain.valueobject.client.ClientId;
import com.kivora.JsWater.domain.valueobject.meter.MeterId;
import com.kivora.JsWater.domain.valueobject.meter.MeterNumber;

import java.time.LocalDate;
import java.util.UUID;

public class Meter {

    private final MeterId id;
    private final MeterNumber meterNumber;
    private final ClientId clientId;
    private final LocalDate instalationDate;
    private MeterStatus meterStatus;

    private Meter(MeterId id,
                 MeterNumber meterNumber,
                 ClientId clientId,
                 LocalDate instalationDate) {
        this.id = id;
        this.meterNumber = meterNumber;
        this.clientId = clientId;
        this.instalationDate = instalationDate;
    }

    private Meter(MeterId id,MeterNumber meterNumber,
                  ClientId clientId,
                  LocalDate instalationDate,
                  MeterStatus status
    ) {
        this.id = id;
        this.meterNumber = meterNumber;
        this.clientId = clientId;
        this.instalationDate = instalationDate;
        this.meterStatus = status;
    }

    public static Meter register(MeterNumber meterNumber, ClientId clientId, LocalDate installationDate) {
        // Ao registar um novo contador, o estado inicial é ACTIVE
        // Se a data de instalação não for fornecida, usa a data atual
        LocalDate date = installationDate != null ? installationDate : LocalDate.now();
        return new Meter(MeterId.generate(), meterNumber, clientId, date, MeterStatus.ACTIVE);
    }

    public static Meter restore(
            MeterId id,
            MeterNumber meterNumber,
            ClientId clientId,
            LocalDate installationDate,
            MeterStatus status
    ) {
        return new Meter(
                id,
                meterNumber,
                clientId,
                installationDate,
                status
        );
    }


    public void desactivate() {
        this.meterStatus = MeterStatus.INACTIVE;
    }

    public void activate() {
        this.meterStatus = MeterStatus.ACTIVE;
    }

    public boolean isActive() {
        return meterStatus == MeterStatus.ACTIVE;
    }

    public boolean isInactive() {
        return meterStatus == MeterStatus.INACTIVE;
    }

    public ClientId getClientId() {
        return clientId;
    }

    public MeterId getId() {
        return id;
    }
    public LocalDate getInstalationDate() {
        return instalationDate;
    }

    public MeterStatus getMeterStatus() {
        return meterStatus;
    }

    public MeterNumber getMeterNumber() {
        return meterNumber;
    }

    public  MeterStatus getStatus() {
        return meterStatus;
    }
}
