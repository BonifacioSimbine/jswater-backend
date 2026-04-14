package com.kivora.JsWater.application.usecase.reading;

import com.kivora.JsWater.domain.exception.meter.MeterNotFoundException;
import com.kivora.JsWater.domain.exception.client.ClientInactiveException;
import com.kivora.JsWater.domain.repository.ClientRepository;
import com.kivora.JsWater.domain.model.meter.Meter;
import com.kivora.JsWater.domain.model.reading.Reading;
import com.kivora.JsWater.domain.repository.MeterRepository;
import com.kivora.JsWater.domain.repository.ReadingRepository;
import com.kivora.JsWater.domain.valueobject.meter.MeterId;
import com.kivora.JsWater.domain.valueobject.reading.ReadingValue;

import java.time.YearMonth;
import java.time.format.TextStyle;
import java.util.Locale;

public class RegisterReadingUseCase {

    private final MeterRepository meterRepository;
    private final ReadingRepository readingRepository;
    private final ClientRepository clientRepository;

    public RegisterReadingUseCase(MeterRepository meterRepository,
                                  ReadingRepository readingRepository,
                                  ClientRepository clientRepository) {
        this.meterRepository = meterRepository;
        this.readingRepository = readingRepository;
        this.clientRepository = clientRepository;
    }

    public Reading execute(MeterId meterId, ReadingValue currentReading) {
        // Permitir apenas uma leitura por mês
        YearMonth currentMonth = YearMonth.now();
        com.kivora.JsWater.domain.valueobject.invoice.InvoicePeriod currentPeriod = com.kivora.JsWater.domain.valueobject.invoice.InvoicePeriod.of(currentMonth);
        if (readingRepository.existsForPeriod(meterId, currentPeriod)) {
            String monthName = currentMonth.getMonth()
                    .getDisplayName(TextStyle.FULL, new Locale("pt", "PT"));
            throw new com.kivora.JsWater.domain.exception.reading.ReadingAlreadyExistsException(
                "Já existe uma leitura registada para este contador no mês de " + monthName
            );
        }
        
        Meter meter = meterRepository.findById(meterId)
                .orElseThrow(() -> new MeterNotFoundException(meterId));

        if (!meter.isActive()) {
            throw new MeterNotFoundException(meterId);
        }

        var client = clientRepository.findById(meter.getClientId())
            .orElseThrow(() -> new MeterNotFoundException(meterId));

        if (!client.isActive()) {
            throw new ClientInactiveException(client.getId());
        }

        ReadingValue previousReading = readingRepository
                .findLastByMeterId(meterId)
                .map(Reading::getCurrent)
                .orElse(ReadingValue.zero());

        Reading reading = Reading.register(
                meterId,
                previousReading,
                currentReading
        );

        readingRepository.save(reading);

        return reading;
    }
}