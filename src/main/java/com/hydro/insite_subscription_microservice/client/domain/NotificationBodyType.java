package com.hydro.insite_subscription_microservice.client.domain;

import com.hydro.common.dictionary.enums.TextEnum;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * NotificationBodyType contains all known {@link NotificationBody}
 * declerations.
 *
 * @author Sam Butler
 * @since March 24, 2022
 */
@Schema(description = "Notification Body Types")
public enum NotificationBodyType implements TextEnum {
    USER("USER"),
    SYSTEM_FAILURE("SYSTEM_FAILURE");

    private String textId;

    private NotificationBodyType(String textId) {
        this.textId = textId;
    }

    @Override
    public String getTextId() {
        return textId;
    }
}
