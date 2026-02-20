package com.kivora.JsWater.domain.valueobject.client;

public class FullName {

    private String value;

    public FullName(String value) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("O nome nao pode ser nulo.");
        }
        this.value = value.trim();
    }

    public String getValue() {
        return value;
    }
}
