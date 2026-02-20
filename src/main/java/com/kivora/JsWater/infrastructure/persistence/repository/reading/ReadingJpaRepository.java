package com.kivora.JsWater.infrastructure.persistence.repository.reading;

import com.kivora.JsWater.infrastructure.persistence.entity.reading.ReadingJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ReadingJpaRepository
        extends JpaRepository<ReadingJpaEntity, UUID> {

    Optional<ReadingJpaEntity> findTopByMeterIdOrderByReadingDateDesc(UUID meterId);

    List<ReadingJpaEntity> findByMeterId(UUID meterId);

    boolean existsByMeterIdAndReadingDateBetween(
            UUID meterId,
            LocalDate start,
            LocalDate end
    );
}

