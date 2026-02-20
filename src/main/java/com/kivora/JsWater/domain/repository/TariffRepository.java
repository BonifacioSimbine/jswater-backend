package com.kivora.JsWater.domain.repository;

import com.kivora.JsWater.domain.model.tariff.Tariff;
import com.kivora.JsWater.domain.valueobject.invoice.InvoicePeriod;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface TariffRepository {

    Optional<Tariff> getActiveTariff();

    Optional<Tariff> findActiveFor(InvoicePeriod period);

    void save(Tariff tariff);

    Optional<Tariff> findById(UUID id);

    List<Tariff> findAll();






}
