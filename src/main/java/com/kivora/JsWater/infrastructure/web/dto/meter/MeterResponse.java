package com.kivora.JsWater.infrastructure.web.dto.meter;

import com.kivora.JsWater.domain.model.meter.Meter;

import java.time.LocalDate;
import java.util.UUID;

public record MeterResponse(
        UUID id,
        String meterNumber,
        UUID clientId,
        String clientName,
        LocalDate installationDate,
        String status
) {
    public static MeterResponse from(Meter meter, String clientName) {
        return new MeterResponse(
                meter.getId().value(),
                meter.getMeterNumber().getValue(),
                meter.getClientId().value(),
                clientName,
                meter.getInstalationDate(),
                meter.getStatus().name()
        );
    }
    
    // Método de compatibilidade (sem nome)
    public static MeterResponse from(Meter meter) {
        return from(meter, "Desconhecido");
    }
}
