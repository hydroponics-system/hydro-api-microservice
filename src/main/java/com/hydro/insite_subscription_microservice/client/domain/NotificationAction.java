package com.hydro.insite_subscription_microservice.client.domain;

import com.hydro.common.dictionary.enums.TextEnum;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Notification Action types
 * 
 * @author Sam Butler
 * @since March 24, 2022
 */
@Schema(description = "Notification Action Types")
public enum NotificationAction implements TextEnum {
    CREATE("C"),
    READ("R"),
    UPDATE("U"),
    DELETE("D");

    private String textId;

    private NotificationAction(String textId) {
        this.textId = textId;
    }

    @Override
    public String getTextId() {
        return textId;
    }
}
