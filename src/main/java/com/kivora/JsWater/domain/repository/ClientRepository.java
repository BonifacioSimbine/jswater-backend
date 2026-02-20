package com.kivora.JsWater.domain.repository;

import com.kivora.JsWater.domain.model.client.Client;
import com.kivora.JsWater.domain.model.client.ClientStatus;
import com.kivora.JsWater.domain.valueobject.client.ClientId;

import java.util.List;
import java.util.Optional;

public interface ClientRepository {

    void save(Client client);

    Optional<Client> findById(ClientId clientId);

    void delete(ClientId clientId);

    boolean existsById(ClientId clientId);

    List<Client> findAll();

    List<Client> findByStatus(ClientStatus status);

    List<Client> findByBairro(String bairro);

    List<Client> findByStatusAndBairro(ClientStatus status, String bairro);
}
