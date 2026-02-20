package com.kivora.JsWater.infrastructure.persistence.mapper.invoice;

import com.kivora.JsWater.domain.model.invoice.Invoice;
import com.kivora.JsWater.domain.valueobject.client.ClientId;
import com.kivora.JsWater.domain.valueobject.consumption.Consumption;
import com.kivora.JsWater.domain.valueobject.invoice.InvoiceId;
import com.kivora.JsWater.domain.valueobject.invoice.InvoicePeriod;
import com.kivora.JsWater.domain.valueobject.meter.MeterId;
import com.kivora.JsWater.domain.valueobject.money.Money;
import com.kivora.JsWater.domain.valueobject.reading.ReadingId;
import com.kivora.JsWater.infrastructure.persistence.entity.Invoice.InvoiceJpaEntity;

public class InvoiceMapper {

    public static InvoiceJpaEntity toJpaEntity(Invoice invoice) {
        InvoiceJpaEntity invoiceJpaEntity = new InvoiceJpaEntity();

        invoiceJpaEntity.setId(invoice.getId().getValue());
        invoiceJpaEntity.setClientId(invoice.getClientId().value());
        
        if (invoice.getMeterId() != null) {
            invoiceJpaEntity.setMeterId(invoice.getMeterId().value());
        }
        if (invoice.getReadingId() != null) {
            invoiceJpaEntity.setReadingId(invoice.getReadingId().value());
        }
        if (invoice.getConsumption() != null) {
            invoiceJpaEntity.setConsumption(invoice.getConsumption().getMetrodCubicos());
        }
        
        invoiceJpaEntity.setTotalAmount(invoice.getTotalAmount().getValue());
        invoiceJpaEntity.setPeriod(invoice.getPeriod().value().atDay(1));
        invoiceJpaEntity.setStatus(invoice.getStatus());
        invoiceJpaEntity.setType(invoice.getType());

        return invoiceJpaEntity;
    }

    public static Invoice toDomain(InvoiceJpaEntity invoiceJpaEntity) {
        return Invoice.restore(
                new InvoiceId(invoiceJpaEntity.getId()),
                new ClientId(invoiceJpaEntity.getClientId()),
                invoiceJpaEntity.getMeterId() != null ? new MeterId(invoiceJpaEntity.getMeterId()) : null,
                invoiceJpaEntity.getReadingId() != null ? new ReadingId(invoiceJpaEntity.getReadingId()) : null,
                InvoicePeriod.from(invoiceJpaEntity.getPeriod()),
                invoiceJpaEntity.getConsumption() != null ? new Consumption(invoiceJpaEntity.getConsumption()) : null,
                new Money(invoiceJpaEntity.getTotalAmount()),
                invoiceJpaEntity.getStatus(),
                invoiceJpaEntity.getType() != null ? invoiceJpaEntity.getType() : com.kivora.JsWater.domain.model.invoice.InvoiceType.WATER
        );
    }
}
