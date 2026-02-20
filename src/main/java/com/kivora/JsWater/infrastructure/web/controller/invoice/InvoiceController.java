
package com.kivora.JsWater.infrastructure.web.controller.invoice;

import com.kivora.JsWater.application.usecase.invoice.GetFinancialReportUseCase;
import com.kivora.JsWater.infrastructure.web.dto.invoice.FinancialReportResponse;

import com.kivora.JsWater.application.usecase.invoice.GenerateInvoiceUseCase;
import com.kivora.JsWater.application.usecase.invoice.ListInvoicesUseCase;
import com.kivora.JsWater.application.usecase.invoice.RegisterFineUseCase;
import com.kivora.JsWater.application.usecase.invoice.GetClientDebtSummaryUseCase;
import com.kivora.JsWater.application.usecase.invoice.GetClientStatementUseCase;
import com.kivora.JsWater.application.usecase.invoice.GetInvoiceByIdUseCase;
import com.kivora.JsWater.application.usecase.invoice.GetClientPendingInvoicesUseCase;
import com.kivora.JsWater.application.usecase.invoice.GetMonthlyBillingReportUseCase;
import com.kivora.JsWater.application.usecase.invoice.GetTopDebtorsReportUseCase;
import com.kivora.JsWater.application.usecase.invoice.GetZoneDebtReportUseCase;
import com.kivora.JsWater.application.usecase.invoice.PayInvoiceUseCase;
import com.kivora.JsWater.domain.model.invoice.Invoice;
import com.kivora.JsWater.application.usecase.invoice.GetBillingDetailReportUseCase;
import com.kivora.JsWater.domain.repository.ClientRepository;
import com.kivora.JsWater.domain.valueobject.reading.ReadingId;
import com.kivora.JsWater.infrastructure.web.dto.invoice.GenerateInvoiceRequest;
import com.kivora.JsWater.infrastructure.web.dto.invoice.RegisterFineRequest;
import com.kivora.JsWater.infrastructure.web.dto.invoice.InvoiceResponse;
import com.kivora.JsWater.infrastructure.web.dto.invoice.ClientDebtSummaryResponse;
import com.kivora.JsWater.infrastructure.web.dto.invoice.MonthlyBillingReportResponse;
import com.kivora.JsWater.infrastructure.web.dto.invoice.TopDebtorResponse;
import com.kivora.JsWater.infrastructure.web.dto.invoice.ZoneDebtResponse;
import com.kivora.JsWater.infrastructure.web.dto.invoice.BillingDetailResponse;
import com.kivora.JsWater.infrastructure.web.dto.common.PageResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.YearMonth;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/invoices")
@Tag(name = "Invoices", description = "Facturacao de consumo")

public class InvoiceController {

        private final GenerateInvoiceUseCase generateInvoiceUseCase;
        private final RegisterFineUseCase registerFineUseCase;
        private final ListInvoicesUseCase listInvoicesUseCase;
        private final GetInvoiceByIdUseCase getInvoiceByIdUseCase;
        private final GetClientStatementUseCase getClientStatementUseCase;
        private final GetClientDebtSummaryUseCase getClientDebtSummaryUseCase;
        private final GetClientPendingInvoicesUseCase getClientPendingInvoicesUseCase;
        private final PayInvoiceUseCase payInvoiceUseCase;
        private final GetMonthlyBillingReportUseCase getMonthlyBillingReportUseCase;
        private final GetTopDebtorsReportUseCase getTopDebtorsReportUseCase;
        private final GetZoneDebtReportUseCase getZoneDebtReportUseCase;
        private final GetBillingDetailReportUseCase getBillingDetailReportUseCase;
        private final GetFinancialReportUseCase getFinancialReportUseCase;
        private final ClientRepository clientRepository;

        public InvoiceController(GenerateInvoiceUseCase generateInvoiceUseCase,
                                                         RegisterFineUseCase registerFineUseCase,
                                                         ListInvoicesUseCase listInvoicesUseCase,
                                                         GetInvoiceByIdUseCase getInvoiceByIdUseCase,
                                                         GetClientStatementUseCase getClientStatementUseCase,
                                                         GetClientDebtSummaryUseCase getClientDebtSummaryUseCase,
                                                         GetClientPendingInvoicesUseCase getClientPendingInvoicesUseCase,
                                                         PayInvoiceUseCase payInvoiceUseCase,
                                                         GetMonthlyBillingReportUseCase getMonthlyBillingReportUseCase,
                                                         GetTopDebtorsReportUseCase getTopDebtorsReportUseCase,
                                                         GetZoneDebtReportUseCase getZoneDebtReportUseCase,
                                                         GetBillingDetailReportUseCase getBillingDetailReportUseCase,
                                                         GetFinancialReportUseCase getFinancialReportUseCase,
                                                         ClientRepository clientRepository) {
                this.generateInvoiceUseCase = generateInvoiceUseCase;
                this.registerFineUseCase = registerFineUseCase;
                this.listInvoicesUseCase = listInvoicesUseCase;
                this.getInvoiceByIdUseCase = getInvoiceByIdUseCase;
                this.getClientStatementUseCase = getClientStatementUseCase;
                this.getClientDebtSummaryUseCase = getClientDebtSummaryUseCase;
                this.getClientPendingInvoicesUseCase = getClientPendingInvoicesUseCase;
                this.payInvoiceUseCase = payInvoiceUseCase;
                this.getMonthlyBillingReportUseCase = getMonthlyBillingReportUseCase;
                this.getTopDebtorsReportUseCase = getTopDebtorsReportUseCase;
                this.getZoneDebtReportUseCase = getZoneDebtReportUseCase;
                this.getBillingDetailReportUseCase = getBillingDetailReportUseCase;
                this.getFinancialReportUseCase = getFinancialReportUseCase;
                this.clientRepository = clientRepository;
        }
        @Operation(
                summary = "Relatório financeiro consolidado",
                description = "Mostra totais financeiros (faturado, recebido, em aberto, multas, inadimplência, etc.) para um mês, com filtros opcionais de zona e cliente."
        )
        @ApiResponses({
                @ApiResponse(responseCode = "200", description = "Relatório financeiro gerado com sucesso"),
                @ApiResponse(responseCode = "400", description = "Parâmetros inválidos")
        })
        @GetMapping("/reports/financial")
        @PreAuthorize("hasRole('ADMIN')")
        public FinancialReportResponse getFinancialReport(
                @RequestParam("month") String month,
                @RequestParam(name = "zone", required = false) String zone,
                @RequestParam(name = "clientName", required = false) String clientName
        ) {
                YearMonth ym = YearMonth.parse(month);
                GetFinancialReportUseCase.Result result = getFinancialReportUseCase.execute(ym, zone, clientName);
                return FinancialReportResponse.from(result);
        }

    @Operation(
            summary = "Listar facturas",
            description = "Devolve uma lista de todas as facturas"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lista de facturas devolvida com sucesso")
    })
    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    public List<InvoiceResponse> list() {
        return listInvoicesUseCase.execute().stream()
                .map(invoice -> {
                    var clientOpt = clientRepository.findById(invoice.getClientId());
                    String clientName = clientOpt
                            .map(c -> c.getFullName().getValue())
                            .orElse("Desconhecido");
                    String bairro = clientOpt
                            .map(c -> c.getAddress().getBairro())
                            .orElse(null);
                    return InvoiceResponse.from(invoice, clientName, bairro);
                })
                .toList();
    }

    @Operation(
            summary = "Gerar Factura",
            description = "Gerar factura a partir de uma leitura valida"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Fatura gerada"),
            @ApiResponse(responseCode = "404", description = "Leitura não encontrada"),
            @ApiResponse(responseCode = "409", description = "Leitura já faturada")
    })

    @PostMapping("/register")
    @PreAuthorize("hasRole('ADMIN')")
    @ResponseStatus(HttpStatus.CREATED)
    public InvoiceResponse register(@RequestBody @Valid GenerateInvoiceRequest generateInvoiceRequest) {
        Invoice invoice = generateInvoiceUseCase.execute(
                new ReadingId(generateInvoiceRequest.readingId())
        );

        var clientOpt = clientRepository.findById(invoice.getClientId());
        String clientName = clientOpt
                .map(c -> c.getFullName().getValue())
                .orElse("Desconhecido");
        String bairro = clientOpt
                .map(c -> c.getAddress().getBairro())
                .orElse(null);

        return InvoiceResponse.from(invoice, clientName, bairro);
    }

    @Operation(
            summary = "Registar Multa",
            description = "Regista uma multa para um cliente manualmente"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Multa registada"),
            @ApiResponse(responseCode = "404", description = "Cliente não encontrado")
    })
    @PostMapping("/fines")
    @PreAuthorize("hasRole('ADMIN')")
    @ResponseStatus(HttpStatus.CREATED)
    public InvoiceResponse registerFine(@RequestBody @Valid RegisterFineRequest request) {
        Invoice invoice = registerFineUseCase.execute(
                request.clientId(),
                request.amount(),
                request.period()
        );

        var clientOpt = clientRepository.findById(invoice.getClientId());
        String clientName = clientOpt
                .map(c -> c.getFullName().getValue())
                .orElse("Desconhecido");
        String bairro = clientOpt
                .map(c -> c.getAddress().getBairro())
                .orElse(null);

        return InvoiceResponse.from(invoice, clientName, bairro);
    }

    @Operation(
            summary = "Obter factura por ID",
            description = "Devolve os dados de uma factura pelo seu ID"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Factura encontrada"),
            @ApiResponse(responseCode = "404", description = "Factura não encontrada")
    })
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    public InvoiceResponse getById(@PathVariable String id) {
        Invoice invoice = getInvoiceByIdUseCase.execute(id);

        var clientOpt = clientRepository.findById(invoice.getClientId());
        String clientName = clientOpt
                .map(c -> c.getFullName().getValue())
                .orElse("Desconhecido");
        String bairro = clientOpt
                .map(c -> c.getAddress().getBairro())
                .orElse(null);

        return InvoiceResponse.from(invoice, clientName, bairro);
    }

    @Operation(
            summary = "Extrato de facturas do cliente",
            description = "Lista as facturas (transacções) de um cliente, com filtro opcional por período (from/to em formato yyyy-MM)."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Extrato gerado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Parâmetros inválidos")
    })
    @GetMapping("/statement")
    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    public PageResponse<InvoiceResponse> getClientStatement(
            @RequestParam("clientId") UUID clientId,
                        @RequestParam(name = "from", required = false) String from,
                        @RequestParam(name = "to", required = false) String to,
                        @RequestParam(name = "page", defaultValue = "0") int page,
                        @RequestParam(name = "size", defaultValue = "20") int size
    ) {
        YearMonth fromPeriod = (from == null || from.isBlank()) ? null : YearMonth.parse(from);
        YearMonth toPeriod = (to == null || to.isBlank()) ? null : YearMonth.parse(to);

        List<Invoice> invoices = getClientStatementUseCase.execute(clientId, fromPeriod, toPeriod);

        var clientOpt = clientRepository.findById(new com.kivora.JsWater.domain.valueobject.client.ClientId(clientId));
        String clientName = clientOpt
                .map(c -> c.getFullName().getValue())
                .orElse("Desconhecido");
        String bairro = clientOpt
                .map(c -> c.getAddress().getBairro())
                .orElse(null);

        if (size <= 0) {
                size = 20;
        }
        if (page < 0) {
                page = 0;
        }

        long totalElements = invoices.size();
        int totalPages = totalElements == 0 ? 0 : (int) Math.ceil((double) totalElements / size);

        int fromIndex = page * size;
        if (fromIndex >= invoices.size()) {
                return new PageResponse<>(java.util.List.of(), page, size, totalElements, totalPages);
        }
        int toIndex = Math.min(fromIndex + size, invoices.size());

        List<InvoiceResponse> content = invoices.subList(fromIndex, toIndex)
                .stream()
                .map(i -> InvoiceResponse.from(i, clientName, bairro))
                .toList();

        return new PageResponse<>(content, page, size, totalElements, totalPages);
    }

    @Operation(
            summary = "Resumo de dívida do cliente",
            description = "Mostra o total em dívida, valor do mês actual e valor vencido de um cliente."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Resumo calculado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Parâmetros inválidos")
    })
    @GetMapping("/debt")
    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    public ClientDebtSummaryResponse getClientDebt(@RequestParam("clientId") UUID clientId) {
        GetClientDebtSummaryUseCase.Result result = getClientDebtSummaryUseCase.execute(clientId);

                var amountToPayNow = result.currentMonthAmount().add(result.overdueAmount());
                var remainingBalance = result.totalOutstanding().subtract(amountToPayNow);
                if (remainingBalance.signum() < 0) {
                        remainingBalance = java.math.BigDecimal.ZERO;
                }

        return new ClientDebtSummaryResponse(
                clientId,
                result.totalOutstanding(),
                result.currentMonthAmount(),
                                result.overdueAmount(),
                                amountToPayNow,
                                remainingBalance
        );
    }

    @Operation(
            summary = "Listar facturas pendentes do cliente",
            description = "Lista facturas em aberto de um cliente, podendo filtrar apenas as vencidas (overdueOnly=true)."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lista de facturas pendentes"),
            @ApiResponse(responseCode = "400", description = "Parâmetros inválidos")
    })
    @GetMapping("/pending")
    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    public PageResponse<InvoiceResponse> getClientPendingInvoices(
            @RequestParam("clientId") UUID clientId,
                        @RequestParam(name = "overdueOnly", defaultValue = "false") boolean overdueOnly,
                        @RequestParam(name = "page", defaultValue = "0") int page,
                        @RequestParam(name = "size", defaultValue = "20") int size
    ) {
                List<Invoice> invoices = getClientPendingInvoicesUseCase.execute(clientId, overdueOnly);

                var clientOpt = clientRepository.findById(new com.kivora.JsWater.domain.valueobject.client.ClientId(clientId));
                String clientName = clientOpt
                        .map(c -> c.getFullName().getValue())
                        .orElse("Desconhecido");
                String bairro = clientOpt
                        .map(c -> c.getAddress().getBairro())
                        .orElse(null);

                if (size <= 0) {
                                size = 20;
                }
                if (page < 0) {
                                page = 0;
                }

                long totalElements = invoices.size();
                int totalPages = totalElements == 0 ? 0 : (int) Math.ceil((double) totalElements / size);

                int fromIndex = page * size;
                if (fromIndex >= invoices.size()) {
                                return new PageResponse<>(java.util.List.of(), page, size, totalElements, totalPages);
                }
                int toIndex = Math.min(fromIndex + size, invoices.size());

                List<InvoiceResponse> content = invoices.subList(fromIndex, toIndex)
                                                                .stream()
                                                                .map(i -> InvoiceResponse.from(i, clientName, bairro))
                                                                .toList();

                return new PageResponse<>(content, page, size, totalElements, totalPages);
    }

        @Operation(
                        summary = "Pagar factura",
                        description = "Marca uma factura como paga."
        )
        @ApiResponses({
                        @ApiResponse(responseCode = "200", description = "Factura paga com sucesso"),
                        @ApiResponse(responseCode = "404", description = "Factura não encontrada"),
                        @ApiResponse(responseCode = "409", description = "Factura já está paga ou fechada")
        })
        @PostMapping("/{id}/pay")
        @PreAuthorize("hasRole('ADMIN')")
        public InvoiceResponse pay(@PathVariable String id) {
                Invoice invoice = payInvoiceUseCase.execute(id);

                var clientOpt = clientRepository.findById(invoice.getClientId());
                String clientName = clientOpt
                        .map(c -> c.getFullName().getValue())
                        .orElse("Desconhecido");
                String bairro = clientOpt
                        .map(c -> c.getAddress().getBairro())
                        .orElse(null);

                return InvoiceResponse.from(invoice, clientName, bairro);
        }

        @Operation(
                        summary = "Relatório mensal de facturação",
                        description = "Mostra totais de facturação para um mês (emitido, pago e em aberto)."
        )
        @ApiResponses({
                        @ApiResponse(responseCode = "200", description = "Relatório gerado com sucesso"),
                        @ApiResponse(responseCode = "400", description = "Parâmetros inválidos")
        })
        @GetMapping("/reports/monthly")
        @PreAuthorize("hasRole('ADMIN')")
        public MonthlyBillingReportResponse getMonthlyReport(
                        @RequestParam("month") String month
        ) {
                java.time.YearMonth ym = java.time.YearMonth.parse(month);
                GetMonthlyBillingReportUseCase.Result result = getMonthlyBillingReportUseCase.execute(ym);
                return MonthlyBillingReportResponse.from(result);
        }

                @Operation(
                                summary = "Relatório detalhado de faturação por cliente",
                                description = "Lista detalhada de clientes para um mês, com filtros por zona e nome do cliente."
                )
                @ApiResponses({
                                @ApiResponse(responseCode = "200", description = "Relatório detalhado gerado com sucesso"),
                                @ApiResponse(responseCode = "400", description = "Parâmetros inválidos")
                })
                @GetMapping("/reports/billing-detail")
                @PreAuthorize("hasRole('ADMIN')")
                public PageResponse<BillingDetailResponse> getBillingDetailReport(
                                @RequestParam("month") String month,
                                @RequestParam(name = "zone", required = false) String zone,
                                @RequestParam(name = "clientName", required = false) String clientName,
                                @RequestParam(name = "page", defaultValue = "0") int page,
                                @RequestParam(name = "size", defaultValue = "20") int size
                ) {
                        YearMonth ym = YearMonth.parse(month);
                        GetBillingDetailReportUseCase.Result result = getBillingDetailReportUseCase.execute(ym, zone, clientName);

                        if (size <= 0) {
                                size = 20;
                        }
                        if (page < 0) {
                                page = 0;
                        }

                        List<BillingDetailResponse> all = result.items().stream()
                                        .map(BillingDetailResponse::from)
                                        .toList();

                        long totalElements = all.size();
                        int totalPages = totalElements == 0 ? 0 : (int) Math.ceil((double) totalElements / size);

                        int fromIndex = page * size;
                        if (fromIndex >= all.size()) {
                                return new PageResponse<>(List.of(), page, size, totalElements, totalPages);
                        }
                        int toIndex = Math.min(fromIndex + size, all.size());

                        List<BillingDetailResponse> content = all.subList(fromIndex, toIndex);

                        return new PageResponse<>(content, page, size, totalElements, totalPages);
                }

        @Operation(
                        summary = "Top devedores",
                        description = "Lista os clientes com maior dívida em aberto."
        )
        @ApiResponses({
                        @ApiResponse(responseCode = "200", description = "Relatório gerado com sucesso")
        })
        @GetMapping("/reports/top-debtors")
        @PreAuthorize("hasRole('ADMIN')")
        public java.util.List<TopDebtorResponse> getTopDebtors(
                        @RequestParam(name = "limit", defaultValue = "10") int limit
        ) {
                java.util.List<GetTopDebtorsReportUseCase.ResultItem> items = getTopDebtorsReportUseCase.execute(limit);
                return items.stream().map(TopDebtorResponse::from).toList();
        }

        @Operation(
                        summary = "Dívida por zona",
                        description = "Mostra a soma de dívida em aberto por bairro (zona)."
        )
        @ApiResponses({
                        @ApiResponse(responseCode = "200", description = "Relatório gerado com sucesso")
        })
        @GetMapping("/reports/zone-debt")
        @PreAuthorize("hasRole('ADMIN')")
        public java.util.List<ZoneDebtResponse> getZoneDebt() {
                java.util.List<GetZoneDebtReportUseCase.ResultItem> items = getZoneDebtReportUseCase.execute();
                return items.stream().map(ZoneDebtResponse::from).toList();
        }

        @Operation(
                        summary = "Dívida por zona e mês",
                        description = "Mostra a soma de dívida em aberto por bairro (zona) para um mês específico."
        )
        @ApiResponses({
                        @ApiResponse(responseCode = "200", description = "Relatório gerado com sucesso"),
                        @ApiResponse(responseCode = "400", description = "Parâmetros inválidos")
        })
        @GetMapping("/reports/zone-debt/monthly")
        @PreAuthorize("hasRole('ADMIN')")
        public java.util.List<ZoneDebtResponse> getZoneDebtByMonth(
                        @RequestParam("month") String month
        ) {
                java.time.YearMonth ym = java.time.YearMonth.parse(month);
                java.util.List<GetZoneDebtReportUseCase.ResultItem> items = getZoneDebtReportUseCase.execute(ym);
                return items.stream().map(ZoneDebtResponse::from).toList();
        }
}
