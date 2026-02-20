package com.kivora.JsWater.application.usecase.invoice;

import com.kivora.JsWater.domain.model.invoice.Invoice;
import com.kivora.JsWater.domain.model.invoice.InvoiceStatus;
import com.kivora.JsWater.domain.model.client.Client;
import com.kivora.JsWater.domain.repository.InvoiceRepository;
import com.kivora.JsWater.domain.repository.ClientRepository;
import com.kivora.JsWater.domain.valueobject.invoice.InvoicePeriod;

import java.math.BigDecimal;
import java.time.YearMonth;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

public class GetZoneDebtReportUseCase {

    private final InvoiceRepository invoiceRepository;
    private final ClientRepository clientRepository;

    public GetZoneDebtReportUseCase(InvoiceRepository invoiceRepository,
                                    ClientRepository clientRepository) {
        this.invoiceRepository = invoiceRepository;
        this.clientRepository = clientRepository;
    }

    public List<ResultItem> execute() {
        List<Invoice> invoices = invoiceRepository.findAll();
        return aggregateByZone(invoices);
    }

    public List<ResultItem> execute(YearMonth month) {
                InvoicePeriod period = InvoicePeriod.of(month);
        List<Invoice> invoices = invoiceRepository.findAll().stream()
                .filter(i -> i.getPeriod().equals(period))
                .toList();

        return aggregateByZone(invoices);
    }

    private List<ResultItem> aggregateByZone(List<Invoice> invoices) {
        Map<String, ZoneAggregate> aggregates = new HashMap<>();

        invoices.stream()
                .filter(i -> i.getStatus() == InvoiceStatus.OPEN)
                .forEach(invoice -> {
                    String zone = getClientZone(invoice.getClientId());
                    if (zone == null || zone.isBlank()) {
                        return;
                    }

                    ZoneAggregate agg = aggregates.computeIfAbsent(zone, z -> new ZoneAggregate());
                    agg.totalDebt = agg.totalDebt.add(invoice.getTotalAmount().getValue());
                    agg.clientIds.add(invoice.getClientId());
                });

        return aggregates.entrySet().stream()
                .sorted((e1, e2) -> e2.getValue().totalDebt.compareTo(e1.getValue().totalDebt))
                .map(e -> new ResultItem(
                        e.getKey(),
                        e.getValue().totalDebt,
                        e.getValue().clientIds.size()
                ))
                .toList();
    }

    private String getClientZone(com.kivora.JsWater.domain.valueobject.client.ClientId clientId) {
        return clientRepository.findById(clientId)
                .map(Client::getAddress)
                .map(com.kivora.JsWater.domain.valueobject.Adress.Address::getBairro)
                .orElse("");
    }

    public record ResultItem(
            String zone,
                        BigDecimal totalDebt,
                        int clientCount
    ) {
    }

        private static class ZoneAggregate {
                private BigDecimal totalDebt = BigDecimal.ZERO;
                private Set<com.kivora.JsWater.domain.valueobject.client.ClientId> clientIds = new HashSet<>();
        }
}
