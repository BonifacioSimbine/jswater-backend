package com.kivora.JsWater.application.usecase.expense;

import com.kivora.JsWater.domain.model.expense.Expense;
import com.kivora.JsWater.domain.repository.ExpenseRepository;
import java.time.LocalDate;
import java.util.List;

public class ListExpensesUseCase {
    private final ExpenseRepository expenseRepository;

    public ListExpensesUseCase(ExpenseRepository expenseRepository) {
        this.expenseRepository = expenseRepository;
    }

    public List<Expense> execute() {
        return expenseRepository.findAll();
    }

    public List<Expense> execute(LocalDate from, LocalDate to) {
        return expenseRepository.findByDateBetween(from, to);
    }
}
