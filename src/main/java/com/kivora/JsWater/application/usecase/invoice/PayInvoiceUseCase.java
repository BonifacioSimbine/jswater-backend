package com.kivora.JsWater.application.usecase.invoice;

import com.kivora.JsWater.domain.exception.invoice.InvoiceAlreadyPaidException;
import com.kivora.JsWater.domain.exception.invoice.InvoiceNotFoundException;
import com.kivora.JsWater.domain.model.invoice.Invoice;
import com.kivora.JsWater.domain.repository.InvoiceRepository;
import com.kivora.JsWater.domain.valueobject.invoice.InvoiceId;
import com.kivora.JsWater.domain.valueobject.invoice.InvoicePeriod;
import com.kivora.JsWater.domain.valueobject.money.Money;

import java.math.BigDecimal;
import java.time.LocalDate;

public class PayInvoiceUseCase {

    private final InvoiceRepository invoiceRepository;
    private static final BigDecimal LATE_FEE_PERCENTAGE = new BigDecimal("0.15"); // 15% de multa

    public PayInvoiceUseCase(InvoiceRepository invoiceRepository) {
        this.invoiceRepository = invoiceRepository;
    }

    public Invoice execute(String invoiceId) {
        InvoiceId id = InvoiceId.from(invoiceId);

        Invoice invoice = invoiceRepository.findById(id)
                .orElseThrow(() -> new InvoiceNotFoundException(id));

        if (!invoice.isOpen()) {
            throw new InvoiceAlreadyPaidException(id);
        }

        // Verifica se esta atrasado para aplicar multa
        if (invoice.isOverdue(LocalDate.now())) {
            applyLateFee(invoice);
        }

        invoice.markAsPaid();
        invoiceRepository.save(invoice);

        return invoice;
    }

    private void applyLateFee(Invoice originalInvoice) {
        BigDecimal fineAmount = originalInvoice.getTotalAmount().getValue().multiply(LATE_FEE_PERCENTAGE);
        
        Invoice fine = Invoice.createFine(
                originalInvoice.getClientId(),
                new Money(fineAmount),
                InvoicePeriod.current()
        );
        
        // Assumimos que o cliente paga a multa no momento
        fine.markAsPaid();
        invoiceRepository.save(fine);
    }
}
