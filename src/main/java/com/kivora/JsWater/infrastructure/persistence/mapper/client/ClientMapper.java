package com.kivora.JsWater.infrastructure.persistence.mapper.client;

import com.kivora.JsWater.domain.model.client.Client;
import com.kivora.JsWater.domain.model.client.ClientStatus;
import com.kivora.JsWater.domain.valueobject.client.ClientId;
import com.kivora.JsWater.domain.valueobject.client.Document;
import com.kivora.JsWater.domain.valueobject.client.FullName;
import com.kivora.JsWater.domain.valueobject.client.PhoneNumber;
import com.kivora.JsWater.domain.valueobject.Adress.Address;
import com.kivora.JsWater.infrastructure.persistence.entity.client.ClientJpaEntity;

public class ClientMapper {


        private ClientMapper() {}

                public static ClientJpaEntity toJpa(Client client) {
                        ClientJpaEntity entity = new ClientJpaEntity(
                                        client.getId().getValue(),
                                        client.getFullName().getValue(),
                                        client.getDocument().getType(),
                                        client.getDocument().getValue(),
                                        client.getPhoneNumber().getValue()
                        );

                        // Endereço
                        if (client.getAddress() != null) {
                                entity.setBairro(client.getAddress().getBairro());
                                entity.setLocalidade(client.getAddress().getLocalidade());
                                entity.setRua(client.getAddress().getRua());
                                entity.setReferencia(client.getAddress().getReferencia());
                        }

                        // Estado
                        entity.setStatus(client.getStatus());
                        entity.setCreatedAt(client.getCreatedAt());

                        return entity;
                }

                public static Client toDomain(ClientJpaEntity entity) {
                        Address address;
                        if (entity.getBairro() != null || entity.getLocalidade() != null || entity.getRua() != null) {
                                address = new Address(
                                                entity.getBairro() != null ? entity.getBairro() : "",
                                                entity.getLocalidade() != null ? entity.getLocalidade() : "",
                                                entity.getRua() != null ? entity.getRua() : "",
                                                entity.getReferencia() != null ? entity.getReferencia() : ""
                                );
                        } else {
                                address = Address.empty();
                        }

                        Client client = Client.create(
                                        new ClientId(entity.getId()),
                                        new FullName(entity.getFullName()),
                                        new Document(entity.getDocumentType(), entity.getDocumentNumber()),
                                        new PhoneNumber(entity.getPhoneNumber()),
                                        address
                        );

                        
                        ClientStatus status = entity.getStatus();
                        if (status == ClientStatus.INACTIVE) {
                                client.deactivate();
                        }

                        return client;
                }


}
