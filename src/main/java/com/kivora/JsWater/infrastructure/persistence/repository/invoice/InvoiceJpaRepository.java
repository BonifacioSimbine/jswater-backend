package com.kivora.JsWater.infrastructure.persistence.repository.invoice;

import com.kivora.JsWater.infrastructure.persistence.entity.Invoice.InvoiceJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface InvoiceJpaRepository extends JpaRepository<InvoiceJpaEntity, UUID> {

    boolean existsByReadingId(UUID readingId);

    List<InvoiceJpaEntity> findByClientId(UUID clientId);


}
