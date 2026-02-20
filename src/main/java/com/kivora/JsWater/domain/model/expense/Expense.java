package com.kivora.JsWater.domain.model.expense;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

public class Expense {
    private UUID id;
    private BigDecimal amount;
    private LocalDate date;
    private String description;
    private String category;
    private String responsible;

    public Expense(UUID id, BigDecimal amount, LocalDate date, String description, String category, String responsible) {
        this.id = id;
        this.amount = amount;
        this.date = date;
        this.description = description;
        this.category = category;
        this.responsible = responsible;
    }

    public UUID getId() { return id; }
    public BigDecimal getAmount() { return amount; }
    public LocalDate getDate() { return date; }
    public String getDescription() { return description; }
    public String getCategory() { return category; }
    public String getResponsible() { return responsible; }

    public void setId(UUID id) { this.id = id; }
    public void setAmount(BigDecimal amount) { this.amount = amount; }
    public void setDate(LocalDate date) { this.date = date; }
    public void setDescription(String description) { this.description = description; }
    public void setCategory(String category) { this.category = category; }
    public void setResponsible(String responsible) { this.responsible = responsible; }
}
