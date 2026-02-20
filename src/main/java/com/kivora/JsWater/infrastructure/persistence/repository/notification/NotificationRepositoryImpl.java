package com.kivora.JsWater.infrastructure.persistence.repository.notification;

import com.kivora.JsWater.domain.model.notification.Notification;
import com.kivora.JsWater.domain.model.notification.NotificationCategory;
import com.kivora.JsWater.domain.model.notification.NotificationStatus;
import com.kivora.JsWater.domain.repository.NotificationRepository;
import com.kivora.JsWater.infrastructure.persistence.entity.notification.NotificationJpaEntity;
import com.kivora.JsWater.infrastructure.persistence.mapper.notification.NotificationMapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public class NotificationRepositoryImpl implements NotificationRepository {

    private final NotificationJpaRepository jpaRepository;

    public NotificationRepositoryImpl(NotificationJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public Notification save(Notification notification) {
        NotificationJpaEntity entity = NotificationMapper.toJpaEntity(notification);
        NotificationJpaEntity saved = jpaRepository.save(entity);
        return NotificationMapper.toDomain(saved);
    }

    @Override
    public Optional<Notification> findById(UUID id) {
        return jpaRepository.findById(id).map(NotificationMapper::toDomain);
    }

    @Override
    public List<Notification> findByUserId(UUID userId) {
        return jpaRepository.findByUserId(userId).stream()
                .map(NotificationMapper::toDomain)
                .toList();
    }

    @Override
    public List<Notification> findByUserIdAndStatus(UUID userId, NotificationStatus status) {
        return jpaRepository.findByUserIdAndStatus(userId, status).stream()
                .map(NotificationMapper::toDomain)
                .toList();
    }

    @Override
    public List<Notification> findByUserIdAndCategory(UUID userId, NotificationCategory category) {
        return jpaRepository.findByUserIdAndCategory(userId, category).stream()
                .map(NotificationMapper::toDomain)
                .toList();
    }

    @Override
    public long countUnreadByUserId(UUID userId) {
        return jpaRepository.countByUserIdAndStatus(userId, NotificationStatus.UNREAD);
    }
}
