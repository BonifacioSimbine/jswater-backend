package com.kivora.JsWater.infrastructure.web.dto.invoice;

import com.kivora.JsWater.application.usecase.invoice.GetZoneDebtReportUseCase;

import java.math.BigDecimal;

public record ZoneDebtResponse(
        String zone,
        BigDecimal totalDebt,
        int clientCount
) {
    public static ZoneDebtResponse from(GetZoneDebtReportUseCase.ResultItem item) {
        return new ZoneDebtResponse(
                item.zone(),
                item.totalDebt(),
                item.clientCount()
        );
    }
}
