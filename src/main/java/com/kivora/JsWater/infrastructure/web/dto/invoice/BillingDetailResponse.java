package com.kivora.JsWater.infrastructure.web.dto.invoice;

import com.kivora.JsWater.application.usecase.invoice.GetBillingDetailReportUseCase;

import java.math.BigDecimal;
import java.time.YearMonth;
import java.util.UUID;

public record BillingDetailResponse(
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

    public static BillingDetailResponse from(GetBillingDetailReportUseCase.Item item) {
        return new BillingDetailResponse(
                item.clientId(),
                item.clientName(),
                item.bairro(),
                item.previousReading(),
                item.currentReading(),
                item.consumption(),
                item.invoiceAmount(),
                item.totalDebt(),
                item.tariffRate(),
                item.fineAmount(),
                item.amountToPay(),
                item.difference(),
                item.period(),
                item.status()
        );
    }
}
