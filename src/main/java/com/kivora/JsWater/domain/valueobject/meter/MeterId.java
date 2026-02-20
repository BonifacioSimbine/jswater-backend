package com.kivora.JsWater.domain.valueobject.meter;

import java.util.UUID;

public record MeterId(UUID value) {
    public MeterId {
        if (value == null) {
            throw new IllegalArgumentException("MeterId não pode ser nulo");
        }
    }

    public static MeterId generate() {
        return new MeterId(UUID.randomUUID());
    }

    public UUID getValue() {
        return value;
    }
}
