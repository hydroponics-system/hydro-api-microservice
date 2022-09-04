package com.hydro.insite_subscription_microservice.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

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
import com.hydro.insite_subscription_microservice.client.domain.Notification;
import com.hydro.insite_subscription_microservice.client.domain.NotificationSocket;
import com.hydro.insite_subscription_microservice.client.domain.UserPrincipal;
import com.hydro.insite_subscription_microservice.notification.UserNotification;
import com.hydro.test.factory.annotations.HydroServiceTest;

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

    @InjectMocks
    private SubscriptionService service;

    @Captor
    private ArgumentCaptor<Notification> sendNotificationCaptor;

    @Captor
    private ArgumentCaptor<String> sessionCaptor;

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
        lenient().when(userRegistry.getUsers()).thenReturn(Sets.newHashSet(su));
    }

    @Test
    public void testPushWithJustBody() {
        UserNotification userSub = new UserNotification();
        userSub.setName("Test User");
        userSub.setUserId(5);

        service.push(userSub);

        verify(webNotifierService).send(sendNotificationCaptor.capture());

        Notification body = sendNotificationCaptor.getValue();
        assertEquals(body.getClass(), UserNotification.class, "Should be UserNotification class");

        UserNotification u = (UserNotification) body;

        assertEquals(u.getName(), "Test User", "User Name");
        assertEquals(u.getUserId(), 5, "User Id");
        assertEquals("/topic/general/notification", u.getDestination(), "Notification Destination");
    }

    @Test
    public void testPushWithBodyAndNotificationAction() {
        service.push(new UserNotification(), NotificationSocket.QUEUE_USER_NOTIFICATION, 12);

        verify(webNotifierService).send(sendNotificationCaptor.capture(), sessionCaptor.capture());

        Notification body = sendNotificationCaptor.getValue();
        assertEquals(body.getClass(), UserNotification.class, "Should be UserSubscription class");
        assertEquals("/queue/user/notification", body.getDestination(), "Notification Destination");
        assertEquals("fake-uuid", sessionCaptor.getValue(), "Session UUID");
    }
}
