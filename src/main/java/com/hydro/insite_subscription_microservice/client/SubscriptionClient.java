package com.hydro.insite_subscription_microservice.client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.reactive.socket.server.WebSocketService;

import com.hydro.common.dictionary.enums.WebRole;
import com.hydro.insite_common_microservice.annotations.interfaces.Client;
import com.hydro.insite_subscription_microservice.client.domain.NotificationAction;
import com.hydro.insite_subscription_microservice.client.domain.NotificationBody;
import com.hydro.insite_subscription_microservice.client.domain.NotificationSocket;
import com.hydro.insite_subscription_microservice.service.SubscriptionService;

/**
 * Client for {@link WebSocketService} to expose the given endpoint's to other
 * services.
 * 
 * @author Sam Butler
 * @since Dec 14, 2020
 */
@Client
public class SubscriptionClient {

    @Autowired
    private SubscriptionService service;

    /**
     * Push a web notification to the socket. It will perform a
     * {@link NotificationAction#CREATE} with the passed in notification body.
     * Default path this will be sent to is {@link NotificationSocket#GENERAL}
     * 
     * @param body The body to be sent.
     */
    public void push(NotificationBody body) {
        service.push(body, NotificationSocket.GENERAL);
    }

    /**
     * Push a web notification to the socket. It will perform a
     * {@link NotificationAction#CREATE} with the passed in notification body.
     * 
     * @param body   The body to be sent.
     * @param socket The socket path the notification should be sent too.
     */
    public void push(NotificationBody body, NotificationSocket socket) {
        service.push(NotificationAction.CREATE, body, socket);
    }

    /**
     * Push a web notification to the socket. It will perform a
     * {@link NotificationAction#CREATE} with the passed in notification body.
     * 
     * @param action The action to perform.
     * @param body   The body to be sent.
     * @param socket The socket path the notification should be sent too.
     */
    public void push(NotificationAction action, NotificationBody body, NotificationSocket socket) {
        service.push(action, body, socket);
    }

    /**
     * Push a web notification. It will perform a {@link NotificationAction#CREATE}
     * with the passed in notification body.
     * 
     * @param action The action to perform.
     * @param body   The body to be sent.
     * @param userId Where the notification is going.
     */
    public void push(NotificationBody body, NotificationSocket socket, int userId) {
        service.push(body, socket, userId);
    }

    /**
     * Push a web notification. It will perform a notification action with the
     * passed in notification body.
     * 
     * @param action The action to perform.
     * @param body   The body to be sent.
     * @param userId Where the notification is going.
     */
    public void push(NotificationAction action, NotificationBody body, NotificationSocket socket, int userId) {
        service.push(action, body, socket, userId);
    }

    /**
     * Push a web notification to the given Web Role. It will perform a
     * {@link NotificationAction#CREATE} with the passed in notification body.
     * 
     * @param body   The body to be sent.
     * @param userId Where the notification is going.
     */
    public void push(NotificationBody body, NotificationSocket socket, WebRole role) {
        service.push(NotificationAction.CREATE, body, socket, role);
    }

    /**
     * Push a web notification to the given Web Role. It will perform a
     * {@link NotificationAction#CREATE} with the passed in notification body.
     * 
     * @param body   The body to be sent.
     * @param userId Where the notification is going.
     */
    public void push(NotificationAction action, NotificationBody body, NotificationSocket socket, WebRole role) {
        service.push(action, body, socket, role);
    }
}