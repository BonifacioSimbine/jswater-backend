package com.kivora.JsWater.application.usecase.reading;

import com.kivora.JsWater.domain.exception.reading.ReadingNotFoundException;
import com.kivora.JsWater.domain.model.reading.Reading;
import com.kivora.JsWater.domain.repository.ReadingRepository;
import com.kivora.JsWater.domain.valueobject.reading.ReadingId;

public class GetReadingByIdUseCase {

    private final ReadingRepository readingRepository;

    public GetReadingByIdUseCase(ReadingRepository readingRepository) {
        this.readingRepository = readingRepository;
    }

    public Reading execute(String readingId) {
        ReadingId id = ReadingId.from(readingId);
        return readingRepository.findById(id)
                .orElseThrow(() -> new ReadingNotFoundException(id));
    }
}
