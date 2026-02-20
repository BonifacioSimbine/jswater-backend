package com.kivora.JsWater.application.usecase.client;

import com.kivora.JsWater.domain.exception.client.ClientNotFoundException;
import com.kivora.JsWater.domain.model.client.Client;
import com.kivora.JsWater.domain.repository.ClientRepository;
import com.kivora.JsWater.domain.valueobject.Adress.Address;
import com.kivora.JsWater.domain.valueobject.client.ClientId;

import java.util.UUID;

public class UpdateClientAddressUseCase {

    private final ClientRepository clientRepository;

    public UpdateClientAddressUseCase(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    public Client execute(UUID clientId,
                          String bairro,
                          String localidade,
                          String rua,
                          String referencia) {
        ClientId id = new ClientId(clientId);

        Client client = clientRepository.findById(id)
                .orElseThrow(() -> new ClientNotFoundException(id));

        Address address = new Address(bairro, localidade, rua, referencia);
        client.changeAddress(address);

        clientRepository.save(client);

        return client;
    }
}
