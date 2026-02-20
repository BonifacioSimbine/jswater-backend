package com.kivora.JsWater.application.usecase.invoice;

import com.kivora.JsWater.domain.model.invoice.Invoice;
import com.kivora.JsWater.domain.model.invoice.InvoiceStatus;
import com.kivora.JsWater.domain.model.client.Client;
import com.kivora.JsWater.domain.repository.InvoiceRepository;
import com.kivora.JsWater.domain.repository.ClientRepository;
import com.kivora.JsWater.domain.valueobject.client.ClientId;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

public class GetTopDebtorsReportUseCase {

        private final InvoiceRepository invoiceRepository;
        private final ClientRepository clientRepository;

        public GetTopDebtorsReportUseCase(InvoiceRepository invoiceRepository,
                                                                          ClientRepository clientRepository) {
                this.invoiceRepository = invoiceRepository;
                this.clientRepository = clientRepository;
        }

    public List<ResultItem> execute(int limit) {
        if (limit <= 0) {
            limit = 10;
        }

        List<Invoice> invoices = invoiceRepository.findAll();

        Map<ClientId, BigDecimal> debtByClient = invoices.stream()
                .filter(i -> i.getStatus() == InvoiceStatus.OPEN)
                .collect(Collectors.groupingBy(
                        Invoice::getClientId,
                        Collectors.mapping(
                                i -> i.getTotalAmount().getValue(),
                                Collectors.reducing(BigDecimal.ZERO, BigDecimal::add)
                        )
                ));

        return debtByClient.entrySet().stream()
                .sorted(Map.Entry.<ClientId, BigDecimal>comparingByValue(Comparator.reverseOrder()))
                .limit(limit)
                .map(e -> {
                    ClientId clientId = e.getKey();
                    BigDecimal totalDebt = e.getValue();

                    Client client = clientRepository.findById(clientId).orElse(null);

                    String fullName = client != null ? client.getFullName().getValue() : null;
                    String document = client != null ? client.getDocument().getValue() : null;
                    String phone = client != null ? client.getPhoneNumber().getValue() : null;
                    String zone = client != null ? client.getAddress().getBairro() : null;

                    return new ResultItem(
                            clientId.value(),
                            fullName,
                            document,
                            phone,
                            zone,
                            totalDebt
                    );
                })
                .toList();
    }

    public record ResultItem(
            UUID clientId,
            String fullName,
            String document,
            String phoneNumber,
            String zone,
            BigDecimal totalDebt
    ) {
    }
}
