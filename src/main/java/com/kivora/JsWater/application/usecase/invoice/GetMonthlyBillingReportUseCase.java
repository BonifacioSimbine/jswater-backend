package com.kivora.JsWater.application.usecase.invoice;

import com.kivora.JsWater.domain.model.invoice.Invoice;
import com.kivora.JsWater.domain.model.invoice.InvoiceStatus;
import com.kivora.JsWater.domain.repository.InvoiceRepository;
import com.kivora.JsWater.domain.valueobject.invoice.InvoicePeriod;

import java.math.BigDecimal;
import java.time.YearMonth;
import java.util.List;

public class GetMonthlyBillingReportUseCase {

    private final InvoiceRepository invoiceRepository;

    public GetMonthlyBillingReportUseCase(InvoiceRepository invoiceRepository) {
        this.invoiceRepository = invoiceRepository;
    }

    public Result execute(YearMonth month) {
                InvoicePeriod period = InvoicePeriod.of(month);

        List<Invoice> invoices = invoiceRepository.findAll().stream()
                .filter(i -> i.getPeriod().equals(period))
                .toList();

        long issuedCount = invoices.size();
        long paidCount = invoices.stream()
                .filter(i -> i.getStatus() == InvoiceStatus.PAID)
                .count();
        long openCount = invoices.stream()
                .filter(i -> i.getStatus() == InvoiceStatus.OPEN)
                .count();

        BigDecimal totalIssued = invoices.stream()
                .map(i -> i.getTotalAmount().getValue())
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal totalPaid = invoices.stream()
                .filter(i -> i.getStatus() == InvoiceStatus.PAID)
                .map(i -> i.getTotalAmount().getValue())
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal totalOpen = invoices.stream()
                .filter(i -> i.getStatus() == InvoiceStatus.OPEN)
                .map(i -> i.getTotalAmount().getValue())
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return new Result(period, issuedCount, paidCount, openCount, totalIssued, totalPaid, totalOpen);
    }

    public record Result(
            InvoicePeriod period,
            long issuedCount,
            long paidCount,
            long openCount,
            BigDecimal totalIssued,
            BigDecimal totalPaid,
            BigDecimal totalOpen
    ) {
    }
}
