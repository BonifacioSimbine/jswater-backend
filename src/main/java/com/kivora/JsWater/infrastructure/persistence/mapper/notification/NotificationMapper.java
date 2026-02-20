package com.kivora.JsWater.infrastructure.persistence.mapper.notification;

import com.kivora.JsWater.domain.model.notification.Notification;
import com.kivora.JsWater.infrastructure.persistence.entity.notification.NotificationJpaEntity;

public class NotificationMapper {

    public static NotificationJpaEntity toJpaEntity(Notification notification) {
        NotificationJpaEntity entity = new NotificationJpaEntity();
        entity.setId(notification.getId());
        entity.setUserId(notification.getUserId());
        entity.setTitle(notification.getTitle());
        entity.setMessage(notification.getMessage());
        entity.setCategory(notification.getCategory());
        entity.setType(notification.getType());
        entity.setStatus(notification.getStatus());
        entity.setCreatedAt(notification.getCreatedAt());
        entity.setReadAt(notification.getReadAt());
        entity.setMetadata(notification.getMetadata());
        return entity;
    }

    public static Notification toDomain(NotificationJpaEntity entity) {
        return new Notification(
                entity.getId(),
                entity.getUserId(),
                entity.getTitle(),
                entity.getMessage(),
                entity.getCategory(),
                entity.getType(),
                entity.getStatus(),
                entity.getCreatedAt(),
                entity.getReadAt(),
                entity.getMetadata()
        );
    }
}
