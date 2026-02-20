package com.kivora.JsWater.application.usecase.client;

import com.kivora.JsWater.domain.exception.client.ClientNotFoundException;
import com.kivora.JsWater.domain.model.client.Client;
import com.kivora.JsWater.domain.model.client.ClientStatus;
import com.kivora.JsWater.domain.repository.ClientRepository;
import com.kivora.JsWater.domain.valueobject.client.ClientId;

import java.util.UUID;

public class ChangeClientStatusUseCase {

    private final ClientRepository clientRepository;

    public ChangeClientStatusUseCase(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    public Client execute(UUID clientId, ClientStatus newStatus) {
        ClientId id = new ClientId(clientId);

        Client client = clientRepository.findById(id)
                .orElseThrow(() -> new ClientNotFoundException(id));

        if (newStatus == ClientStatus.ACTIVE) {
            client.activate();
        } else {
            client.deactivate();
        }

        clientRepository.save(client);

        return client;
    }
}
