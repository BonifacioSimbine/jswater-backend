package com.kivora.JsWater.domain.exception.reading;

import com.kivora.JsWater.domain.exception.DomainException;
import com.kivora.JsWater.domain.valueobject.reading.ReadingId;

public class ReadingAlreadyInvoicedException extends DomainException {
    public ReadingAlreadyInvoicedException(ReadingId readingId) {

        super("Esta Leitura ja tem uma Factura" + readingId);
    }
}
