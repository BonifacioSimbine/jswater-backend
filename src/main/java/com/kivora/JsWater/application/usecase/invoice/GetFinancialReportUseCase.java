package com.kivora.JsWater.application.usecase.invoice;

import com.kivora.JsWater.domain.model.invoice.Invoice;
import com.kivora.JsWater.domain.model.invoice.InvoiceStatus;
import com.kivora.JsWater.domain.model.invoice.InvoiceType;
import com.kivora.JsWater.domain.repository.ClientRepository;
import com.kivora.JsWater.domain.repository.InvoiceRepository;
import com.kivora.JsWater.domain.repository.ExpenseRepository;
import java.math.BigDecimal;
import java.time.YearMonth;
import java.util.List;
import java.util.Locale;
import java.util.Optional;


public class GetFinancialReportUseCase {
    private final InvoiceRepository invoiceRepository;
    private final ClientRepository clientRepository;
    private final ExpenseRepository expenseRepository;

    public GetFinancialReportUseCase(InvoiceRepository invoiceRepository, ClientRepository clientRepository, ExpenseRepository expenseRepository) {
        this.invoiceRepository = invoiceRepository;
        this.clientRepository = clientRepository;
        this.expenseRepository = expenseRepository;
    }

    public Result execute(YearMonth month, String zone, String clientName) {
        List<Invoice> allInvoices = invoiceRepository.findAll();
        String normalizedZone = zone != null ? zone.trim().toLowerCase(Locale.ROOT) : null;
        String normalizedClientName = clientName != null ? clientName.trim().toLowerCase(Locale.ROOT) : null;

        BigDecimal totalFaturado = BigDecimal.ZERO;
        BigDecimal totalRecebido = BigDecimal.ZERO;
        BigDecimal totalEmAberto = BigDecimal.ZERO;
        BigDecimal totalMultas = BigDecimal.ZERO;
        int qtdEmitidas = 0, qtdPagas = 0, qtdEmAberto = 0;
        int clientesComDebito = 0, totalClientes = 0;

        // Filtro por mês e zona/cliente
        for (Invoice invoice : allInvoices) {
            if (!invoice.getPeriod().value().equals(month)) continue;
            Optional<com.kivora.JsWater.domain.model.client.Client> clientOpt = clientRepository.findById(invoice.getClientId());
            if (clientOpt.isEmpty()) continue;
            var client = clientOpt.get();
            String bairro = client.getAddress() != null ? client.getAddress().getBairro() : null;
            if (normalizedZone != null && !normalizedZone.isBlank()) {
                String clientZone = bairro != null ? bairro.trim().toLowerCase(Locale.ROOT) : "";
                if (!clientZone.equals(normalizedZone)) continue;
            }
            if (normalizedClientName != null && !normalizedClientName.isBlank()) {
                String clientFullName = client.getFullName() != null ? client.getFullName().getValue().toLowerCase(Locale.ROOT) : "";
                if (!clientFullName.contains(normalizedClientName)) continue;
            }
            qtdEmitidas++;
            totalFaturado = totalFaturado.add(invoice.getTotalAmount().getValue());
            if (invoice.getType() == InvoiceType.FINE) {
                totalMultas = totalMultas.add(invoice.getTotalAmount().getValue());
            }
            if (invoice.getStatus() == InvoiceStatus.PAID) {
                qtdPagas++;
                totalRecebido = totalRecebido.add(invoice.getTotalAmount().getValue());
            } else if (invoice.getStatus() == InvoiceStatus.OPEN) {
                qtdEmAberto++;
                totalEmAberto = totalEmAberto.add(invoice.getTotalAmount().getValue());
            }
        }
        // Inadimplência: % de clientes com faturas em aberto
        var allClients = clientRepository.findAll();
        totalClientes = allClients.size();
        for (var client : allClients) {
            boolean hasOpen = allInvoices.stream().anyMatch(inv -> inv.getClientId().equals(client.getId()) && inv.getStatus() == InvoiceStatus.OPEN && inv.getPeriod().value().equals(month));
            if (hasOpen) clientesComDebito++;
        }
        double inadimplencia = totalClientes > 0 ? (clientesComDebito * 100.0 / totalClientes) : 0.0;

        // Despesas e saldo líquido
        java.time.LocalDate from = month.atDay(1);
        java.time.LocalDate to = month.atEndOfMonth();
        var expenses = expenseRepository.findByDateBetween(from, to);
        BigDecimal totalDespesas = expenses.stream().map(e -> e.getAmount()).reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal saldoLiquido = totalRecebido.subtract(totalDespesas);

        return new Result(totalFaturado, totalRecebido, totalEmAberto, totalMultas, totalDespesas, saldoLiquido, qtdEmitidas, qtdPagas, qtdEmAberto, inadimplencia);
    }

    public record Result(
        BigDecimal totalFaturado,
        BigDecimal totalRecebido,
        BigDecimal totalEmAberto,
        BigDecimal totalMultas,
        BigDecimal totalDespesas,
        BigDecimal saldoLiquido,
        int quantidadeFaturasEmitidas,
        int quantidadeFaturasPagas,
        int quantidadeFaturasEmAberto,
        double inadimplenciaPercentual
    ) {}
}
