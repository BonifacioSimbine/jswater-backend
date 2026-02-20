package com.kivora.JsWater.application.usecase.reading;

import com.kivora.JsWater.domain.exception.reading.ReadingNotFoundException;
import com.kivora.JsWater.domain.repository.ReadingRepository;
import com.kivora.JsWater.domain.valueobject.reading.ReadingId;

import java.util.UUID;

public class DeleteReadingUseCase {

    private final ReadingRepository readingRepository;

    public DeleteReadingUseCase(ReadingRepository readingRepository) {
        this.readingRepository = readingRepository;
    }

    public void execute(UUID id) {
        ReadingId readingId = ReadingId.from(id.toString());

        if (!readingRepository.existsById(readingId)) {
            throw new ReadingNotFoundException(readingId);
        }

        readingRepository.delete(readingId);
    }
}
