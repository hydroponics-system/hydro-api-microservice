package com.hydro.insite_subscription_microservice.rest;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.hydro.common.annotations.interfaces.HasAccess;
import com.hydro.common.annotations.interfaces.RestApiController;
import com.hydro.common.dictionary.enums.WebRole;
import com.hydro.insite_subscription_microservice.client.domain.NotificationBody;
import com.hydro.insite_subscription_microservice.client.domain.NotificationSocket;
import com.hydro.insite_subscription_microservice.client.domain.UserPrincipal;
import com.hydro.insite_subscription_microservice.openapi.TagSubscription;
import com.hydro.insite_subscription_microservice.service.SubscriptionService;

import io.swagger.v3.oas.annotations.Operation;

@RequestMapping("/api/subscription-app")
@RestApiController
@TagSubscription
public class SubscriptionController {

    @Autowired
    private SubscriptionService service;

    /**
     * Endpoint used for sending notifications to every subscription. It will take
     * in the notification body and deserialize based on it's type. This will push
     * to the default user queue when sending notifications. That is
     * {@link NotificationSocket#USER}
     * 
     * @param body The body to push with the notification.
     */
    @Operation(summary = "Sends a notification to all subscriptions.", description = "Will send the notification body to the subscription microservice.")
    @PostMapping(path = "/notification")
    @HasAccess(WebRole.DEVELOPER)
    public void pushNotification(@RequestBody NotificationBody body) {
        service.push(body);
    }

    /**
     * Endpoint used for sending notifications to a single user. It will take in the
     * notification body and deserialize based on it's type. This will push to the
     * default user queue when sending notifications. That is
     * {@link NotificationSocket#USER}
     * 
     * @param body The body to push with the notification.
     * @param id   The user id to send the notification too.
     */
    @Operation(summary = "Sends a notification to the user", description = "Will send the notification body to the subscription microservice.")
    @PostMapping(path = "/notification/{id}/id")
    @HasAccess(WebRole.DEVELOPER)
    public void pushNotificationToUser(@RequestBody NotificationBody body, @PathVariable int id) {
        service.push(body, NotificationSocket.USER, id);
    }

    /**
     * Endpoint used for sending notifications to users that match the web role. It
     * will take in the notification body and deserialize based on it's type.
     * 
     * @param body The body to push with the notification.
     * @param role The web role of the notification to be sent too.
     */
    @Operation(summary = "Sends a notification to the user with the matching role", description = "Will send the notification body to the subscription microservice witht the given web role.")
    @PostMapping(path = "/notification/{role}/role")
    @HasAccess(WebRole.DEVELOPER)
    public void pushNotificationToUser(@RequestBody NotificationBody body, @PathVariable WebRole role) {
        service.push(body, NotificationSocket.USER, role);
    }

    /**
     * Will get the active users connected to the websocket session.
     * 
     * @return List of SimpUser connections.
     */
    @Operation(summary = "Get's a list of active user sessions", description = "Will return a list of SimpUser objects of connected sessions.")
    @GetMapping(path = "/users")
    @HasAccess(WebRole.DEVELOPER)
    public List<UserPrincipal> getActiveSessionUsers() {
        return service.getActiveSessionUsers();
    }
}
