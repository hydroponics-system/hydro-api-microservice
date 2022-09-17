package com.hydro.insite_subscription_microservice.client.domain;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Notification Socket types to send a notification too.
 *
 * @author Sam Butler
 * @since March 24, 2022
 */
@Schema(description = "Notification Socket Paths")
public abstract class NotificationSocket {
    public static final String QUEUE_USER_NOTIFICATION = "/queue/user/notification";
    public static final String QUEUE_SYSTEM_NOTIFICATION = "/queue/system/notification";
    public static final String QUEUE_SYSTEM_LINK_NOTIFICATION = "/queue/system/link/notification";

    public static final String TOPIC_GENERAL_NOTIFICATION = "/topic/general/notification";
}
