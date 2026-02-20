package com.kivora.JsWater.infrastructure.persistence.entity.client;

import com.kivora.JsWater.domain.model.client.ClientStatus;
import com.kivora.JsWater.domain.model.client.DocumentType;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "clients")
public class ClientJpaEntity {

    @Id
    @Column(name = "id", nullable = false, updatable = false)
    private UUID id;

    @Column(name = "full_name", nullable = false)
    private String fullName;

    @Enumerated(EnumType.STRING)
    @Column(name = "document_type", nullable = false)
    private DocumentType documentType;

    @Column(name = "document_number", nullable = false, unique = true)
    private String documentNumber;

    @Column(name = "phone_number", nullable = false)
    private String phoneNumber;

    // Campos de endereço
    @Column(name = "bairro")
    private String bairro;

    @Column(name = "localidade")
    private String localidade;

    @Column(name = "rua")
    private String rua;

    @Column(name = "referencia")
    private String referencia;

    // Estado do cliente (activo/inactivo)
    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private ClientStatus status;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    protected ClientJpaEntity() {

    }

    public ClientJpaEntity(
            UUID id,
            String fullName,
            DocumentType documentType,
            String documentNumber,
            String phoneNumber
    ) {
        this.id = id;
        this.fullName = fullName;
        this.documentType = documentType;
        this.documentNumber = documentNumber;
        this.phoneNumber = phoneNumber;
    }

    public UUID getId() {
        return id;
    }

    public String getFullName() {
        return fullName;
    }

    public DocumentType getDocumentType() {
        return documentType;
    }

    public String getDocumentNumber() {
        return documentNumber;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getBairro() {
        return bairro;
    }

    public void setBairro(String bairro) {
        this.bairro = bairro;
    }

    public String getLocalidade() {
        return localidade;
    }

    public void setLocalidade(String localidade) {
        this.localidade = localidade;
    }

    public String getRua() {
        return rua;
    }

    public void setRua(String rua) {
        this.rua = rua;
    }

    public String getReferencia() {
        return referencia;
    }

    public void setReferencia(String referencia) {
        this.referencia = referencia;
    }

    public ClientStatus getStatus() {
        return status;
    }

    public void setStatus(ClientStatus status) {
        this.status = status;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
