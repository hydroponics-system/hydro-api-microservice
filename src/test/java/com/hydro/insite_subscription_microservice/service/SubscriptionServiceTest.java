package com.hydro.insite_subscription_microservice.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.security.Principal;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.messaging.simp.user.SimpSession;
import org.springframework.messaging.simp.user.SimpUser;
import org.springframework.messaging.simp.user.SimpUserRegistry;

import com.google.common.collect.Sets;
import com.hydro.common.dictionary.data.User;
import com.hydro.common.jwt.utility.JwtHolder;
import com.hydro.insite_common_microservice.util.HydroLogger;
import com.hydro.insite_subscription_microservice.client.domain.NotificationAction;
import com.hydro.insite_subscription_microservice.client.domain.NotificationEnvelope;
import com.hydro.insite_subscription_microservice.client.domain.NotificationSocket;
import com.hydro.insite_subscription_microservice.client.domain.UserPrincipal;
import com.hydro.insite_subscription_microservice.notification.UserNotification;
import com.hydro.utility.factory.annotations.HydroServiceTest;

/**
 * Test class for the Subscription Service.
 * 
 * @author Sam Butler
 * @since August 23, 2022
 */
@HydroServiceTest
public class SubscriptionServiceTest {

    @Mock
    private WebNotifierService webNotifierService;

    @Mock
    private JwtHolder jwtHolder;

    @Mock
    private SimpUserRegistry userRegistry;

    @Mock
    private HydroLogger LOGGER;

    @InjectMocks
    private SubscriptionService service;

    @Captor
    private ArgumentCaptor<NotificationEnvelope<?>> sendNotificationCaptor;

    @BeforeEach
    public void setup() {
        SimpUser su = new SimpUser() {
            @Override
            public Principal getPrincipal() {
                User u = new User();
                u.setId(12);
                return new UserPrincipal("fake-uuid", u);
            }

            public String getName() {
                return null;
            }

            public boolean hasSessions() {
                return false;
            }

            public SimpSession getSession(String sessionId) {
                return null;
            }

            public Set<SimpSession> getSessions() {
                return null;
            }
        };
        when(userRegistry.getUsers()).thenReturn(Sets.newHashSet(su));
    }

    @Test
    public void testPushWithJustBody() {
        UserNotification userSub = new UserNotification();
        userSub.setName("Test User");
        userSub.setUserId(5);

        service.push(userSub, NotificationSocket.USER, 12);

        verify(webNotifierService).send(sendNotificationCaptor.capture(), anyString());

        NotificationEnvelope<?> envelope = sendNotificationCaptor.getValue();
        assertEquals(envelope.getBody().getClass(), UserNotification.class, "Should be UserSubscription class");

        UserNotification resultSub = (UserNotification) envelope.getBody();
        assertEquals(resultSub.getName(), "Test User", "User Name");
        assertEquals(resultSub.getUserId(), 5, "User Id");
        assertEquals(NotificationAction.CREATE, envelope.getAction(), "Notification Action");
        assertEquals("/queue/user/notification", envelope.getDestination(), "Notification Destination");
    }

    @Test
    public void testPushWithBodyAndNotificationAction() {
        service.push(NotificationAction.DELETE, new UserNotification(), NotificationSocket.USER, 12);

        verify(webNotifierService).send(sendNotificationCaptor.capture(), anyString());

        NotificationEnvelope<?> envelope = sendNotificationCaptor.getValue();
        assertEquals(envelope.getBody().getClass(), UserNotification.class, "Should be UserSubscription class");
        assertEquals(NotificationAction.DELETE, envelope.getAction(), "Notification Action");
        assertEquals("/queue/user/notification", envelope.getDestination(), "Notification Destination");
    }
}
