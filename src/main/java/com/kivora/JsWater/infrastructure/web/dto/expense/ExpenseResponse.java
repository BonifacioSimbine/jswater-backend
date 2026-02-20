package com.kivora.JsWater.infrastructure.web.dto.expense;

import com.kivora.JsWater.domain.model.expense.Expense;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

public record ExpenseResponse(
    UUID id,
    BigDecimal amount,
    LocalDate date,
    String description,
    String category,
    String responsible
) {
    public static ExpenseResponse from(Expense expense) {
        return new ExpenseResponse(
            expense.getId(),
            expense.getAmount(),
            expense.getDate(),
            expense.getDescription(),
            expense.getCategory(),
            expense.getResponsible()
        );
    }
}
