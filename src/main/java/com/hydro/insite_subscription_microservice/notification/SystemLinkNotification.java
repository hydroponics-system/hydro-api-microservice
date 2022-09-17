package com.hydro.insite_subscription_microservice.notification;

import javax.annotation.Nonnull;

import com.hydro.insite_subscription_microservice.client.domain.Notification;
import com.hydro.insite_subscription_microservice.client.domain.NotificationType;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Link Notification object for a system.
 * 
 * @author Sam Butler
 * @since September 16, 2022
 */
@Schema(description = "System Link Notification")
public class SystemLinkNotification extends Notification {
    @Schema(description = "The system uuid to link too.")
    @Nonnull
    private String uuid;

    @Schema(description = "The unique code to authenticate with.")
    private String code;

    @Schema(description = "The user id of the user requesting the link request.")
    private int userId;

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    @Override
    public NotificationType getBodyType() {
        return NotificationType.SYSTEM_LINK;
    }
}
