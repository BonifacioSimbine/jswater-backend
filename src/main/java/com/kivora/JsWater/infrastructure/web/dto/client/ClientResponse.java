package com.kivora.JsWater.infrastructure.web.dto.client;

import com.kivora.JsWater.domain.model.client.Client;
import com.kivora.JsWater.domain.model.client.ClientStatus;

import java.util.UUID;

public record ClientResponse(
        UUID id,
        String fullName,
        String document,
        String phoneNumber,
        String bairro,
        String localidade,
        String rua,
        String referencia,
        ClientStatus status
) {
    public static ClientResponse from(Client client) {
        return new ClientResponse(
                client.getId().getValue(),
                client.getFullName().getValue(),
                client.getDocument().getValue(),
                client.getPhoneNumber().getValue(),
                client.getAddress() != null ? client.getAddress().getBairro() : null,
                client.getAddress() != null ? client.getAddress().getLocalidade() : null,
                client.getAddress() != null ? client.getAddress().getRua() : null,
                client.getAddress() != null ? client.getAddress().getReferencia() : null,
                client.getStatus()
        );
    }
}
