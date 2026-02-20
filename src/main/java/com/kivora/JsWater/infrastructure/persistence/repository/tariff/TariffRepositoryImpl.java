package com.kivora.JsWater.infrastructure.persistence.repository.tariff;


import com.kivora.JsWater.domain.model.tariff.Tariff;
import com.kivora.JsWater.domain.repository.TariffRepository;
import com.kivora.JsWater.domain.valueobject.invoice.InvoicePeriod;
import com.kivora.JsWater.infrastructure.persistence.mapper.tariff.TariffMapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public class TariffRepositoryImpl implements TariffRepository {

    private final TariffJpaRepository jpaRepository;

    public TariffRepositoryImpl(TariffJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public void save(Tariff tariff) {
        jpaRepository.save(TariffMapper.toJpa(tariff));
    }

    @Override
    public Optional<Tariff> findById(UUID id) {
        return jpaRepository.findById(id).map(TariffMapper::toDomain);
    }

    @Override
    public List<Tariff> findAll() {
        return jpaRepository.findAll().stream()
                .map(TariffMapper::toDomain)
                .toList();
    }





    @Override
    public Optional<Tariff> getActiveTariff() {
        // Busca o primeiro tarifa ativa no banco
        return jpaRepository.findAll().stream()
                .map(TariffMapper::toDomain)
                .filter(t -> t.getStatus() == com.kivora.JsWater.domain.model.tariff.TariffStatus.ACTIVE)
                .findFirst();
    }

    @Override
    public Optional<Tariff> findActiveFor(InvoicePeriod period) {
        // Busca o primeiro tarifa ativa que cobre o período fornecido
        return jpaRepository.findAll().stream()
                .map(TariffMapper::toDomain)
                .filter(t -> t.isActiveFor(period))
                .findFirst();
    }
}
