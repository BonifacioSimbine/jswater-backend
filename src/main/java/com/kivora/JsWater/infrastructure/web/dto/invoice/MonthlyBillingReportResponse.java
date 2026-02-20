package com.kivora.JsWater.infrastructure.web.dto.invoice;

import com.kivora.JsWater.application.usecase.invoice.GetMonthlyBillingReportUseCase;

import java.math.BigDecimal;

public record MonthlyBillingReportResponse(
        String period,
    long issuedCount,
    long paidCount,
    long openCount,
        BigDecimal totalIssued,
        BigDecimal totalPaid,
        BigDecimal totalOpen
) {

    public static MonthlyBillingReportResponse from(GetMonthlyBillingReportUseCase.Result result) {
        return new MonthlyBillingReportResponse(
                result.period().value().toString(),
        result.issuedCount(),
        result.paidCount(),
        result.openCount(),
                result.totalIssued(),
                result.totalPaid(),
                result.totalOpen()
        );
    }
}
