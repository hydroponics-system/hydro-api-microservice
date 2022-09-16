package com.hydro.insite_subscription_microservice.client.domain;

import com.hydro.common.dictionary.enums.TextEnum;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * NotificationType contains all known {@link Notification} declerations.
 *
 * @author Sam Butler
 * @since March 24, 2022
 */
@Schema(description = "Notification Types")
public enum NotificationType implements TextEnum {
    USER("USER"),
    SYSTEM_FAILURE("SYSTEM_FAILURE"),
    SYSTEM_LINK("SYSTEM_LINK");

    private String textId;

    private NotificationType(String textId) {
        this.textId = textId;
    }

    @Override
    public String getTextId() {
        return textId;
    }
}
