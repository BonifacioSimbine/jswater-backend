package com.kivora.JsWater.application.usecase.notification;

import com.kivora.JsWater.domain.model.notification.Notification;
import com.kivora.JsWater.domain.repository.NotificationRepository;

import java.util.UUID;

public class MarkNotificationAsReadUseCase {

    private final NotificationRepository notificationRepository;

    public MarkNotificationAsReadUseCase(NotificationRepository notificationRepository) {
        this.notificationRepository = notificationRepository;
    }

    public void execute(UUID id) {
        Notification notification = notificationRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Notification not found: " + id));
        notification.markAsRead();
        notificationRepository.save(notification);
    }
}
