package com.kivora.JsWater.application.usecase.invoice;

import com.kivora.JsWater.domain.model.client.Client;
import com.kivora.JsWater.domain.model.invoice.Invoice;
import com.kivora.JsWater.domain.model.invoice.InvoiceStatus;
import com.kivora.JsWater.domain.repository.ClientRepository;
import com.kivora.JsWater.domain.repository.InvoiceRepository;
import com.kivora.JsWater.domain.repository.MeterRepository;

import java.math.BigDecimal;
import java.time.YearMonth;
import java.util.List;

public class GetDashboardOverviewUseCase {

    private final GetMonthlyBillingReportUseCase getMonthlyBillingReportUseCase;
    private final GetTopDebtorsReportUseCase getTopDebtorsReportUseCase;
    private final GetZoneDebtReportUseCase getZoneDebtReportUseCase;
    private final ClientRepository clientRepository;
    private final InvoiceRepository invoiceRepository;
    private final MeterRepository meterRepository;

    public GetDashboardOverviewUseCase(
            GetMonthlyBillingReportUseCase getMonthlyBillingReportUseCase,
            GetTopDebtorsReportUseCase getTopDebtorsReportUseCase,
            GetZoneDebtReportUseCase getZoneDebtReportUseCase,
            ClientRepository clientRepository,
            InvoiceRepository invoiceRepository,
            MeterRepository meterRepository
    ) {
        this.getMonthlyBillingReportUseCase = getMonthlyBillingReportUseCase;
        this.getTopDebtorsReportUseCase = getTopDebtorsReportUseCase;
        this.getZoneDebtReportUseCase = getZoneDebtReportUseCase;
        this.clientRepository = clientRepository;
        this.invoiceRepository = invoiceRepository;
        this.meterRepository = meterRepository;
    }

    public Result execute(YearMonth month, int topLimit) {
        if (topLimit <= 0) {
            topLimit = 10;
        }

        GetMonthlyBillingReportUseCase.Result monthly = getMonthlyBillingReportUseCase.execute(month);

        List<GetTopDebtorsReportUseCase.ResultItem> topDebtors = getTopDebtorsReportUseCase.execute(topLimit);

        List<GetZoneDebtReportUseCase.ResultItem> zoneDebts = getZoneDebtReportUseCase.execute(month);

        List<Client> clients = clientRepository.findAll();
        long totalClients = clients.size();
        long activeClients = clients.stream().filter(Client::isActive).count();
        long inactiveClients = totalClients - activeClients;

        List<Invoice> invoices = invoiceRepository.findAll();
        long totalInvoices = invoices.size();
        long openInvoices = invoices.stream().filter(i -> i.getStatus() == InvoiceStatus.OPEN).count();
        long paidInvoices = invoices.stream().filter(i -> i.getStatus() == InvoiceStatus.PAID).count();

        BigDecimal totalOpenDebtOverall = invoices.stream()
                .filter(i -> i.getStatus() == InvoiceStatus.OPEN)
                .map(i -> i.getTotalAmount().getValue())
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        long totalMeters = meterRepository.findAll().size();

        long pendingInvoices = invoices.stream()
            .filter(i -> i.getPeriod().value().equals(month))
            .filter(i -> i.getStatus() == InvoiceStatus.OPEN)
            .count();

        BigDecimal collectedRevenue = monthly.totalPaid();

        return new Result(
                month,
                monthly,
                topDebtors,
                zoneDebts,
                totalClients,
                activeClients,
                inactiveClients,
                totalInvoices,
                openInvoices,
                paidInvoices,
            totalOpenDebtOverall,
            totalMeters,
            pendingInvoices,
            collectedRevenue
        );
    }

    public record Result(
            YearMonth month,
            GetMonthlyBillingReportUseCase.Result monthly,
            List<GetTopDebtorsReportUseCase.ResultItem> topDebtors,
            List<GetZoneDebtReportUseCase.ResultItem> zoneDebts,
            long totalClients,
            long activeClients,
            long inactiveClients,
            long totalInvoices,
            long openInvoices,
            long paidInvoices,
            BigDecimal totalOpenDebtOverall,
            long totalMeters,
            long pendingInvoices,
            BigDecimal collectedRevenue
    ) {
    }
}
