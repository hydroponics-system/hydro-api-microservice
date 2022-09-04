package com.hydro.insite_subscription_microservice.client.domain;

import java.time.LocalDateTime;

/**
 * Abstract class for extending when creating notifications
 * 
 * @author Sam Butler
 * @since March 24, 2022
 */
public abstract class Notification implements NotificationBodyType {

    private String destination;

    private LocalDateTime created;

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public LocalDateTime getCreated() {
        return created;
    }

    public void setCreated(LocalDateTime created) {
        this.created = created;
    }
}
