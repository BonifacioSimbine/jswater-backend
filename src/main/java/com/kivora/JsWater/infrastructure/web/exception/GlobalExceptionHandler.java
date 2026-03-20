package com.kivora.JsWater.infrastructure.web.exception;

import com.kivora.JsWater.domain.exception.*;
import com.kivora.JsWater.domain.exception.AuthenticationException;
import com.kivora.JsWater.domain.exception.client.ClientNotFoundException;
import com.kivora.JsWater.domain.exception.invoice.InvoiceAlreadyExistsException;
import com.kivora.JsWater.domain.exception.invoice.InvoiceNotFoundException;
import com.kivora.JsWater.domain.exception.meter.MeterNotFoundException;
import com.kivora.JsWater.domain.exception.reading.ReadingAlreadyInvoicedException;
import com.kivora.JsWater.domain.exception.reading.ReadingNotFoundException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.validation.FieldError;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(FieldException.class)
    public ResponseEntity<ApiError> handleFieldException(FieldException ex) {
        return ResponseEntity
                .status(HttpStatus.UNPROCESSABLE_ENTITY)
                .body(new ApiError(ex.getMessage(), Instant.now()));
    }

    @ExceptionHandler(org.springframework.security.access.AccessDeniedException.class)
    public ResponseEntity<ApiError> handleAccessDenied(org.springframework.security.access.AccessDeniedException ex) {
        return ResponseEntity
                .status(HttpStatus.FORBIDDEN)
                .body(new ApiError("Você não tem permissão para realizar esta operação.", Instant.now()));
    }

    // Handler para violações de integridade de dados (duplicatas, etc)
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ApiError> handleDataIntegrityViolation(DataIntegrityViolationException ex) {
        String message = "Erro de integridade de dados.";
        
        // Verifica se é violação de chave única
        if (ex.getMessage() != null) {
            if (ex.getMessage().contains("duplicate key value") || ex.getMessage().contains("ukitro471543x0p5ndpgkv7j2ft")) {
                message = "Já existe uma leitura registada para este contador na data de hoje. Não é possível registar uma nova leitura para o mesmo dia.";
            } else if (ex.getMessage().contains("duplicate key") && ex.getMessage().contains("meter")) {
                message = "Já existe um registo com esta informação. Verifique os dados e tente novamente.";
            } else if (ex.getMessage().contains("violates foreign key constraint")) {
                message = "Referência inválida. O contador ou cliente informado não existe.";
            }
        }
        
        return ResponseEntity
                .status(HttpStatus.CONFLICT) // 409 Conflict
                .body(new ApiError(message, Instant.now()));
    }

    @ExceptionHandler({
            ReadingAlreadyInvoicedException.class,
            InvoiceAlreadyExistsException.class
    })
    public ResponseEntity<ApiError> handleConflict(RuntimeException ex) {
        String message;
        if (ex instanceof ReadingAlreadyInvoicedException) {
            message = "Esta leitura já foi faturada. Não é possível faturar novamente.";
        } else if (ex instanceof InvoiceAlreadyExistsException) {
            message = "Este cliente já possui uma fatura para o mês corrente. Não é possível criar uma nova fatura.";
        } else {
            message = ex.getMessage();
        }
        
        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(new ApiError(message, Instant.now()));
    }

    @ExceptionHandler(DomainException.class)
    public ResponseEntity<ApiError> handleDomainException(DomainException ex) {
        return ResponseEntity
                .status(HttpStatus.UNPROCESSABLE_ENTITY)
                .body(new ApiError(ex.getMessage(), Instant.now()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiError> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        
        String message = errors.values().iterator().next();
        return ResponseEntity
                .status(HttpStatus.UNPROCESSABLE_ENTITY)
                .body(new ApiError(message, Instant.now()));
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

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ApiError> handleAuthentication(AuthenticationException ex) {
        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(new ApiError(
                        ex.getMessage() != null ? ex.getMessage() : "Credenciais inválidas",
                        Instant.now()
                ));
    }

    @ExceptionHandler(org.springframework.web.server.ResponseStatusException.class)
    public ResponseEntity<ApiError> handleResponseStatusException(ResponseStatusException ex) {
        return ResponseEntity
                .status(ex.getStatusCode())
                .body(new ApiError(
                        ex.getReason() != null ? ex.getReason() : "Ocorreu um erro, provavelmente algum erro de validação ou na requisição em si",
                        Instant.now()
                ));
    }
}