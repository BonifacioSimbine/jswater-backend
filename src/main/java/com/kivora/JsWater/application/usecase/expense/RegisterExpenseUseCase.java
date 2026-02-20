package com.kivora.JsWater.application.usecase.expense;

import com.kivora.JsWater.domain.model.expense.Expense;
import com.kivora.JsWater.domain.repository.ExpenseRepository;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

public class RegisterExpenseUseCase {
    private final ExpenseRepository expenseRepository;

    public RegisterExpenseUseCase(ExpenseRepository expenseRepository) {
        this.expenseRepository = expenseRepository;
    }

    public Expense execute(BigDecimal amount, LocalDate date, String description, String category, String responsible) {
        Expense expense = new Expense(UUID.randomUUID(), amount, date, description, category, responsible);
        return expenseRepository.save(expense);
    }
}
