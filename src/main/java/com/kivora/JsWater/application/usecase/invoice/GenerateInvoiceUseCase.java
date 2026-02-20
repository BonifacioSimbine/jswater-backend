package com.kivora.JsWater.application.usecase.invoice;

import com.kivora.JsWater.domain.exception.client.ClientNotFoundException;
import com.kivora.JsWater.domain.exception.client.ClientInactiveException;
import com.kivora.JsWater.domain.exception.invoice.InvoiceAlreadyExistsException;
import com.kivora.JsWater.domain.exception.meter.MeterNotFoundException;
import com.kivora.JsWater.domain.exception.reading.ReadingAlreadyExistsException;
import com.kivora.JsWater.domain.exception.reading.ReadingAlreadyInvoicedException;
import com.kivora.JsWater.domain.exception.reading.ReadingNotFoundException;
import com.kivora.JsWater.domain.exception.tariff.TariffNotFoundForPeriodException;
import com.kivora.JsWater.domain.model.invoice.Invoice;
import com.kivora.JsWater.domain.model.meter.Meter;
import com.kivora.JsWater.domain.model.reading.Reading;
import com.kivora.JsWater.domain.repository.*;
import com.kivora.JsWater.domain.valueobject.invoice.InvoicePeriod;
import com.kivora.JsWater.domain.valueobject.reading.ReadingId;

import java.time.YearMonth;

public class GenerateInvoiceUseCase {

    private final ReadingRepository readingRepository;
    private final MeterRepository meterRepository;
    private final ClientRepository clientRepository;
    private final TariffRepository tariffRepository;
    private final InvoiceRepository invoiceRepository;

    public GenerateInvoiceUseCase(
            ReadingRepository readingRepository,
            MeterRepository meterRepository,
            ClientRepository clientRepository,
            TariffRepository tariffRepository,
            InvoiceRepository invoiceRepository
    ) {
        this.readingRepository = readingRepository;
        this.meterRepository = meterRepository;
        this.clientRepository = clientRepository;
        this.tariffRepository = tariffRepository;
        this.invoiceRepository = invoiceRepository;
    }

    public Invoice execute(ReadingId readingId) {

        Reading reading = readingRepository.findById(readingId)
                .orElseThrow(() -> new ReadingNotFoundException(readingId));

        if (reading.isInvoiced()) {
            throw new ReadingAlreadyInvoicedException(readingId);
        }

        if (invoiceRepository.existsByReadingId(readingId)) {
            throw new InvoiceAlreadyExistsException(readingId);
        }

        Meter meter = meterRepository.findById(reading.getMeterId())
                .orElseThrow(() -> new MeterNotFoundException(reading.getMeterId()));

        var client = clientRepository.findById(meter.getClientId())
                .orElseThrow(() -> new ClientNotFoundException(meter.getClientId()));

        if (!client.isActive()) {
            throw new ClientInactiveException(client.getId());
        }

        var tariff = tariffRepository.findActiveFor(
                InvoicePeriod.from(reading.getReadingDate().getValue())
        ).orElseThrow(() ->
                new TariffNotFoundForPeriodException(
                        InvoicePeriod.from(reading.getReadingDate().getValue())
                )
        );

        var totalAmount = tariff.calculate(reading.getConsumption());

        Invoice invoice = Invoice.create(
                client.getId(),
                meter.getId(),
                reading.getId(),
                InvoicePeriod.from(reading.getReadingDate().getValue()),
                reading.getConsumption(),
                totalAmount
        );

        invoiceRepository.save(invoice);

        reading.markAsInvoiced();
        readingRepository.save(reading);

        return invoice;
    }
}
