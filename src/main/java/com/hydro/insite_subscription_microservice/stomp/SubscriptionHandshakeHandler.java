package com.hydro.insite_subscription_microservice.stomp;

import java.security.Principal;
import java.util.Map;
import java.util.UUID;

import org.springframework.http.server.ServerHttpRequest;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.support.DefaultHandshakeHandler;

import com.hydro.common.jwt.utility.JwtHolder;
import com.hydro.common.logger.HydroLogger;
import com.hydro.insite_subscription_microservice.client.domain.UserPrincipal;

/**
 * Custom handshake handler for assigning a unique id to a new connection.
 * 
 * @author Sam Butler
 * @since March 24, 2022
 */
public class SubscriptionHandshakeHandler extends DefaultHandshakeHandler {

    private HydroLogger LOGGER = new HydroLogger(SubscriptionHandshakeHandler.class);

    private JwtHolder jwtHolder;

    public SubscriptionHandshakeHandler(JwtHolder jwtHolder) {
        this.jwtHolder = jwtHolder;
    }

    @Override
    protected Principal determineUser(ServerHttpRequest request, WebSocketHandler wsHandler,
            Map<String, Object> attributes) {
        String randomId = UUID.randomUUID().toString();

        LOGGER.info("Client connected to socket with ID '{}'", randomId);
        return new UserPrincipal(randomId, jwtHolder.getUser());
    }
}
