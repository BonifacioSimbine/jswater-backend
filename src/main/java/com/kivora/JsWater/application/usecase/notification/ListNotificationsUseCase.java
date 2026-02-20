package com.kivora.JsWater.application.usecase.notification;

import com.kivora.JsWater.domain.model.notification.Notification;
import com.kivora.JsWater.domain.model.notification.NotificationCategory;
import com.kivora.JsWater.domain.model.notification.NotificationStatus;
import com.kivora.JsWater.domain.repository.NotificationRepository;

import java.util.List;
import java.util.UUID;

public class ListNotificationsUseCase {

    private final NotificationRepository notificationRepository;

    public ListNotificationsUseCase(NotificationRepository notificationRepository) {
        this.notificationRepository = notificationRepository;
    }

    public List<Notification> execute(UUID userId, NotificationStatus status, NotificationCategory category) {
        if (status != null) {
            return notificationRepository.findByUserIdAndStatus(userId, status);
        }
        if (category != null) {
            return notificationRepository.findByUserIdAndCategory(userId, category);
        }
        return notificationRepository.findByUserId(userId);
    }
}
