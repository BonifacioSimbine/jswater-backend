package com.kivora.JsWater.infrastructure.web.exception;


import com.kivora.JsWater.domain.exception.*;
import com.kivora.JsWater.domain.exception.AuthenticationException;
import com.kivora.JsWater.domain.exception.client.ClientNotFoundException;
import com.kivora.JsWater.domain.exception.invoice.InvoiceAlreadyExistsException;
import com.kivora.JsWater.domain.exception.invoice.InvoiceNotFoundException;
import com.kivora.JsWater.domain.exception.meter.MeterNotFoundException;
import com.kivora.JsWater.domain.exception.reading.ReadingAlreadyInvoicedException;
import com.kivora.JsWater.domain.exception.reading.ReadingNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(DomainException.class)
    public ResponseEntity<ApiError> handleDomainException(DomainException ex) {
        return ResponseEntity
                .status(HttpStatus.UNPROCESSABLE_ENTITY)
                .body(new ApiError(
                        ex.getMessage(),
                        Instant.now()
                ));
    }

    @ExceptionHandler({
            ClientNotFoundException.class,
            MeterNotFoundException.class,
            ReadingNotFoundException.class,
            InvoiceNotFoundException.class
    })
    public ResponseEntity<ApiError> handleNotFound(RuntimeException ex) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(new ApiError(ex.getMessage(), Instant.now()));
    }

    @ExceptionHandler({
            ReadingAlreadyInvoicedException.class,
            InvoiceAlreadyExistsException.class
    })
    public ResponseEntity<ApiError> handleConflict(RuntimeException ex) {
        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(new ApiError(ex.getMessage(), Instant.now()));
    }

        @ExceptionHandler(AuthenticationException.class)
        public ResponseEntity<ApiError> handleAuthentication(AuthenticationException ex) {
                return ResponseEntity
                                .status(HttpStatus.UNAUTHORIZED)
                                .body(new ApiError("Credenciais inválidas", Instant.now()));
        }
}

