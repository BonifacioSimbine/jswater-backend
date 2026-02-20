    // Método zero() correto está mais abaixo
package com.kivora.JsWater.domain.valueobject.money;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class Money {

    private final BigDecimal value;

    public Money(BigDecimal value) {
        if (value == null) {
            throw new IllegalArgumentException("valor nao pode ser nulo");
        }
        this.value = value.setScale(2, RoundingMode.HALF_UP);
    }
    public static Money zero() {
        return new Money(BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP));
    }

    public BigDecimal getValue() {
        return value;
    }



    public Money add(Money money) {
        return new Money(value.add(money.getValue()));
    }

    public Money subtract(Money money) {
        return new Money(value.subtract(money.getValue()));
    }

    public boolean isZeroOrNegative() {
        return value.compareTo(BigDecimal.ZERO) <= 0;
    }

    public Money multiply(BigDecimal valor2) {
        if (valor2 == null || valor2.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("valor nao pode ser negativo");
        }
        return new Money(value.multiply(valor2));
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }

        if (!(obj instanceof Money money)) {
            return false;
        }
        return value.compareTo(money.getValue()) == 0;
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }

}
