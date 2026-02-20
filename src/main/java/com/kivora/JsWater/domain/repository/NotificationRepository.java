package com.kivora.JsWater.domain.repository;

import com.kivora.JsWater.domain.model.notification.Notification;
import com.kivora.JsWater.domain.model.notification.NotificationCategory;
import com.kivora.JsWater.domain.model.notification.NotificationStatus;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface NotificationRepository {

    Notification save(Notification notification);

    Optional<Notification> findById(UUID id);

    List<Notification> findByUserId(UUID userId);

    List<Notification> findByUserIdAndStatus(UUID userId, NotificationStatus status);

    List<Notification> findByUserIdAndCategory(UUID userId, NotificationCategory category);

    long countUnreadByUserId(UUID userId);
}
