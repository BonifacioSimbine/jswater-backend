package com.kivora.JsWater.domain.valueobject.Tariff;

import java.util.UUID;

public record TariffId(UUID value) {

    public TariffId {
        if (value == null)
            throw new NullPointerException("TariffId is null");
    }

    public static TariffId generate() {
        return new TariffId(UUID.randomUUID());
    }

    public static TariffId from(String value)  {
        return new TariffId(UUID.fromString(value));
    }

    public static TariffId of(UUID value) {
        return new TariffId(value);
    }

    @Override
    public String toString() {
        return value.toString();
    }
}
