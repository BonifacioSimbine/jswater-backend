package com.kivora.JsWater.domain.valueobject.client;

import com.kivora.JsWater.domain.model.client.DocumentType;
import com.kivora.JsWater.domain.exception.FieldException;

import java.util.Objects;

public class Document {

    private final DocumentType type;
    private final String value;

    public Document(DocumentType type, String value) {
        if (type == null) {
            throw new FieldException("documentType", "O tipo de documento deve ser informado");

        }

        if (value == null || value.isEmpty()) {
            throw new FieldException("documentNumber", "O valor deve ser informado");
        }

        validate(type, value);

        this.type = type;
        this.value = value;
    }

    private void validate(DocumentType type, String value) {
        switch (type) {
            case BI -> {
                if (!value.matches("^\\d{12}[A-Z]$")) {
                    throw new FieldException("documentNumber", "BI inválido");
                }
            }
            case NUIT -> {
                if (!value.matches("^\\d{9}$")) {
                    throw new FieldException("documentNumber", "NUIT inválido");
                }
            }
            case PASSPORT -> {
                if (value.length() < 6) {
                    throw new FieldException("documentNumber", "Passaporte inválido");
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
