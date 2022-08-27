package com.hydro.insite_subscription_microservice.client.domain;

import com.hydro.common.dictionary.enums.TextEnum;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Notification Socket types to send a notification too.
 *
 * @author Sam Butler
 * @since March 24, 2022
 */
@Schema(description = "Notification Socket Paths")
public enum NotificationSocket implements TextEnum {
    USER("USER" , "/queue/user/notification"),
    GENERAL("GENERAL" , "/topic/general/notification");

    private String textId;
    private String path;

    private NotificationSocket(String textId, String path) {
        this.textId = textId;
        this.path = path;
    }

    @Override
    public String getTextId() {
        return textId;
    }

    /**
     * Gets the socket URL path for the type.
     * 
     * @return String of the socket path url.
     */
    public String path() {
        return path;
    }
}
