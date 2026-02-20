package com.kivora.JsWater.application.usecase.notification;

import com.kivora.JsWater.domain.repository.NotificationRepository;

import java.util.UUID;

public class GetUnreadNotificationCountUseCase {

    private final NotificationRepository notificationRepository;

    public GetUnreadNotificationCountUseCase(NotificationRepository notificationRepository) {
        this.notificationRepository = notificationRepository;
    }

    public long execute(UUID userId) {
        return notificationRepository.countUnreadByUserId(userId);
    }
}
