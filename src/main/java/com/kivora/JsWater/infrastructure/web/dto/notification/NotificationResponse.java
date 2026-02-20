package com.kivora.JsWater.infrastructure.web.dto.notification;

import com.kivora.JsWater.domain.model.notification.Notification;
import com.kivora.JsWater.domain.model.notification.NotificationCategory;
import com.kivora.JsWater.domain.model.notification.NotificationStatus;
import com.kivora.JsWater.domain.model.notification.NotificationType;

import java.time.Instant;
import java.util.UUID;

public record NotificationResponse(
        UUID id,
        UUID userId,
        String title,
        String message,
        NotificationCategory category,
        NotificationType type,
        NotificationStatus status,
        Instant createdAt,
        Instant readAt,
        String metadata
) {

    public static NotificationResponse from(Notification notification) {
        return new NotificationResponse(
                notification.getId(),
                notification.getUserId(),
                notification.getTitle(),
                notification.getMessage(),
                notification.getCategory(),
                notification.getType(),
                notification.getStatus(),
                notification.getCreatedAt(),
                notification.getReadAt(),
                notification.getMetadata()
        );
    }
}
