package com.kivora.JsWater.domain.repository;

import com.kivora.JsWater.domain.model.expense.Expense;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ExpenseRepository {
    Expense save(Expense expense);
    Optional<Expense> findById(UUID id);
    List<Expense> findAll();
    List<Expense> findByDateBetween(LocalDate start, LocalDate end);
    void deleteById(UUID id);
}
