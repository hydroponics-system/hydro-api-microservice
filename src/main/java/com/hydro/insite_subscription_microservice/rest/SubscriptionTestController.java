package com.hydro.insite_subscription_microservice.rest;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.hydro.common.annotations.interfaces.HasAccess;
import com.hydro.common.annotations.interfaces.RestApiController;
import com.hydro.common.dictionary.enums.WebRole;
import com.hydro.insite_subscription_microservice.client.domain.NotificationSocket;
import com.hydro.insite_subscription_microservice.client.domain.SystemPrincipal;
import com.hydro.insite_subscription_microservice.client.domain.UserPrincipal;
import com.hydro.insite_subscription_microservice.notification.UserNotification;
import com.hydro.insite_subscription_microservice.openapi.TagSubscription;
import com.hydro.insite_subscription_microservice.service.SubscriptionService;

import io.swagger.v3.oas.annotations.Operation;

@RequestMapping("/api/subscription-app")
@RestApiController
@TagSubscription
public class SubscriptionTestController {

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
    @Operation(summary = "Sends a notification to all subscriptions. Test Endpoint", description = "Will send the notification body to the subscription microservice.")
    @PostMapping(path = "/notification")
    @HasAccess(WebRole.DEVELOPER)
    public void pushNotification() {
        UserNotification user = new UserNotification();
        user.setName("TEST USER");
        user.setUserId(15);
        service.push(user);
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

    /**
     * Will get the active users connected to the websocket session.
     * 
     * @return List of system connections.
     */
    @Operation(summary = "Get's a list of active system sessions", description = "Will return a list of system objects of connected sessions.")
    @GetMapping(path = "/systems")
    @HasAccess(WebRole.DEVELOPER)
    public List<SystemPrincipal> getActiveSessionSystems() {
        return service.getActiveSessionSystems();
    }
}
