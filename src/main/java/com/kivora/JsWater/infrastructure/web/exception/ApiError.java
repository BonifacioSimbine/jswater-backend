package com.kivora.JsWater.infrastructure.web.exception;

import java.time.Instant;

public record ApiError(
        String message,
        Instant timestamp
) {}
