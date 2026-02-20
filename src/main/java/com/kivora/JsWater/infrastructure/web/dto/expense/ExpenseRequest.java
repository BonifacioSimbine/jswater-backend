package com.kivora.JsWater.infrastructure.web.dto.expense;

import java.math.BigDecimal;
import java.time.LocalDate;

public record ExpenseRequest(
    BigDecimal amount,
    LocalDate date,
    String description,
    String category,
    String responsible
) {}
