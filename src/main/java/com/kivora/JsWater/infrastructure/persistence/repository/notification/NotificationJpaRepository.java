package com.kivora.JsWater.infrastructure.persistence.repository.notification;

import com.kivora.JsWater.domain.model.notification.NotificationCategory;
import com.kivora.JsWater.domain.model.notification.NotificationStatus;
import com.kivora.JsWater.infrastructure.persistence.entity.notification.NotificationJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface NotificationJpaRepository extends JpaRepository<NotificationJpaEntity, UUID> {

    List<NotificationJpaEntity> findByUserId(UUID userId);

    List<NotificationJpaEntity> findByUserIdAndStatus(UUID userId, NotificationStatus status);

    List<NotificationJpaEntity> findByUserIdAndCategory(UUID userId, NotificationCategory category);

    long countByUserIdAndStatus(UUID userId, NotificationStatus status);
}
