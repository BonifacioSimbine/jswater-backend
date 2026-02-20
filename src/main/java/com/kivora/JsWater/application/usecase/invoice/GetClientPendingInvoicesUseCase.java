package com.kivora.JsWater.application.usecase.invoice;

import com.kivora.JsWater.domain.model.invoice.Invoice;
import com.kivora.JsWater.domain.model.invoice.InvoiceStatus;
import com.kivora.JsWater.domain.repository.InvoiceRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public class GetClientPendingInvoicesUseCase {

    private final InvoiceRepository invoiceRepository;

    public GetClientPendingInvoicesUseCase(InvoiceRepository invoiceRepository) {
        this.invoiceRepository = invoiceRepository;
    }

    public List<Invoice> execute(UUID clientId, boolean overdueOnly) {
        List<Invoice> all = invoiceRepository.findByClientId(clientId);
        LocalDate today = LocalDate.now();

        return all.stream()
                .filter(invoice -> invoice.getStatus() == InvoiceStatus.OPEN)
                .filter(invoice -> !overdueOnly || invoice.isOverdue(today))
                .toList();
    }
}
