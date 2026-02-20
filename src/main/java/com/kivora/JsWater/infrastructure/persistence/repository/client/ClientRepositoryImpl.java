package com.kivora.JsWater.infrastructure.persistence.repository.client;

import com.kivora.JsWater.domain.model.client.Client;
import com.kivora.JsWater.domain.model.client.ClientStatus;
import com.kivora.JsWater.domain.repository.ClientRepository;
import com.kivora.JsWater.domain.valueobject.client.ClientId;
import com.kivora.JsWater.infrastructure.persistence.mapper.client.ClientMapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class ClientRepositoryImpl implements ClientRepository {

    private final ClientJpaRepository jpaRepository;

    public ClientRepositoryImpl(ClientJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public void save(Client client) {
        jpaRepository.save(ClientMapper.toJpa(client));
    }

    @Override
    public Optional<Client> findById(ClientId clientId) {
        return jpaRepository.findById(clientId.getValue())
                .map(ClientMapper::toDomain);
    }

    @Override
    public void delete(ClientId clientId) {
        jpaRepository.deleteById(clientId.getValue());
    }

    @Override
    public boolean existsById(ClientId clientId) {
        return jpaRepository.existsById(clientId.getValue());
    }

    @Override
    public List<Client> findAll() {
        return jpaRepository.findAll().stream()
                .map(ClientMapper::toDomain)
                .toList();
    }

    @Override
    public List<Client> findByStatus(ClientStatus status) {
        return jpaRepository.findByStatus(status).stream()
                .map(ClientMapper::toDomain)
                .toList();
    }

    @Override
    public List<Client> findByBairro(String bairro) {
        return jpaRepository.findByBairro(bairro).stream()
                .map(ClientMapper::toDomain)
                .toList();
    }

    @Override
    public List<Client> findByStatusAndBairro(ClientStatus status, String bairro) {
        return jpaRepository.findByStatusAndBairro(status, bairro).stream()
                .map(ClientMapper::toDomain)
                .toList();
    }
}

