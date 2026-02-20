package com.kivora.JsWater.infrastructure.persistence.repository.client;

import com.kivora.JsWater.domain.model.client.ClientStatus;
import com.kivora.JsWater.infrastructure.persistence.entity.client.ClientJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ClientJpaRepository extends JpaRepository<ClientJpaEntity, UUID> {

    boolean existsById(UUID id);

    boolean existsByDocumentNumber(String documentNumber);

    java.util.List<ClientJpaEntity> findByStatus(ClientStatus status);

    java.util.List<ClientJpaEntity> findByBairro(String bairro);

    java.util.List<ClientJpaEntity> findByStatusAndBairro(ClientStatus status, String bairro);
}
