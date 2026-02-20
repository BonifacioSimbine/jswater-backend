package com.kivora.JsWater.domain.repository;

import com.kivora.JsWater.domain.model.invoice.Invoice;
import com.kivora.JsWater.domain.valueobject.invoice.InvoiceId;
import com.kivora.JsWater.domain.valueobject.reading.ReadingId;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface InvoiceRepository {

    void save(Invoice invoice);

    Optional<Invoice> findById(InvoiceId id);

    List<Invoice> findByClientId(UUID clientId);

    boolean existsByReadingId(ReadingId readingId);

    boolean existsByInvoiceId(InvoiceId invoiceId);

    List<Invoice> findAll();

}
