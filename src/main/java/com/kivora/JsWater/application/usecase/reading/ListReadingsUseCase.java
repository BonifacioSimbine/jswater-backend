package com.kivora.JsWater.application.usecase.reading;

import com.kivora.JsWater.domain.model.reading.Reading;
import com.kivora.JsWater.domain.repository.ReadingRepository;

import java.util.List;

public class ListReadingsUseCase {

    private final ReadingRepository readingRepository;

    public ListReadingsUseCase(ReadingRepository readingRepository) {
        this.readingRepository = readingRepository;
    }

    public List<Reading> execute() {
        return readingRepository.findAll();
    }
}
