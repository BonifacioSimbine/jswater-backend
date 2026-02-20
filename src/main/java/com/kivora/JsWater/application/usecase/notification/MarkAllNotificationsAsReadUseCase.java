package com.kivora.JsWater.application.usecase.notification;

import com.kivora.JsWater.domain.model.notification.Notification;
import com.kivora.JsWater.domain.model.notification.NotificationStatus;
import com.kivora.JsWater.domain.repository.NotificationRepository;

import java.util.List;
import java.util.UUID;

public class MarkAllNotificationsAsReadUseCase {

    private final NotificationRepository notificationRepository;

    public MarkAllNotificationsAsReadUseCase(NotificationRepository notificationRepository) {
        this.notificationRepository = notificationRepository;
    }

    public void execute(UUID userId) {
        List<Notification> notifications = notificationRepository.findByUserIdAndStatus(userId, NotificationStatus.UNREAD);
        for (Notification notification : notifications) {
            notification.markAsRead();
            notificationRepository.save(notification);
        }
    }
}
