package com.kivora.JsWater.domain.model.notification;

import java.time.Instant;
import java.util.UUID;

public class Notification {

    private final UUID id;
    private final UUID userId;
    private final String title;
    private final String message;
    private final NotificationCategory category;
    private final NotificationType type;
    private NotificationStatus status;
    private final Instant createdAt;
    private Instant readAt;
    private final String metadata;

    public Notification(UUID id,
                        UUID userId,
                        String title,
                        String message,
                        NotificationCategory category,
                        NotificationType type,
                        NotificationStatus status,
                        Instant createdAt,
                        Instant readAt,
                        String metadata) {
        this.id = id;
        this.userId = userId;
        this.title = title;
        this.message = message;
        this.category = category;
        this.type = type;
        this.status = status;
        this.createdAt = createdAt;
        this.readAt = readAt;
        this.metadata = metadata;
    }

    public static Notification create(UUID userId,
                                      String title,
                                      String message,
                                      NotificationCategory category,
                                      NotificationType type,
                                      String metadata) {
        Instant now = Instant.now();
        return new Notification(
                UUID.randomUUID(),
                userId,
                title,
                message,
                category,
                type,
                NotificationStatus.UNREAD,
                now,
                null,
                metadata
        );
    }

    public void markAsRead() {
        if (this.status == NotificationStatus.READ) {
            return;
        }
        this.status = NotificationStatus.READ;
        this.readAt = Instant.now();
    }

    public UUID getId() {
        return id;
    }

    public UUID getUserId() {
        return userId;
    }

    public String getTitle() {
        return title;
    }

    public String getMessage() {
        return message;
    }

    public NotificationCategory getCategory() {
        return category;
    }

    public NotificationType getType() {
        return type;
    }

    public NotificationStatus getStatus() {
        return status;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Instant getReadAt() {
        return readAt;
    }

    public String getMetadata() {
        return metadata;
    }
}
