package com.hydro.insite_subscription_microservice.stomp;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

import com.hydro.common.jwt.utility.JwtHolder;

/**
 * Websocket config for setting ws endpoints and defining the handshake handler
 * that should be used on new session connections.
 * 
 * @author Sam Butler
 * @since March 24, 2022
 */
@Configuration
@EnableWebSocketMessageBroker
public class SubscriptionConfig implements WebSocketMessageBrokerConfigurer {
    private static final Logger LOGGER = LoggerFactory.getLogger(SubscriptionConfig.class);
    private final long DEFAULT_HEARTBEAT = 20000;
    private final String SOCKET_URI = "/subscription/socket";

    @Autowired
    private JwtHolder jwtHolder;

    @Bean
    public TaskScheduler taskScheduler() {
        ThreadPoolTaskScheduler ts = new ThreadPoolTaskScheduler();
        ts.setPoolSize(10);
        ts.initialize();
        return ts;
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        config.setUserDestinationPrefix("/user").enableSimpleBroker("/queue", "/topic")
                .setTaskScheduler(taskScheduler())
                .setHeartbeatValue(new long[] { DEFAULT_HEARTBEAT, DEFAULT_HEARTBEAT });
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        LOGGER.info("Websocket connection opened on uri '{}'", SOCKET_URI);
        registry.addEndpoint(SOCKET_URI).setHandshakeHandler(new SubscriptionHandshakeHandler(jwtHolder))
                .setAllowedOrigins("*");
    }
}
