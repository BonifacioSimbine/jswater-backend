package com.kivora.JsWater.domain.exception.invoice;

import com.kivora.JsWater.domain.exception.DomainException;
import com.kivora.JsWater.domain.valueobject.invoice.InvoiceId;

public class InvoiceAlreadyPaidException extends DomainException {

    public InvoiceAlreadyPaidException(InvoiceId invoiceId) {
        super("Factura já está paga ou fechada: " + invoiceId);
    }
}
