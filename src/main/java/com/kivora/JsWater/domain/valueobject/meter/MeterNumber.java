package com.kivora.JsWater.domain.valueobject.meter;

import java.math.BigDecimal;

public class MeterNumber {

    private final String value;

    public MeterNumber(String value) {

        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("O numero de contador nao pode ser nulo ou vazio");
        }

        this.value = value.trim();
    }

    public String getValue() {
        return value;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (!(obj instanceof MeterNumber that)) return false;
        return value.equals(that.value);
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }
    @Override
    public String toString() {
        return value;
    }
}
