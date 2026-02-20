package com.kivora.JsWater.domain.valueobject.client;

import com.kivora.JsWater.domain.model.client.DocumentType;

import java.util.Objects;

public class Document {

    private final DocumentType type;
    private final String value;

    public Document(DocumentType type, String value) {
        if (type == null) {
            throw new IllegalArgumentException("O tipo de documento deve ser informado");
        }

        if (value == null || value.isEmpty()) {
            throw new IllegalArgumentException("O valor deve ser informado");
        }

        validate(type, value);

        this.type = type;
        this.value = value;
    }

    private void validate(DocumentType type, String value) {
        switch (type) {
            case BI -> {
                if (!value.matches("^\\d{12}[A-Z]$")) {
                    throw new IllegalArgumentException("BI Invalido");
                }
            }case NUIT -> {
                if (!value.matches("^\\d{9}")) {
                    throw new IllegalArgumentException("Nuit Invalido");
                }
            } case PASSPORT -> {
                if (value.length() < 6) {
                    throw new IllegalArgumentException("Pasport Invalido");
                }
            }
        }
    }

    public DocumentType getType() {
        return type;
    }
    public String getValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Document that)) return false;
        return type == that.type && value.equals(that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(type, value);
    }
}
