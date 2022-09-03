package com.hydro.insite_subscription_microservice.client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.reactive.socket.server.WebSocketService;

import com.hydro.common.annotations.interfaces.Client;
import com.hydro.common.dictionary.enums.WebRole;
import com.hydro.insite_subscription_microservice.client.domain.Notification;
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
     * Push a web notification to the socket. Default path this will be sent to is
     * {@link NotificationSocket#GENERAL}
     * 
     * @param body The body to be sent.
     */
    public void push(Notification body) {
        service.push(body, NotificationSocket.TOPIC_GENERAL_NOTIFICATION);
    }

    /**
     * Push a web notification to the socket.
     * 
     * @param body   The body to be sent.
     * @param socket Where th notification should go.
     */
    public void push(Notification body, String socket) {
        service.push(body, socket);
    }

    /**
     * Push a web notification.
     * 
     * @param body   The body to be sent.
     * @param socket Where th notification should go.
     * @param userId Where the notification is going.
     */
    public void push(Notification body, String socket, int userId) {
        service.push(body, socket, userId);
    }

    /**
     * Push a web notification to the given Web Role.
     * 
     * @param body   The body to be sent.
     * @param socket Where th notification should go.
     * @param userId Where the notification is going.
     */
    public void push(Notification body, String socket, WebRole role) {
        service.push(body, socket, role);
    }
}