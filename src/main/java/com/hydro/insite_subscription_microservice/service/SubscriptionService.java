package com.hydro.insite_subscription_microservice.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.user.SimpUserRegistry;
import org.springframework.stereotype.Component;

import com.hydro.common.dictionary.enums.WebRole;
import com.hydro.insite_subscription_microservice.client.domain.Notification;
import com.hydro.insite_subscription_microservice.client.domain.NotificationSocket;
import com.hydro.insite_subscription_microservice.client.domain.UserPrincipal;

/**
 * Subscription service for managing and processing notifications.
 * 
 * @author Sam Butler
 * @since July 27, 2022
 */
@Component
public class SubscriptionService {

    private static final Logger LOGGER = LoggerFactory.getLogger(SubscriptionService.class);

    @Autowired
    private WebNotifierService webNotifierService;

    @Autowired
    private SimpUserRegistry userRegistry;

    /**
     * Push a web notification. Default path this will be sent to is
     * {@link NotificationSocket#GENERAL}
     * 
     * @param body The body to be sent.
     */
    public void push(Notification body) {
        push(body, NotificationSocket.TOPIC_GENERAL_NOTIFICATION);
    }

    /**
     * Push a web notification.
     * 
     * @param action The action to perform.
     * @param body   The body to be sent.
     * @param socket The socket path the notification should be sent too.
     */
    public void push(Notification body, String socket) {
        webNotifierService.send(body, socket);
    }

    /**
     * Push a web notification to a user.
     * 
     * @param action The action to perform.
     * @param body   The body to be sent.
     * @param socket The socket path the notification should be sent too.
     * @param userId The user id of the user to send it too.
     */
    public void push(Notification body, String socket, int userId) {
        getUserSessionByUserId(userId)
                .ifPresentOrElse(u -> webNotifierService.send(body, socket, u.getName()),
                                 () -> LOGGER.warn("No subscription found for user ID '{}'", userId));
    }

    /**
     * Push a web notification to list of users with the given role. It will perform
     * a notification action with the passed in notification body.
     * 
     * @param action The action to perform.
     * @param body   The body to be sent.
     * @param socket The socket path the notification should be sent too.
     * @param role   The role of the user to send it too.
     */
    public void push(Notification body, String socket, WebRole role) {
        List<UserPrincipal> sessionList = getUserSessionByWebRole(role);
        if(sessionList.isEmpty()) {
            LOGGER.warn("No subscription sessions found for web role '{}'", role.toString());
        }

        for(UserPrincipal u : sessionList) {
            webNotifierService.send(body, socket, u.getName());
        }
    }

    /**
     * Will get the active users connected to the websocket session.
     * 
     * @return List of SimpUser connections.
     */
    public List<UserPrincipal> getActiveSessionUsers() {
        return userRegistry.getUsers().stream().map(v -> (UserPrincipal) v.getPrincipal()).collect(Collectors.toList());
    }

    /**
     * Get the subscription session of a user by their id.
     * 
     * @param userId The id of the user to find.
     * @return Optional of the user Principal.
     */
    private Optional<UserPrincipal> getUserSessionByUserId(int userId) {
        return getActiveSessionUsers().stream().filter(u -> u.getUser().getId() == userId).findFirst();
    }

    /**
     * Get the subscription session of users by role
     * 
     * @param role The user role to find.
     * @return List of the user Principal.
     */
    private List<UserPrincipal> getUserSessionByWebRole(WebRole role) {
        return getActiveSessionUsers().stream().filter(u -> u.getUser().getWebRole().equals(role))
                .collect(Collectors.toList());
    }
}
