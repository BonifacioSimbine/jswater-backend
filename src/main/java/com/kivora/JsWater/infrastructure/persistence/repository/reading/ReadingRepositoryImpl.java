package com.kivora.JsWater.infrastructure.persistence.repository.reading;

import com.kivora.JsWater.domain.model.reading.Reading;
import com.kivora.JsWater.domain.repository.ReadingRepository;
import com.kivora.JsWater.domain.valueobject.invoice.InvoicePeriod;
import com.kivora.JsWater.domain.valueobject.meter.MeterId;
import com.kivora.JsWater.domain.valueobject.reading.ReadingId;
import com.kivora.JsWater.infrastructure.persistence.mapper.reading.ReadingMapper;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public class ReadingRepositoryImpl implements ReadingRepository {

    private final ReadingJpaRepository jpaRepository;

    public ReadingRepositoryImpl(ReadingJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public boolean existsForPeriod(MeterId meterId, InvoicePeriod period) {

        LocalDate start = period.value().atDay(1);
        LocalDate end   = period.value().atEndOfMonth();

        return jpaRepository.existsByMeterIdAndReadingDateBetween(
                meterId.value(),
                start,
                end
        );
    }

    @Override
    public void save(Reading reading) {
        jpaRepository.save(ReadingMapper.toJpa(reading));
    }

    @Override
    public Optional<Reading> findById(ReadingId readingId) {
        return jpaRepository.findById(readingId.value())
                .map(ReadingMapper::toDomain);
    }

    @Override
    public Optional<Reading> findLastByMeterId(MeterId meterId) {
        return jpaRepository
                .findTopByMeterIdOrderByReadingDateDesc(meterId.value())
                .map(ReadingMapper::toDomain);
    }

    @Override
    public List<Reading> findByMeterId(MeterId meterId) {
        return jpaRepository.findByMeterId(meterId.value())
                .stream()
                .map(ReadingMapper::toDomain)
                .toList();
    }

    @Override
    public List<Reading> findAll() {
        return jpaRepository.findAll().stream()
                .map(ReadingMapper::toDomain)
                .toList();
    }

    @Override
    public void delete(ReadingId readingId) {
        jpaRepository.deleteById(readingId.value());
    }

    @Override
    public boolean existsById(ReadingId readingId) {
        return jpaRepository.existsById(readingId.value());
    }
}
