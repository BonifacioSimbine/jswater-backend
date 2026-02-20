package com.kivora.JsWater.infrastructure.web.controller.expense;

import com.kivora.JsWater.application.usecase.expense.RegisterExpenseUseCase;
import com.kivora.JsWater.application.usecase.expense.ListExpensesUseCase;
import com.kivora.JsWater.infrastructure.web.dto.expense.ExpenseRequest;
import com.kivora.JsWater.infrastructure.web.dto.expense.ExpenseResponse;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/expenses")
public class ExpenseController {
    private final RegisterExpenseUseCase registerExpenseUseCase;
    private final ListExpensesUseCase listExpensesUseCase;

    public ExpenseController(RegisterExpenseUseCase registerExpenseUseCase, ListExpensesUseCase listExpensesUseCase) {
        this.registerExpenseUseCase = registerExpenseUseCase;
        this.listExpensesUseCase = listExpensesUseCase;
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    @ResponseStatus(HttpStatus.CREATED)
    public ExpenseResponse register(@RequestBody ExpenseRequest request) {
        var expense = registerExpenseUseCase.execute(
            request.amount(),
            request.date(),
            request.description(),
            request.category(),
            request.responsible()
        );
        return ExpenseResponse.from(expense);
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public List<ExpenseResponse> list() {
        return listExpensesUseCase.execute().stream().map(ExpenseResponse::from).collect(Collectors.toList());
    }
}
