package com.kivora.JsWater.domain.valueobject.consumption;

import java.math.BigDecimal;

public class Consumption {

    private BigDecimal metrodCubicos;

    public Consumption(BigDecimal metrodCubicos) {

        if (metrodCubicos == null || metrodCubicos.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Os metros cubicos deve ser positivo");
        }

        this.metrodCubicos = metrodCubicos;
    }

    public boolean isZeroOrNegative() {
        return metrodCubicos.compareTo(BigDecimal.ZERO) <= 0;
    }

    public BigDecimal getMetrodCubicos() {
        return metrodCubicos;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Consumption that)) return false;
        return metrodCubicos.compareTo(that.metrodCubicos) == 0;
    }

    @Override
    public int hashCode() {
        return metrodCubicos.hashCode();
    }
}