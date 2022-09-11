package com.hydro.insite_subscription_microservice.client;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.reactive.socket.server.WebSocketService;

import com.hydro.common.annotations.interfaces.Client;
import com.hydro.common.dictionary.enums.WebRole;
import com.hydro.insite_subscription_microservice.client.domain.Notification;
import com.hydro.insite_subscription_microservice.client.domain.NotificationSocket;
import com.hydro.insite_subscription_microservice.client.domain.SystemPrincipal;
import com.hydro.insite_subscription_microservice.client.domain.UserPrincipal;
import com.hydro.insite_subscription_microservice.service.SubscriptionNotifierService;

/**
 * Client for {@link WebSocketService} to expose the given endpoint's to other
 * services.
 * 
 * @author Sam Butler
 * @since Dec 14, 2020
 */
@Client
public class SubscriptionNotifierClient {

    @Autowired
    private SubscriptionNotifierService service;

    /**
     * Push a web notification to a user for the given user id. The default socket
     * this notification will be sent to
     * {@link NotificationSocket#QUEUE_USER_NOTIFICATION}.
     * 
     * @param body   The body to be sent.
     * @param socket The socket path the notification should be sent too.
     * @param userId The user id of the user to send it too.
     */
    public void sendToUser(Notification body, int userId) {
        service.sendToUser(body, userId);
    }

    /**
     * Push a web notification to list of users with the given role. It will perform
     * a notification action with the passed in notification body. The default
     * socket this notification will be sent to
     * {@link NotificationSocket#QUEUE_USER_NOTIFICATION}.
     * 
     * @param body The body to be sent.
     * @param role The role of the user to send it too.
     */
    public void sendToUser(Notification body, WebRole role) {
        service.sendToUser(body, role);
    }

    /**
     * Push a web notification based on the given system uuid. Only the client with
     * the specified system uuid will receive the notification. The default socket
     * this notification will be sent to
     * {@link NotificationSocket#QUEUE_SYSTEM_NOTIFICATION}.
     * 
     * @param body The body to be sent.
     * @param uuid The system uuid to send the notification too.
     */
    public void sendToSystem(Notification body, String uuid) {
        service.sendToSystem(body, uuid);
    }

    /**
     * Push a web notification based on the given system uuid. Only the client with
     * the specified system uuid will receive the notification.
     * 
     * @param body   The body to be sent.
     * @param socket The socket path the notification should be sent too.
     * @param uuid   The system uuid to send the notification too.
     */
    public void sendToSystem(Notification body, String socket, String uuid) {
        service.sendToSystem(body, socket, uuid);
    }

    /**
     * Push a web notification. Default path this will be sent to is
     * {@link NotificationSocket#GENERAL}
     * 
     * @param body The body to be sent.
     */
    public void send(Notification body) {
        service.send(body);
    }

    /**
     * Push a web notification to the passed in socket url.
     * 
     * @param body   The body to be sent.
     * @param socket The socket path the notification should be sent too.
     */
    public void send(Notification body, String socket) {
        service.send(body, socket);
    }

    /**
     * Push a web notification based on the given session id. Only the client with
     * the specified session id will receive the notification.
     * 
     * @param body      The body to be sent.
     * @param socket    The socket path the notification should be sent too.
     * @param sessionId The session of id of the client to send the notification
     *                  too.
     */
    public void send(Notification body, String socket, String sessionId) {
        service.send(body, socket, sessionId);
    }

    /**
     * Will get the active users connected to the websocket session.
     * 
     * @return List of SimpUser connections.
     */
    public List<UserPrincipal> getActiveUserSessions() {
        return service.getActiveUserSessions();
    }

    /**
     * Will get the active systems connected to the websocket session.
     * 
     * @return List of system connections.
     */
    public List<SystemPrincipal> getActiveSystemSessions() {
        return service.getActiveSystemSessions();
    }
}