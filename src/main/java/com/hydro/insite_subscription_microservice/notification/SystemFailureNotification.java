package com.hydro.insite_subscription_microservice.notification;

import java.time.LocalDateTime;

import com.hydro.insite_subscription_microservice.client.domain.Notification;
import com.hydro.insite_subscription_microservice.client.domain.NotificationType;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * System failure susbcription notification.
 * 
 * @author Sam Butler
 * @since July 28, 2022
 */
@Schema(description = "System Failure Subscription for notifications")
public class SystemFailureNotification implements Notification {
    @Schema(description = "Message information about the system failure.")
    private String message;

    @Schema(description = "When the failure occurred.")
    private LocalDateTime created;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public LocalDateTime getCreated() {
        return created;
    }

    public void setCreated(LocalDateTime created) {
        this.created = created;
    }

    @Override
    public NotificationType getType() {
        return NotificationType.SYSTEM_FAILURE;
    }
}
