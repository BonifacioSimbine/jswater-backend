package com.kivora.JsWater.domain.model.client;

import com.kivora.JsWater.domain.valueobject.client.ClientId;
import com.kivora.JsWater.domain.valueobject.client.FullName;
import com.kivora.JsWater.domain.valueobject.client.Document;
import com.kivora.JsWater.domain.valueobject.client.PhoneNumber;
import com.kivora.JsWater.domain.valueobject.Adress.Address;

import java.time.LocalDateTime;

public class Client {

    private final ClientId id;
    private FullName fullName;
    private Document document;
    private PhoneNumber phoneNumber;
    private Address address;
    private ClientStatus status;
    private final LocalDateTime createdAt;

    private Client(ClientId id,
                   FullName fullName,
                   Document document,
                   PhoneNumber phoneNumber,
                   Address address) {
        this.id = id;
        this.fullName = fullName;
        this.document = document;
        this.phoneNumber = phoneNumber;
        this.address = address;
        this.status = ClientStatus.ACTIVE;
        this.createdAt = LocalDateTime.now();
    }


    public static Client create(ClientId id,
                                FullName fullName,
                                Document document,
                                PhoneNumber phoneNumber,
                                Address address) {
        return new Client(id, fullName, document, phoneNumber, address);
    }


    public void changeAddress(Address address) {
        this.address = address;
    }

    public void changeFullName(FullName fullName) {
        this.fullName = fullName;
    }

    public void changePhoneNumber(PhoneNumber phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void deactivate() {
        this.status = ClientStatus.INACTIVE;
    }

    public void activate() {
        this.status = ClientStatus.ACTIVE;
    }

    public boolean isActive() {
        return status == ClientStatus.ACTIVE;
    }

    /* ===== Getters ===== */
    public ClientId getId() {
        return id;
    }

    public FullName getFullName() {
        return fullName;
    }

    public Document getDocument() {
        return document;
    }

    public PhoneNumber getPhoneNumber() {
        return phoneNumber;
    }

    public Address getAddress() {
        return address;
    }

    public ClientStatus getStatus() {
        return status;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}
