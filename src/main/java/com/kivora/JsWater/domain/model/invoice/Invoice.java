package com.kivora.JsWater.domain.model.invoice;

import com.kivora.JsWater.domain.valueobject.client.ClientId;
import com.kivora.JsWater.domain.valueobject.consumption.Consumption;
import com.kivora.JsWater.domain.valueobject.invoice.InvoiceDueDate;
import com.kivora.JsWater.domain.valueobject.invoice.InvoiceId;
import com.kivora.JsWater.domain.valueobject.invoice.InvoicePeriod;
import com.kivora.JsWater.domain.valueobject.meter.MeterId;
import com.kivora.JsWater.domain.valueobject.money.Money;
import com.kivora.JsWater.domain.valueobject.reading.ReadingId;

import java.time.LocalDate;

public class Invoice {

    private final InvoiceId id;
    private final ClientId clientId;
    private final ReadingId readingId;
    private final MeterId meterId;
    private final InvoicePeriod period;
    private final InvoiceDueDate dueDate;
    private final Consumption consumption;
    private final Money totalAmount;
    private InvoiceStatus status;
    private final InvoiceType type;

    private Invoice(InvoiceId id,
                    ClientId clientId,
                    MeterId meterId,
                    ReadingId readingId,
                    InvoicePeriod period,
                    Consumption consumption,
                    Money totalAmount,
                    InvoiceType type) {

        this.id = id;
        this.clientId = clientId;
        this.meterId = meterId;
        this.readingId = readingId;
        this.period = period;
        this.dueDate = InvoiceDueDate.from(period);
        this.consumption = consumption;
        this.totalAmount = totalAmount;
        this.status = InvoiceStatus.OPEN;
        this.type = type;
    }



    public static Invoice create(ClientId clientId,
                                 MeterId meterId,
                                 ReadingId readingId,
                                 InvoicePeriod period,
                                 Consumption consumption,
                                 Money totalAmount) {

        return new Invoice(
                InvoiceId.generate(),
                clientId,
                meterId,
                readingId,
                period,
                consumption,
                totalAmount,
                InvoiceType.WATER
        );
    }

    public static Invoice createFine(ClientId clientId,
                                     Money totalAmount,
                                     InvoicePeriod period) {
        return new Invoice(
                InvoiceId.generate(),
                clientId,
                null, 
                null, 
                period,
                null, 
                totalAmount,
                InvoiceType.FINE
        );
    }

    public static Invoice restore(
            InvoiceId id,
            ClientId clientId,
            MeterId meterId,
            ReadingId readingId,
            InvoicePeriod period,
            Consumption consumption,
            Money totalAmount,
            InvoiceStatus status,
            InvoiceType type
    ) {
        Invoice invoice = new Invoice(id,
                clientId,
                meterId,
                readingId,
                period,
                consumption,
                totalAmount,
                type);
        invoice.status = status;
        return invoice;
    }






    public void markAsPaid() {
        this.status = InvoiceStatus.PAID;
    }

    public boolean isOverdue(LocalDate today) {
        return status != InvoiceStatus.PAID && dueDate.isOverdue(today);
    }

    public boolean isOpen() {
        return status == InvoiceStatus.OPEN;
    }



    public InvoiceId getId() {
        return id;
    }

    public InvoicePeriod getPeriod() {
        return period;
    }

    public Money getTotalAmount() {
        return totalAmount;
    }

    public ClientId getClientId() {
        return clientId;
    }

    public MeterId getMeterId() {
        return meterId;
    }

    public ReadingId getReadingId() {
        return readingId;
    }

    public InvoiceDueDate getDueDate() {
        return dueDate;
    }

    public Consumption getConsumption() {
        return consumption;
    }

    public InvoiceStatus getStatus() {
        return status;
    }

    public InvoiceType getType() {
        return type;
    }
}
