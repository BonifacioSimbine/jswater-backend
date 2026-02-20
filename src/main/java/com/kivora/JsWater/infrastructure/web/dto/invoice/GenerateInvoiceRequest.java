package com.kivora.JsWater.infrastructure.web.dto.invoice;

import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record GenerateInvoiceRequest(
        @NotNull UUID readingId
) {}
