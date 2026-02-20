package com.kivora.JsWater.application.usecase.tariff;

import com.kivora.JsWater.domain.model.tariff.Tariff;
import com.kivora.JsWater.domain.repository.TariffRepository;

import java.util.List;

public class ListTariffsUseCase {

    private final TariffRepository tariffRepository;

    public ListTariffsUseCase(TariffRepository tariffRepository) {
        this.tariffRepository = tariffRepository;
    }

    public List<Tariff> execute() {
        return tariffRepository.findAll();
    }
}
