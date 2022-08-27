package com.hydro.insite_subscription_microservice.client.domain;

/**
 * Implementation class for the NotificationBody Interface.
 * 
 * @author Sam Butler
 * @since March 24, 2022
 */
public class NotificationBodyImplementation implements NotificationBody {

    private NotificationBodyType bodyType;

    public NotificationBodyType getBodyType() {
        return bodyType;
    }

    public void setType(NotificationBodyType bodyType) {
        this.bodyType = bodyType;
    }
}
