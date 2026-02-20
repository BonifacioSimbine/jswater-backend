package com.kivora.JsWater.domain.model.tariff;

import com.kivora.JsWater.domain.exception.tariff.InvalidTariffCalculationException;
import com.kivora.JsWater.domain.exception.tariff.InvalidTariffPeriodException;
import com.kivora.JsWater.domain.exception.tariff.TariffAlreadyInactiveException;
import com.kivora.JsWater.domain.valueobject.Tariff.TariffId;
import com.kivora.JsWater.domain.valueobject.consumption.Consumption;
import com.kivora.JsWater.domain.valueobject.invoice.InvoicePeriod;
import com.kivora.JsWater.domain.valueobject.money.Money;
import lombok.Getter;
import java.math.*;

import java.util.Objects;

public class Tariff {

    private final TariffId id;
    private final InvoicePeriod validFrom;
    private InvoicePeriod validTo;
    private final Money pricePerCubicMeter;
    private TariffStatus status;
    @Getter
    private final int minimumConsumption;

    private Tariff(TariffId id,
                   InvoicePeriod validFrom,
                   InvoicePeriod validTo,
                   Money pricePerCubicMeter,
                   TariffStatus status,
                   int minimumConsumption
    ) {
        this.id = id;
        this.validFrom = validFrom;
        this.validTo = validTo;
        this.pricePerCubicMeter = pricePerCubicMeter;
        this.status = status;
        this.minimumConsumption = minimumConsumption;
    }

    /* ===== Factory Methods ===== */
    public static Tariff create(InvoicePeriod validFrom, Money pricePerCubicMeter) {
        return new Tariff(
                TariffId.generate(),
                validFrom,
                null,
                pricePerCubicMeter,
                TariffStatus.ACTIVE,
                5
        );
    }

    public static Tariff restore(TariffId id,
                                 InvoicePeriod validFrom,
                                 InvoicePeriod validTo,
                                 Money pricePerCubicMeter,
                                 TariffStatus status,
                                 int minimumConsumption) {
        Objects.requireNonNull(id, "TariffId is required");
        Objects.requireNonNull(validFrom, "ValidFrom is required");
        Objects.requireNonNull(pricePerCubicMeter, "Price is required");
        Objects.requireNonNull(status, "Status is required");

        return new Tariff(id, validFrom, validTo, pricePerCubicMeter, status, minimumConsumption);
    }


    public Money calculate(Consumption consumption) {
        Objects.requireNonNull(consumption, "Consumo é obrigatório");

        if (consumption.isZeroOrNegative()) {
            throw new InvalidTariffCalculationException(
                    "O consumo deve ser maior que zero."
            );
        }

        java.math.BigDecimal consumo = consumption.getMetrodCubicos();
        java.math.BigDecimal taxaMinima = new BigDecimal("300.00");
        java.math.BigDecimal consumoMinimo = new BigDecimal(this.minimumConsumption);
        java.math.BigDecimal precoPorMetro = this.pricePerCubicMeter.getValue();

        if (consumo.compareTo(consumoMinimo) <= 0) {
            return new com.kivora.JsWater.domain.valueobject.money.Money(taxaMinima);
        } else {
            java.math.BigDecimal adicional = consumo.subtract(consumoMinimo).multiply(precoPorMetro);
            java.math.BigDecimal total = taxaMinima.add(adicional);
            return new com.kivora.JsWater.domain.valueobject.money.Money(total);
        }
    }

    public boolean isActiveFor(InvoicePeriod period) {
        if (status != TariffStatus.ACTIVE) return false;

        boolean afterStart = !period.isBefore(validFrom);
        boolean beforeEnd = validTo == null || !period.isAfter(validTo);

        return afterStart && beforeEnd;
    }

    public void desactivate(InvoicePeriod endPeriod) {
        Objects.requireNonNull(endPeriod, "Periodo de fim, e obrigatorio");

        if (endPeriod.isBefore(validFrom)) {
            throw new InvalidTariffPeriodException(
                    "End period cannot be before tariff start period"
            );
        }

        if (status != TariffStatus.ACTIVE) {
            throw new TariffAlreadyInactiveException();
        }

        this.validTo = endPeriod;
        this.status = TariffStatus.INACTIVE;
    }

    /* ===== Getters ===== */
    public Money pricePerCubicMeter() {
        return pricePerCubicMeter;
    }

    public TariffStatus getStatus() {
        return status;
    }

    public TariffId getId() {
        return id;
    }

    public InvoicePeriod getValidFrom() {
        return validFrom;
    }

    public InvoicePeriod getValidTo() {
        return validTo;
    }
}
