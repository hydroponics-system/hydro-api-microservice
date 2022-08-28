package com.hydro.insite_subscription_microservice.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import com.hydro.common.logger.HydroLogger;
import com.hydro.insite_subscription_microservice.client.domain.NotificationBody;
import com.hydro.insite_subscription_microservice.client.domain.NotificationEnvelope;

/**
 * Web Notifier Service wraps the common elements of sending web notifications
 * into one call.
 * 
 * @author Sam Butler
 * @since March 24, 2022
 */
@Service
public class WebNotifierService {

    @Autowired
    private SimpMessagingTemplate template;

    @Autowired
    private HydroLogger LOGGER;

    /**
     * Send a Web Notification for a given subscription match with the User
     * Notification set.
     * 
     * @param envelope {@link NotificationEnvelope} to be sent.
     */
    public <T extends NotificationBody> void send(NotificationEnvelope<T> envelope) {
        LOGGER.info("Sending Web Notification to '{}' with type '{}'", envelope.getDestination(),
                    envelope.getBody().getBodyType());
        sendNotification(envelope.getDestination(), envelope);
    }

    /**
     * Send a Web Notification for a given subscription match with the User
     * Notification set to a single user.
     * 
     * @param envelope    {@link NotificationEnvelope} to be sent.
     * @param sessionUUID The unique session id for the user.
     */
    public <T extends NotificationBody> void send(NotificationEnvelope<T> envelope, String sessionUUID) {
        LOGGER.info("Sending Web Notification to '{}' with type '{}'",
                    String.format("%s-%s", envelope.getDestination(), sessionUUID), envelope.getBody().getBodyType());
        sendNotification(String.format("%s-%s", envelope.getDestination(), sessionUUID), envelope);
    }

    /**
     * Helper method for sending a web notification to the desired destination and
     * body.
     * 
     * @param <T>         The generic envelope type.
     * @param envelope    The body to be sent.
     * @param destination Where the notification should go.
     */
    private <T extends NotificationBody> void sendNotification(String destination, NotificationEnvelope<T> envelope) {
        template.convertAndSend(destination, envelope);
    }
}
