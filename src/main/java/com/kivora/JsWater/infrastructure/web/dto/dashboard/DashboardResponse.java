package com.kivora.JsWater.infrastructure.web.dto.dashboard;

import com.kivora.JsWater.application.usecase.invoice.GetDashboardOverviewUseCase;
import com.kivora.JsWater.infrastructure.web.dto.invoice.MonthlyBillingReportResponse;
import com.kivora.JsWater.infrastructure.web.dto.invoice.TopDebtorResponse;
import com.kivora.JsWater.infrastructure.web.dto.invoice.ZoneDebtResponse;

import java.math.BigDecimal;
import java.util.List;

public record DashboardResponse(
        String month,
        MonthlyBillingReportResponse monthly,
        List<TopDebtorResponse> topDebtors,
        List<ZoneDebtResponse> zoneDebts,
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

    public static DashboardResponse from(GetDashboardOverviewUseCase.Result result) {
        MonthlyBillingReportResponse monthlyResponse = MonthlyBillingReportResponse.from(result.monthly());
        List<TopDebtorResponse> topDebtors = result.topDebtors().stream()
                .map(TopDebtorResponse::from)
                .toList();
        List<ZoneDebtResponse> zoneDebts = result.zoneDebts().stream()
                .map(ZoneDebtResponse::from)
                .toList();

        return new DashboardResponse(
                result.month().toString(),
                monthlyResponse,
                topDebtors,
                zoneDebts,
                result.totalClients(),
                result.activeClients(),
                result.inactiveClients(),
                result.totalInvoices(),
                result.openInvoices(),
                                result.paidInvoices(),
                                result.totalOpenDebtOverall(),
                                result.totalMeters(),
                                result.pendingInvoices(),
                                result.collectedRevenue()
        );
    }
}
