package com.kivora.JsWater.application.usecase.invoice;

import com.kivora.JsWater.domain.model.invoice.Invoice;
import com.kivora.JsWater.domain.model.invoice.InvoiceStatus;
import com.kivora.JsWater.domain.repository.InvoiceRepository;
import com.kivora.JsWater.domain.valueobject.invoice.InvoicePeriod;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;
import java.util.UUID;

public class GetClientDebtSummaryUseCase {

    private final InvoiceRepository invoiceRepository;

    public GetClientDebtSummaryUseCase(InvoiceRepository invoiceRepository) {
        this.invoiceRepository = invoiceRepository;
    }

    public Result execute(UUID clientId) {
        List<Invoice> invoices = invoiceRepository.findByClientId(clientId);

        LocalDate today = LocalDate.now();
        YearMonth currentPeriod = InvoicePeriod.current().value();

        BigDecimal totalOutstanding = BigDecimal.ZERO;
        BigDecimal currentMonthAmount = BigDecimal.ZERO;
        BigDecimal overdueAmount = BigDecimal.ZERO;

        for (Invoice invoice : invoices) {
            if (invoice.getStatus() != InvoiceStatus.OPEN) {
                continue;
            }

            BigDecimal amount = invoice.getTotalAmount().getValue();
            totalOutstanding = totalOutstanding.add(amount);

            YearMonth period = invoice.getPeriod().value();
            if (period.equals(currentPeriod)) {
                currentMonthAmount = currentMonthAmount.add(amount);
            }

            if (invoice.isOverdue(today)) {
                overdueAmount = overdueAmount.add(amount);
            }
        }

        return new Result(totalOutstanding, currentMonthAmount, overdueAmount);
    }

    public record Result(
            BigDecimal totalOutstanding,
            BigDecimal currentMonthAmount,
            BigDecimal overdueAmount
    ) {
    }
}
