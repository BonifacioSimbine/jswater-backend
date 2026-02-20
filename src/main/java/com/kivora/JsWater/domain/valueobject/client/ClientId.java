package com.kivora.JsWater.domain.valueobject.client;

import java.util.UUID;

public record ClientId(UUID value) {

    public ClientId {

        if (value == null) throw new NullPointerException("value is null");
    }

    public static ClientId generate() {
        return new ClientId(UUID.randomUUID());
    }

    public static ClientId from(String value) {
        return new ClientId(UUID.fromString(value));
    }

    @Override
    public String toString() {
        return value.toString();
    }

    public UUID getValue() {
        return value;
    }
}
