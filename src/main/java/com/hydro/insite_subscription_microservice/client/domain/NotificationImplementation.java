package com.hydro.insite_subscription_microservice.client.domain;

/**
 * Implementation class for the NotificationBody Interface.
 * 
 * @author Sam Butler
 * @since March 24, 2022
 */
public class NotificationImplementation implements Notification {

    private NotificationType type;

    public NotificationType getType() {
        return type;
    }

    public void setType(NotificationType type) {
        this.type = type;
    }
}
