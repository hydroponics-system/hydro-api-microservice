package com.hydro.insite_auth_microservice.rest;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ContextConfiguration;

import com.hydro.InsiteMicroserviceApplication;
import com.hydro.common.annotations.interfaces.ControllerJwt;
import com.hydro.common.dictionary.data.User;
import com.hydro.insite_auth_microservice.client.domain.AuthToken;
import com.hydro.insite_auth_microservice.client.domain.request.AuthenticationRequest;
import com.hydro.insite_auth_microservice.service.AuthenticationService;
import com.hydro.test.factory.abstracts.BaseControllerTest;
import com.hydro.test.factory.annotations.HydroRestTest;

/**
 * Test class for the Authenticate Controller.
 * 
 * @author Sam Butler
 * @since August 23, 2022
 */
@ContextConfiguration(classes = InsiteMicroserviceApplication.class)
@HydroRestTest
public class AuthenticationControllerTest extends BaseControllerTest {

    @MockBean
    private AuthenticationService service;

    @Test
    public void testAuthenticate() throws Exception {
        when(service.authenticate(any(AuthenticationRequest.class))).thenReturn(new AuthToken<User>());
        AuthenticationRequest request = new AuthenticationRequest("test@mail.com", "testPassword");
        check(post("/api/authenticate", request, AuthToken.class), serializedNonNull());
    }

    @Test
    @ControllerJwt
    public void testReAuthenticate() throws Exception {
        when(service.reauthenticate()).thenReturn(new AuthToken<User>());
        check(post("/api/reauthenticate", AuthToken.class), serializedNonNull());
    }

    @Test
    public void testReAuthenticateNoToken() {
        check(post("/api/reauthenticate"), error(HttpStatus.UNAUTHORIZED, "Missing JWT Token."));
    }
}
