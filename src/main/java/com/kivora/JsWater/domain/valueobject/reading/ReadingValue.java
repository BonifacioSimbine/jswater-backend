package com.kivora.JsWater.domain.valueobject.reading;

import java.math.BigDecimal;

public class ReadingValue implements Comparable<ReadingValue>{

    private final BigDecimal value;

    public ReadingValue(BigDecimal value) {

        if (value == null || value.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("O valor da leitura deve ser positivo");
        }

        this.value = value;
    }


    public static ReadingValue zero() {
        return new ReadingValue(BigDecimal.ZERO);
    }

    public BigDecimal getValue() {
        return value;
    }

    public BigDecimal subtract(ReadingValue readingValue) {
        return this.value.subtract(readingValue.getValue());
    }

    @Override
    public int compareTo(ReadingValue other)  {
        return value.compareTo(other.value);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof ReadingValue that)) return false;
        return value.compareTo(that.value) == 0;
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }

}
