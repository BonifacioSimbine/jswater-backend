package com.kivora.JsWater.domain.exception.reading;

import com.kivora.JsWater.domain.exception.DomainException;

import java.time.YearMonth;

public class ReadingAlreadyExistsException extends DomainException {
    public ReadingAlreadyExistsException(YearMonth month) {

        super("Já existe uma leitura registrada para o mês: " + month);
    }
}
