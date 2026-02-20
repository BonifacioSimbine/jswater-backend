package com.kivora.JsWater.infrastructure.persistence.repository;

import com.kivora.JsWater.domain.model.expense.Expense;
import com.kivora.JsWater.domain.repository.ExpenseRepository;
import com.kivora.JsWater.infrastructure.persistence.entity.ExpenseEntity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Repository
public class ExpenseRepositoryJpa implements ExpenseRepository {
    @PersistenceContext
    private EntityManager em;

    private Expense toDomain(ExpenseEntity entity) {
        return new Expense(
                entity.getId(),
                entity.getAmount(),
                entity.getDate(),
                entity.getDescription(),
                entity.getCategory(),
                entity.getResponsible()
        );
    }

    private ExpenseEntity toEntity(Expense expense) {
        ExpenseEntity entity = new ExpenseEntity();
        entity.setId(expense.getId());
        entity.setAmount(expense.getAmount());
        entity.setDate(expense.getDate());
        entity.setDescription(expense.getDescription());
        entity.setCategory(expense.getCategory());
        entity.setResponsible(expense.getResponsible());
        return entity;
    }

    @Override
    @Transactional
    public Expense save(Expense expense) {
        ExpenseEntity entity = toEntity(expense);
        if (entity.getId() == null) {
            em.persist(entity);
        } else {
            entity = em.merge(entity);
        }
        return toDomain(entity);
    }

    @Override
    public Optional<Expense> findById(UUID id) {
        ExpenseEntity entity = em.find(ExpenseEntity.class, id);
        return entity != null ? Optional.of(toDomain(entity)) : Optional.empty();
    }

    @Override
    public List<Expense> findAll() {
        return em.createQuery("SELECT e FROM ExpenseEntity e", ExpenseEntity.class)
                .getResultList().stream().map(this::toDomain).collect(Collectors.toList());
    }

    @Override
    public List<Expense> findByDateBetween(LocalDate start, LocalDate end) {
        return em.createQuery("SELECT e FROM ExpenseEntity e WHERE e.date BETWEEN :start AND :end", ExpenseEntity.class)
                .setParameter("start", start)
                .setParameter("end", end)
                .getResultList().stream().map(this::toDomain).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void deleteById(UUID id) {
        ExpenseEntity entity = em.find(ExpenseEntity.class, id);
        if (entity != null) {
            em.remove(entity);
        }
    }
}
