package com.kivora.JsWater.application.usecase.client;

import com.kivora.JsWater.domain.exception.client.ClientNotFoundException;
import com.kivora.JsWater.domain.model.client.Client;
import com.kivora.JsWater.domain.repository.ClientRepository;
import com.kivora.JsWater.domain.valueobject.client.ClientId;
import com.kivora.JsWater.domain.valueobject.client.FullName;
import com.kivora.JsWater.domain.valueobject.client.PhoneNumber;

import java.util.UUID;

public class UpdateClientUseCase {

    private final ClientRepository clientRepository;

    public UpdateClientUseCase(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    public Client execute(UUID id, String fullName, String phoneNumber) {
        ClientId clientId = ClientId.from(id.toString());
        
        Client client = clientRepository.findById(clientId)
                .orElseThrow(() -> new ClientNotFoundException(clientId));

        client.changeFullName(new FullName(fullName));
        client.changePhoneNumber(new PhoneNumber(phoneNumber));

        clientRepository.save(client);
        return client;
    }
}
