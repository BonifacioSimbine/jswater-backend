package com.kivora.JsWater.application.usecase.client;

import com.kivora.JsWater.domain.model.client.Client;
import com.kivora.JsWater.domain.model.client.ClientStatus;
import com.kivora.JsWater.domain.repository.ClientRepository;

import java.util.List;

public class SearchClientsUseCase {

    private final ClientRepository clientRepository;

    public SearchClientsUseCase(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    public List<Client> execute(ClientStatus status, String bairro) {
        if (status != null && bairro != null && !bairro.isBlank()) {
            return clientRepository.findByStatusAndBairro(status, bairro);
        }
        if (status != null) {
            return clientRepository.findByStatus(status);
        }
        if (bairro != null && !bairro.isBlank()) {
            return clientRepository.findByBairro(bairro);
        }
        return clientRepository.findAll();
    }

    public List<Client> execute(ClientStatus status, String bairro, int page, int size) {
        List<Client> all = execute(status, bairro);
        if (size <= 0) {
            size = 20;
        }
        if (page < 0) {
            page = 0;
        }
        int fromIndex = page * size;
        if (fromIndex >= all.size()) {
            return java.util.List.of();
        }
        int toIndex = Math.min(fromIndex + size, all.size());
        return all.subList(fromIndex, toIndex);
    }
}
