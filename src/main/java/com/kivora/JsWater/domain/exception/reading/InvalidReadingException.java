package com.kivora.JsWater.domain.exception.reading;

import com.kivora.JsWater.domain.exception.DomainException;

public class InvalidReadingException extends DomainException {
    public InvalidReadingException(int current, int previous) {

        super(
                "Leitura inválida. A leitura atual (" + current +
                        ") não pode ser menor que a anterior (" + previous + ")."
        );
    }
}
