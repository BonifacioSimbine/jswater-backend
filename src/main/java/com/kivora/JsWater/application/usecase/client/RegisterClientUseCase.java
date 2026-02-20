package com.kivora.JsWater.application.usecase.client;

import com.kivora.JsWater.domain.model.client.Client;
import com.kivora.JsWater.domain.model.client.DocumentType;
import com.kivora.JsWater.domain.repository.ClientRepository;
import com.kivora.JsWater.domain.valueobject.client.ClientId;
import com.kivora.JsWater.domain.valueobject.client.Document;
import com.kivora.JsWater.domain.valueobject.client.FullName;
import com.kivora.JsWater.domain.valueobject.client.PhoneNumber;
import com.kivora.JsWater.domain.valueobject.Adress.Address;

public class RegisterClientUseCase {

    private final ClientRepository clientRepository;

    public RegisterClientUseCase(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    public Client execute(String fullName,
                          DocumentType documentType,
                          String documentNumber,
                          String phoneNumber,
                          String bairro,
                          String localidade,
                          String rua,
                          String referencia
                          ) {
        Address address = new Address(bairro, localidade, rua, referencia);

        Client client = Client.create(
                ClientId.generate(),
                new FullName(fullName),
                new Document(documentType, documentNumber),
                new PhoneNumber(phoneNumber),
                address
        );

        clientRepository.save(client);

        return client;
    }



}
