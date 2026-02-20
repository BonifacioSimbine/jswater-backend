package com.kivora.JsWater.domain.exception.invoice;

import com.kivora.JsWater.domain.exception.DomainException;
import com.kivora.JsWater.domain.valueobject.invoice.InvoiceId;

public class InvoiceNotFoundException extends DomainException {

    public InvoiceNotFoundException(InvoiceId invoiceId) {
        super("Factura nao encontrada" + invoiceId);
    }
}
