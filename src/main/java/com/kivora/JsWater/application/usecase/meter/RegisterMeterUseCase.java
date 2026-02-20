package com.kivora.JsWater.application.usecase.meter;

import com.kivora.JsWater.domain.exception.client.ClientNotFoundException;
import com.kivora.JsWater.domain.exception.meter.MeterAlreadyRegisteredException;
import com.kivora.JsWater.domain.model.meter.Meter;
import com.kivora.JsWater.domain.repository.ClientRepository;
import com.kivora.JsWater.domain.repository.MeterRepository;
import com.kivora.JsWater.domain.valueobject.client.ClientId;
import com.kivora.JsWater.domain.valueobject.meter.MeterNumber;

import java.time.LocalDate;

public class RegisterMeterUseCase {

    private final MeterRepository meterRepository;
    private final ClientRepository clientRepository;

    public RegisterMeterUseCase(MeterRepository meterRepository, ClientRepository clientRepository) {
        this.meterRepository = meterRepository;
        this.clientRepository = clientRepository;
    }

    public Meter execute(String meterNumber, ClientId clientId, LocalDate installationDate) {
        if (!clientRepository.existsById(clientId)) {
            throw new ClientNotFoundException(clientId);
        }

        MeterNumber number = new MeterNumber(meterNumber);

        if (meterRepository.existsByMeterNumber(number)) {
            throw new MeterAlreadyRegisteredException(meterNumber);
        }

        Meter meter = Meter.register(
                number,
                clientId,
                installationDate
        );

        meterRepository.save(meter);

        return meter;
    }
}
