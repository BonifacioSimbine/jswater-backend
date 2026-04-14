package com.kivora.JsWater.application.usecase.invoice;
import com.kivora.JsWater.application.usecase.tariff.GetActiveTariffForPeriodUseCase;
import com.kivora.JsWater.domain.model.client.Client;
import com.kivora.JsWater.domain.model.invoice.Invoice;
import com.kivora.JsWater.domain.model.invoice.InvoiceType;
import com.kivora.JsWater.domain.model.reading.Reading;
import com.kivora.JsWater.domain.model.tariff.Tariff;
import com.kivora.JsWater.domain.repository.ClientRepository;
import com.kivora.JsWater.domain.repository.InvoiceRepository;
import com.kivora.JsWater.domain.repository.ReadingRepository;
import com.kivora.JsWater.domain.valueobject.client.ClientId;
import com.kivora.JsWater.domain.valueobject.invoice.InvoicePeriod;
import com.kivora.JsWater.domain.valueobject.reading.ReadingId;

import java.math.BigDecimal;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GetBillingDetailReportUseCase {

    private static final Logger log = LoggerFactory.getLogger(GetBillingDetailReportUseCase.class);

    private final InvoiceRepository invoiceRepository;
    private final ClientRepository clientRepository;
    private final ReadingRepository readingRepository;
    private final GetClientDebtSummaryUseCase getClientDebtSummaryUseCase;
    private final GetActiveTariffForPeriodUseCase getActiveTariffForPeriodUseCase;

    public GetBillingDetailReportUseCase(
            InvoiceRepository invoiceRepository,
            ClientRepository clientRepository,
            ReadingRepository readingRepository,
            GetClientDebtSummaryUseCase getClientDebtSummaryUseCase,
            GetActiveTariffForPeriodUseCase getActiveTariffForPeriodUseCase
    ) {
        this.invoiceRepository = invoiceRepository;
        this.clientRepository = clientRepository;
        this.readingRepository = readingRepository;
        this.getClientDebtSummaryUseCase = getClientDebtSummaryUseCase;
        this.getActiveTariffForPeriodUseCase = getActiveTariffForPeriodUseCase;
    }

    public Result execute(YearMonth month, String zone, String clientName) {
        InvoicePeriod period = InvoicePeriod.of(month);

        List<Invoice> allInvoices = invoiceRepository.findAll();
        long waterCount = allInvoices.stream().filter(i -> i.getType() == InvoiceType.WATER && i.getPeriod().equals(period)).count();
        long fineCount = allInvoices.stream().filter(i -> i.getType() == InvoiceType.FINE && i.getPeriod().equals(period)).count();
        log.info("[Relatório Detalhado] Faturas WATER encontradas para o período {}: {}", period.value(), waterCount);
        log.info("[Relatório Detalhado] Faturas FINE encontradas para o período {}: {}", period.value(), fineCount);

        // Logar todas as faturas de multa para o período
        allInvoices.stream()
            .filter(i -> i.getType() == InvoiceType.FINE)
            .forEach(i -> log.info("[DEBUG MULTA] FINE: clientId={} period={} valor={}", i.getClientId().getValue(), i.getPeriod().value(), i.getTotalAmount().getValue()));

        Map<UUID, BigDecimal> finesByClient = calculateFinesByClientForPeriod(allInvoices, period);

        Tariff activeTariff = null;
        BigDecimal tariffUnit = BigDecimal.ZERO;
        int minimumConsumption = 0;
        try {
            activeTariff = getActiveTariffForPeriodUseCase.execute(period);
            tariffUnit = activeTariff.pricePerCubicMeter().getValue();
            minimumConsumption = activeTariff.getMinimumConsumption();
        } catch (Exception e) {
            log.warn("[Relatório Detalhado] Nenhuma tarifa ativa encontrada para o período {}", period.value());
        }
        log.info("[Relatório Detalhado] Tarifa ativa para o período {}: {} (mínimo: {})", period.value(), tariffUnit, minimumConsumption);

        List<Item> items = new ArrayList<>();

        String normalizedZone = zone != null ? zone.trim().toLowerCase(Locale.ROOT) : null;
        String normalizedClientName = clientName != null ? clientName.trim().toLowerCase(Locale.ROOT) : null;

        for (Invoice invoice : allInvoices) {
            if (invoice.getType() != InvoiceType.WATER) {
                continue;
            }
            if (!invoice.getPeriod().equals(period)) {
                continue;
            }

            ClientId clientId = invoice.getClientId();
            Optional<Client> clientOpt = clientRepository.findById(clientId);
            if (clientOpt.isEmpty()) {
                continue;
            }
            Client client = clientOpt.get();

            String bairro = client.getAddress() != null ? client.getAddress().getBairro() : null;

            if (normalizedZone != null && !normalizedZone.isBlank()) {
                String clientZone = bairro != null ? bairro.trim().toLowerCase(Locale.ROOT) : "";
                if (!clientZone.equals(normalizedZone)) {
                    continue;
                }
            }

            if (normalizedClientName != null && !normalizedClientName.isBlank()) {
                String clientFullName = client.getFullName() != null
                        ? client.getFullName().getValue().toLowerCase(Locale.ROOT)
                        : "";
                if (!clientFullName.contains(normalizedClientName)) {
                    continue;
                }
            }

            Reading reading = null;
            ReadingId readingId = invoice.getReadingId();
            if (readingId != null) {
                reading = readingRepository.findById(readingId).orElse(null);
            }

            BigDecimal previousReading = reading != null ? reading.getPrevious().getValue() : null;
            BigDecimal currentReading = reading != null ? reading.getCurrent().getValue() : null;
            BigDecimal consumption = invoice.getConsumption() != null
                    ? invoice.getConsumption().getMetrodCubicos()
                    : null;

            BigDecimal invoiceAmount = invoice.getTotalAmount().getValue();

            BigDecimal fineAmount = finesByClient.getOrDefault(clientId.getValue(), BigDecimal.ZERO);

            GetClientDebtSummaryUseCase.Result debtSummary = getClientDebtSummaryUseCase.execute(clientId.getValue());

            BigDecimal totalOutstanding = debtSummary.totalOutstanding();
            BigDecimal overdueAmount = debtSummary.overdueAmount();
            BigDecimal currentMonthAmount = debtSummary.currentMonthAmount();

            // Calcular valor efetivo da tarifa aplicada (mínimo se consumo < mínimo)
            BigDecimal effectiveTariff = BigDecimal.ZERO;
            if (consumption != null && tariffUnit != null) {
                if (minimumConsumption > 0 && consumption.compareTo(BigDecimal.valueOf(minimumConsumption)) < 0) {
                    effectiveTariff = tariffUnit.multiply(BigDecimal.valueOf(minimumConsumption));
                } else {
                    effectiveTariff = tariffUnit.multiply(consumption);
                }
            }

            // Valor total a pagar: valor da fatura + débito anterior + multa
            BigDecimal amountToPay = invoiceAmount.add(overdueAmount).add(fineAmount);

            // Diferença: saldo restante após pagamento
            BigDecimal difference = totalOutstanding.subtract(amountToPay);
            if (difference.signum() < 0) {
                difference = BigDecimal.ZERO;
            }

            log.info("[Relatório Detalhado] Cliente: {} | Bairro: {} | Leituras: {} -> {} | Consumo: {} | Fatura: {} | Dívida: {} | Tarifa Aplicada: {} | Multa: {} | Valor a Pagar: {} | Diferença: {}", 
                client.getFullName() != null ? client.getFullName().getValue() : null,
                bairro,
                previousReading,
                currentReading,
                consumption,
                invoiceAmount,
                totalOutstanding,
                effectiveTariff,
                fineAmount,
                amountToPay,
                difference
            );

            // Map InvoiceStatus to status string
            String status;
            switch (invoice.getStatus()) {
            case PAID -> status = "PAID";
            case OPEN -> status = "PENDING";
            case CLOSED -> status = "CANCELLED";
            default -> status = "UNKNOWN";
            }

            Item item = new Item(
                clientId.getValue(),
                client.getFullName() != null ? client.getFullName().getValue() : null,
                bairro,
                previousReading,
                currentReading,
                consumption,
                invoiceAmount,
                totalOutstanding,
                tariffUnit, // Corrigido: agora retorna o valor unitário da tarifa
                fineAmount,
                amountToPay,
                difference,
                period.value(),
                status
            );

            items.add(item);
        }

        return new Result(items);
    }

    private Map<UUID, BigDecimal> calculateFinesByClientForPeriod(List<Invoice> invoices, InvoicePeriod period) {
        Map<UUID, BigDecimal> finesByClient = new HashMap<>();

        for (Invoice invoice : invoices) {
            if (invoice.getType() != InvoiceType.FINE) {
                continue;
            }
            log.info("[DEBUG MULTA] Verificando multa: clientId={} period={} esperado={} valor={}", invoice.getClientId().getValue(), invoice.getPeriod().value(), period.value(), invoice.getTotalAmount().getValue());
            if (!invoice.getPeriod().equals(period)) {
                log.info("[DEBUG MULTA] Ignorado: período não coincide ({} != {})", invoice.getPeriod().value(), period.value());
                continue;
            }

            UUID clientId = invoice.getClientId().getValue();
            BigDecimal currentTotal = finesByClient.getOrDefault(clientId, BigDecimal.ZERO);
            BigDecimal newTotal = currentTotal.add(invoice.getTotalAmount().getValue());
            finesByClient.put(clientId, newTotal);
            log.info("[DEBUG MULTA] Somando multa para clientId={}: totalAtual={} + multa={} => novoTotal={}", clientId, currentTotal, invoice.getTotalAmount().getValue(), newTotal);
        }

        return finesByClient;
    }

    private BigDecimal resolveTariffRateForPeriod(InvoicePeriod period) {
        try {
            Tariff tariff = getActiveTariffForPeriodUseCase.execute(period);
            return tariff.pricePerCubicMeter().getValue();
        } catch (Exception e) {
            return BigDecimal.ZERO;
        }
    }

        public record Item(
            UUID clientId,
            String clientName,
            String bairro,
            BigDecimal previousReading,
            BigDecimal currentReading,
            BigDecimal consumption,
            BigDecimal invoiceAmount,
            BigDecimal totalDebt,
            BigDecimal tariffRate,
            BigDecimal fineAmount,
            BigDecimal amountToPay,
            BigDecimal difference,
            YearMonth period,
            String status
        ) {
        }

    public record Result(List<Item> items) {
    }
}
