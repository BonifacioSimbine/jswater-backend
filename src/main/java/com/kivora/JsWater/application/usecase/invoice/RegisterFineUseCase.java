package com.kivora.JsWater.application.usecase.invoice;

import com.kivora.JsWater.domain.exception.client.ClientInactiveException;
import com.kivora.JsWater.domain.exception.client.ClientNotFoundException;
import com.kivora.JsWater.domain.model.client.Client;
import com.kivora.JsWater.domain.model.invoice.Invoice;
import com.kivora.JsWater.domain.repository.ClientRepository;
import com.kivora.JsWater.domain.repository.InvoiceRepository;
import com.kivora.JsWater.domain.valueobject.client.ClientId;
import com.kivora.JsWater.domain.valueobject.invoice.InvoicePeriod;
import com.kivora.JsWater.domain.valueobject.money.Money;

import java.math.BigDecimal;
import java.time.YearMonth;
import java.util.UUID;

public class RegisterFineUseCase {

    private final ClientRepository clientRepository;
    private final InvoiceRepository invoiceRepository;

    public RegisterFineUseCase(ClientRepository clientRepository, InvoiceRepository invoiceRepository) {
        this.clientRepository = clientRepository;
        this.invoiceRepository = invoiceRepository;
    }

    public Invoice execute(UUID clientId, BigDecimal amount, String periodStr) {
        ClientId id = new ClientId(clientId);
        
        Client client = clientRepository.findById(id)
                .orElseThrow(() -> new ClientNotFoundException(id));

        if (!client.isActive()) {
            throw new ClientInactiveException(id);
        }

        InvoicePeriod period;
        if (periodStr != null && !periodStr.isBlank()) {
            period = InvoicePeriod.of(YearMonth.parse(periodStr));
        } else {
            period = InvoicePeriod.current();
        }

        Invoice fine = Invoice.createFine(
                id,
                new Money(amount),
                period
        );

        invoiceRepository.save(fine);
        return fine;
    }
}
