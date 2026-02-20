package com.kivora.JsWater.domain.valueobject.client;

public class PhoneNumber {

    private final String  value;

    private static final String VALID_PATTERN = "^(84|85|86|87|82)\\d{7}$";

    public PhoneNumber(String value) {

        if (value == null || value.isEmpty()) {
            throw new IllegalArgumentException("O numero de telefone nao pode ser vazio");
        }

        String normalized = value.replaceAll("\\s+", "");

        if (!normalized.matches(VALID_PATTERN)) {
            throw new IllegalArgumentException(
                    "Numero de telefone Invalido, deve ter no minimo 9 digitos e iniciar com 84 0u 85 ou 82 ou 86 ou 87"
            );
        }

        this.value = value;

    }

    public String getValue() {
        return value;
    }
}
