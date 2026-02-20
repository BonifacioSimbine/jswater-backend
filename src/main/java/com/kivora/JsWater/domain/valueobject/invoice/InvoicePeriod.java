package com.kivora.JsWater.domain.valueobject.invoice;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.Objects;

public final class InvoicePeriod {

    private final YearMonth value;

    private InvoicePeriod(YearMonth value) {
        this.value = value;
    }



    public static InvoicePeriod of(YearMonth period) {
        Objects.requireNonNull(period, "Period de pagamento de factura e obrigatorio");
        return new InvoicePeriod(period);
    }

    public static InvoicePeriod from(LocalDate value) {
        Objects.requireNonNull(value, "A Data de Leitura e obrigatoria");
        return new InvoicePeriod(YearMonth.from(value));
    }





    public static InvoicePeriod current() {
        return new InvoicePeriod(YearMonth.now());
    }



    public boolean isBefore(InvoicePeriod other) {
        return this.value.isBefore(other.value);
    }

    public boolean isAfter(InvoicePeriod other) {
        return this.value.isAfter(other.value);
    }

    public InvoicePeriod next() {
        return new InvoicePeriod(this.value.plusMonths(1));
    }

    public InvoicePeriod previous() {
        return new InvoicePeriod(this.value.minusMonths(1));
    }



    public YearMonth value() {
        return value;
    }



    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof InvoicePeriod)) return false;
        InvoicePeriod that = (InvoicePeriod) o;
        return value.equals(that.value);
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }

    @Override
    public String toString() {
        return value.toString();
    }
}

