package com.kivora.JsWater.application.usecase.client;

import com.kivora.JsWater.domain.exception.client.ClientNotFoundException;
import com.kivora.JsWater.domain.model.client.Client;
import com.kivora.JsWater.domain.repository.ClientRepository;
import com.kivora.JsWater.domain.valueobject.client.ClientId;

public class GetClientByIdUseCase {

    private final ClientRepository clientRepository;

    public GetClientByIdUseCase(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    public Client execute(String clientId) {
        ClientId id = ClientId.from(clientId);
        return clientRepository.findById(id)
                .orElseThrow(() -> new ClientNotFoundException(id));
    }
}
