package com.kivora.JsWater.infrastructure.persistence.mapper.tariff;

import com.kivora.JsWater.domain.model.tariff.Tariff;
import com.kivora.JsWater.domain.valueobject.invoice.InvoicePeriod;
import com.kivora.JsWater.domain.valueobject.Tariff.TariffId;
import com.kivora.JsWater.domain.valueobject.money.Money;
import com.kivora.JsWater.infrastructure.persistence.entity.tariff.TariffJpaEntity;
import com.kivora.JsWater.infrastructure.util.DateUtils;

public class TariffMapper {

    private TariffMapper() {}

    public static TariffJpaEntity toJpa(Tariff tariff) {
        TariffJpaEntity entity = new TariffJpaEntity();

        entity.setId(tariff.getId().value());
        entity.setValidFrom(DateUtils.toLocalDate(tariff.getValidFrom().value()));
        entity.setValidTo(
                tariff.getValidTo() != null ? DateUtils.toLocalDate(tariff.getValidTo().value()) : null
        );
        entity.setPrice(tariff.pricePerCubicMeter().getValue());
        entity.setStatus(tariff.getStatus());
        entity.setMinimumConsumption(tariff.getMinimumConsumption());

        return entity;
    }

    public static Tariff toDomain(TariffJpaEntity entity) {
        return Tariff.restore(
                new TariffId(entity.getId()),
                InvoicePeriod.of(DateUtils.toYearMonth(entity.getValidFrom())),
                entity.getValidTo() != null ? InvoicePeriod.of(DateUtils.toYearMonth(entity.getValidTo())) : null,
                new Money(entity.getPrice()),
                entity.getStatus(),
                entity.getMinimumConsumption()
        );
    }
}
