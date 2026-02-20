package com.kivora.JsWater.infrastructure.persistence.repository.invoice;

import com.kivora.JsWater.domain.model.invoice.Invoice;
import com.kivora.JsWater.domain.repository.InvoiceRepository;
import com.kivora.JsWater.domain.valueobject.invoice.InvoiceId;
import com.kivora.JsWater.domain.valueobject.reading.ReadingId;
import com.kivora.JsWater.infrastructure.persistence.mapper.invoice.InvoiceMapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public class InvoiceRepositoryImpl implements InvoiceRepository {
    private final InvoiceJpaRepository invoiceJpaRepository;

    public InvoiceRepositoryImpl(InvoiceJpaRepository invoiceJpaRepository) {
        this.invoiceJpaRepository = invoiceJpaRepository;
    }

    @Override
    public void save(Invoice invoice) {
        invoiceJpaRepository.save(InvoiceMapper.toJpaEntity(invoice));
    }

    @Override
    public boolean existsByReadingId(ReadingId readingId) {
        return invoiceJpaRepository.existsByReadingId(readingId.value());
    }

    public InvoiceJpaRepository getInvoiceJpaRepository() {
        return invoiceJpaRepository;
    }

    @Override
    public Optional<Invoice> findById(InvoiceId invoiceId) {
        return invoiceJpaRepository.findById(invoiceId.value())
                .map(InvoiceMapper::toDomain);
    }

    @Override
    public boolean existsByInvoiceId(InvoiceId invoiceId) {
        return invoiceJpaRepository.existsById(invoiceId.value());
    }

    @Override
    public List<Invoice> findByClientId(UUID clientId) {
        return invoiceJpaRepository.findByClientId(clientId)
                .stream()
                .map(InvoiceMapper::toDomain)
                .toList();
    }

    @Override
    public List<Invoice> findAll() {
        return invoiceJpaRepository.findAll()
                .stream()
                .map(InvoiceMapper::toDomain)
                .toList();
    }

}
