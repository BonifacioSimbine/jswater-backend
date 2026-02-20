package com.kivora.JsWater.domain.exception.invoice;

import com.kivora.JsWater.domain.exception.DomainException;

import java.util.UUID;

public class InvoiceAlreadyGeneratedException extends DomainException {
    public InvoiceAlreadyGeneratedException(UUID readingId) {
        super("Já existe uma fatura gerada para a leitura: " + readingId);
    }
}
