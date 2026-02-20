package com.kivora.JsWater.domain.valueobject.invoice;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.Objects;

public class InvoiceDueDate {

    private final LocalDate value;

    private InvoiceDueDate(LocalDate value) {
        this.value = value;
    }

    public static InvoiceDueDate from(InvoicePeriod period) {
        Objects.requireNonNull(period);

        YearMonth nextMonth = period.value().plusMonths(1);
        LocalDate dueDate = nextMonth.atDay(10);

        return new InvoiceDueDate(dueDate);
    }

    public LocalDate value() {
        return value;
    }

    public boolean isOverdue(LocalDate today) {
        return today.isAfter(value);
    }

    @Override
    public String toString() {
        return value.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof InvoiceDueDate)) return false;
        InvoiceDueDate that = (InvoiceDueDate) o;
        return value.equals(that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }
}
