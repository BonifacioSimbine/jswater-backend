package com.kivora.JsWater.application.usecase.notification;

import com.kivora.JsWater.domain.model.notification.Notification;
import com.kivora.JsWater.domain.model.notification.NotificationCategory;
import com.kivora.JsWater.domain.model.notification.NotificationType;
import com.kivora.JsWater.domain.repository.NotificationRepository;

import java.util.UUID;

public class CreateNotificationUseCase {

    private final NotificationRepository notificationRepository;

    public CreateNotificationUseCase(NotificationRepository notificationRepository) {
        this.notificationRepository = notificationRepository;
    }

    public Notification execute(UUID userId,
                                String title,
                                String message,
                                NotificationCategory category,
                                NotificationType type,
                                String metadata) {
        Notification notification = Notification.create(
                userId,
                title,
                message,
                category,
                type,
                metadata
        );
        return notificationRepository.save(notification);
    }
}
