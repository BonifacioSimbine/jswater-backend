package com.kivora.JsWater.application.usecase.invoice;

import com.kivora.JsWater.domain.exception.invoice.InvoiceNotFoundException;
import com.kivora.JsWater.domain.model.invoice.Invoice;
import com.kivora.JsWater.domain.repository.InvoiceRepository;
import com.kivora.JsWater.domain.valueobject.invoice.InvoiceId;

public class GetInvoiceByIdUseCase {

    private final InvoiceRepository invoiceRepository;

    public GetInvoiceByIdUseCase(InvoiceRepository invoiceRepository) {
        this.invoiceRepository = invoiceRepository;
    }

    public Invoice execute(String invoiceId) {
        InvoiceId id = InvoiceId.from(invoiceId);
        return invoiceRepository.findById(id)
                .orElseThrow(() -> new InvoiceNotFoundException(id));
    }
}
