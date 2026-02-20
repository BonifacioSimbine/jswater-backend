package com.kivora.JsWater.domain.exception.reading;

import com.kivora.JsWater.domain.exception.DomainException;
import com.kivora.JsWater.domain.valueobject.reading.ReadingId;

public class ReadingNotFoundException extends DomainException {

    public ReadingNotFoundException(ReadingId readingId) {

        super("Leitura com este ID: " + readingId + "nao foi encontrada.");
    }
}
