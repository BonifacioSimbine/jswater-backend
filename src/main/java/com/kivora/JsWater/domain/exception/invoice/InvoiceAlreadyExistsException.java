package com.kivora.JsWater.domain.exception.invoice;

import com.kivora.JsWater.domain.exception.DomainException;
import com.kivora.JsWater.domain.valueobject.reading.ReadingId;

public class InvoiceAlreadyExistsException extends DomainException {

    public InvoiceAlreadyExistsException(ReadingId readingId) {

        super("Factura ja existe para esta Leitura" +readingId);
    }
}
