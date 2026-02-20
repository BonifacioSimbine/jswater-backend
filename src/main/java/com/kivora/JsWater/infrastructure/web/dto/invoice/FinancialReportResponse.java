package com.kivora.JsWater.infrastructure.web.dto.invoice;

import com.kivora.JsWater.application.usecase.invoice.GetFinancialReportUseCase;
import java.math.BigDecimal;

public record FinancialReportResponse(
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
) {
    public static FinancialReportResponse from(GetFinancialReportUseCase.Result result) {
        return new FinancialReportResponse(
            result.totalFaturado(),
            result.totalRecebido(),
            result.totalEmAberto(),
            result.totalMultas(),
            result.totalDespesas(),
            result.saldoLiquido(),
            result.quantidadeFaturasEmitidas(),
            result.quantidadeFaturasPagas(),
            result.quantidadeFaturasEmAberto(),
            result.inadimplenciaPercentual()
        );
    }
}
