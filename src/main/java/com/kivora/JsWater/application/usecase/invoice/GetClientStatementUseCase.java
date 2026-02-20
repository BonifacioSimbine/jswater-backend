package com.kivora.JsWater.application.usecase.invoice;

import com.kivora.JsWater.domain.model.invoice.Invoice;
import com.kivora.JsWater.domain.repository.InvoiceRepository;

import java.time.YearMonth;
import java.util.List;
import java.util.UUID;

public class GetClientStatementUseCase {

    private final InvoiceRepository invoiceRepository;

    public GetClientStatementUseCase(InvoiceRepository invoiceRepository) {
        this.invoiceRepository = invoiceRepository;
    }

    public List<Invoice> execute(UUID clientId, YearMonth from, YearMonth to) {
        List<Invoice> all = invoiceRepository.findByClientId(clientId);

        if (from == null && to == null) {
            return all;
        }

        return all.stream()
                .filter(invoice -> {
                    YearMonth period = invoice.getPeriod().value();
                    boolean afterFrom = from == null || !period.isBefore(from);
                    boolean beforeTo = to == null || !period.isAfter(to);
                    return afterFrom && beforeTo;
                })
                .toList();
    }
}
