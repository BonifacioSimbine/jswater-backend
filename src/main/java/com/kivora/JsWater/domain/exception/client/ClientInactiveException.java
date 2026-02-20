package com.kivora.JsWater.domain.exception.client;

import com.kivora.JsWater.domain.exception.DomainException;
import com.kivora.JsWater.domain.valueobject.client.ClientId;

public class ClientInactiveException extends DomainException {

    public ClientInactiveException(ClientId clientId) {
        super("Cliente encontra-se desactivado: " + clientId);
    }
}
