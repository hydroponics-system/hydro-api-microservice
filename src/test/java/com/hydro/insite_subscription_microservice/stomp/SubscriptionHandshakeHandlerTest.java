package com.hydro.insite_subscription_microservice.stomp;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

import java.net.URISyntaxException;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.http.server.ServerHttpRequest;

import com.hydro.common.dictionary.data.User;
import com.hydro.common.dictionary.enums.WebRole;
import com.hydro.common.jwt.utility.JwtHolder;
import com.hydro.insite_subscription_microservice.client.domain.UserPrincipal;
import com.hydro.utility.factory.annotations.HydroServiceTest;

/**
 * Test class for the Subscription Handshake Handler.
 * 
 * @author Sam Butler
 * @since August 23, 2022
 */
@HydroServiceTest
public class SubscriptionHandshakeHandlerTest {

    @Mock
    private JwtHolder jwtHolder;

    @Mock
    private ServerHttpRequest request;

    @InjectMocks
    private SubscriptionHandshakeHandler handler;

    @Test
    public void testDetermineUser() throws URISyntaxException {
        User currentUser = new User();
        currentUser.setId(12);
        currentUser.setWebRole(WebRole.DEVELOPER);

        when(jwtHolder.getUser()).thenReturn(currentUser);

        UserPrincipal u = (UserPrincipal) handler.determineUser(request, null, null);

        assertNotNull(u.getName(), "Random UUID");
        assertEquals(12, u.getUser().getId(), "User id");
        assertEquals(WebRole.DEVELOPER, u.getUser().getWebRole(), "User Role");
    }
}
