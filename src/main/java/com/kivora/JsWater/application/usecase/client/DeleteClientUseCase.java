package com.kivora.JsWater.application.usecase.client;

import com.kivora.JsWater.domain.exception.client.ClientNotFoundException;
import com.kivora.JsWater.domain.repository.ClientRepository;
import com.kivora.JsWater.domain.valueobject.client.ClientId;

import java.util.UUID;

public class DeleteClientUseCase {

    private final ClientRepository clientRepository;

    public DeleteClientUseCase(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    public void execute(UUID id) {
        ClientId clientId = ClientId.from(id.toString());

        if (!clientRepository.existsById(clientId)) {
            throw new ClientNotFoundException(clientId);
        }

        clientRepository.delete(clientId);
    }
}
