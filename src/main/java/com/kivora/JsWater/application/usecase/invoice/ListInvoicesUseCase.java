package com.kivora.JsWater.application.usecase.invoice;

import com.kivora.JsWater.domain.model.invoice.Invoice;
import com.kivora.JsWater.domain.repository.InvoiceRepository;

import java.util.List;

public class ListInvoicesUseCase {
    private final InvoiceRepository invoiceRepository;

    public ListInvoicesUseCase(InvoiceRepository invoiceRepository) {
        this.invoiceRepository = invoiceRepository;
    }

    public List<Invoice> execute() {
        return invoiceRepository.findAll();
    }
}
