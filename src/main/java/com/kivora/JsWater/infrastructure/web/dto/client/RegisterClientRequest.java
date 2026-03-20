package com.kivora.JsWater.infrastructure.web.dto.client;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record RegisterClientRequest(
    @NotBlank(message = "Nome completo é obrigatório")
    String fullName,
    
    @NotBlank(message = "Tipo de documento é obrigatório")
    String documentType,
    
    @NotBlank(message = "Número do documento é obrigatório")
    @Pattern(regexp = "^[0-9]{12}[A-Z]$", message = "BI inválido. Deve ter 13 dígitos e o último deve ser uma letra (ex: 123456789320S)")
    String documentNumber,
    
    @NotBlank(message = "Número de telefone é obrigatório")
    @Pattern(regexp = "^[0-9]{9}$", message = "Telefone inválido. Deve ter 9 dígitos (ex: 842345678)")
    String phoneNumber,
    
    String bairro,
    String localidade,
    String rua,
    String referencia
) { }