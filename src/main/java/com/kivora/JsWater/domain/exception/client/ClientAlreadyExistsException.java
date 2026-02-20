package com.kivora.JsWater.domain.exception.client;

import com.kivora.JsWater.domain.exception.DomainException;

public class ClientAlreadyExistsException extends DomainException {
    public ClientAlreadyExistsException(String document) {

        super("Cliente ja existe, com o documento " + document);
    }
}
