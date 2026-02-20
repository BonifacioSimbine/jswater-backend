package com.kivora.JsWater.infrastructure.web.dto.invoice;

import com.kivora.JsWater.domain.model.invoice.Invoice;
import com.kivora.JsWater.domain.model.invoice.InvoiceStatus;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.UUID;

public record InvoiceResponse(
        UUID id,
        UUID clientId,
        String clientName,
        UUID meterId,
        BigDecimal consumption,
        BigDecimal totalAmount,
        YearMonth period,
        LocalDate dueDate,
	String type,
	String status,
	String bairro
) {
    public static InvoiceResponse from(Invoice invoice, String clientName, String bairro) {
        String type = invoice.getType() != null ? invoice.getType().name() : "WATER";

        String status;
        InvoiceStatus invoiceStatus = invoice.getStatus();
        if (invoiceStatus == null) {
            status = "PENDING";
        } else {
            status = switch (invoiceStatus) {
                case OPEN -> "PENDING";
                case PAID -> "PAID";
                case CLOSED -> "CANCELED";
            };
        }

        return new InvoiceResponse(
                invoice.getId().getValue(),
                invoice.getClientId().getValue(),
                clientName,
                invoice.getMeterId() != null ? invoice.getMeterId().getValue() : null,
                invoice.getConsumption() != null ? invoice.getConsumption().getMetrodCubicos() : BigDecimal.ZERO,
                invoice.getTotalAmount().getValue(),
                invoice.getPeriod().value(),
                invoice.getDueDate().value(),
        type,
        status,
		bairro
        );
    }
}
