package com.kivora.JsWater.infrastructure.web.dto.invoice;

import java.math.BigDecimal;
import java.util.UUID;

public record ClientDebtSummaryResponse(
                UUID clientId,
                BigDecimal totalOutstanding,
                BigDecimal monthlyAmount,
                BigDecimal overdueAmount,
                BigDecimal amountToPayNow,
                BigDecimal remainingBalance
) {
}
