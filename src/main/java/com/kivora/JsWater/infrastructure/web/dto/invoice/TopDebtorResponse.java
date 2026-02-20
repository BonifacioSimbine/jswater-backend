package com.kivora.JsWater.infrastructure.web.dto.invoice;

import com.kivora.JsWater.application.usecase.invoice.GetTopDebtorsReportUseCase;

import java.math.BigDecimal;
import java.util.UUID;

public record TopDebtorResponse(
        UUID clientId,
        String fullName,
        String document,
        String phoneNumber,
        String zone,
        BigDecimal totalDebt
) {
    public static TopDebtorResponse from(GetTopDebtorsReportUseCase.ResultItem item) {
        return new TopDebtorResponse(
                item.clientId(),
                item.fullName(),
                item.document(),
                item.phoneNumber(),
                item.zone(),
                item.totalDebt()
        );
    }
}
