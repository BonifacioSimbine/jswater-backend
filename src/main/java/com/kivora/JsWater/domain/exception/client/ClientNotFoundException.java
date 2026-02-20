package com.kivora.JsWater.domain.exception.client;

import com.kivora.JsWater.domain.exception.DomainException;
import com.kivora.JsWater.domain.valueobject.client.ClientId;

import java.util.UUID;

public class ClientNotFoundException extends DomainException {

    public ClientNotFoundException(ClientId clientId) {
        super("Cliente nao encontrado" + clientId);
    }
}
