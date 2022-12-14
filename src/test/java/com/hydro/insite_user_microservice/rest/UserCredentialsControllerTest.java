package com.hydro.insite_user_microservice.rest;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ContextConfiguration;

import com.hydro.InsiteMicroserviceApplication;
import com.hydro.common.annotations.interfaces.ControllerJwt;
import com.hydro.common.dictionary.data.User;
import com.hydro.common.dictionary.enums.WebRole;
import com.hydro.insite_user_microservice.client.domain.PasswordUpdate;
import com.hydro.insite_user_microservice.service.UserCredentialsService;
import com.hydro.test.factory.abstracts.BaseControllerTest;
import com.hydro.test.factory.annotations.HydroRestTest;

/**
 * Test class for the User Credentials Controller.
 * 
 * @author Sam Butler
 * @since August 23, 2022
 */
@ContextConfiguration(classes = InsiteMicroserviceApplication.class)
@HydroRestTest
@ControllerJwt
public class UserCredentialsControllerTest extends BaseControllerTest {
    private static final String USER_CREDENTIALS_PATH = "/api/user-app/credentials";

    @MockBean
    private UserCredentialsService service;

    @Test
    public void testupdateUserPassword() throws Exception {
        when(service.updateUserPassword(any(PasswordUpdate.class))).thenReturn(new User());
        check(put(USER_CREDENTIALS_PATH + "/password", new PasswordUpdate(), User.class), serializedNonNull());

        verify(service).updateUserPassword(any(PasswordUpdate.class));
    }

    @Test
    public void testUpdateUserPasswordById() throws Exception {
        when(service.updateUserPasswordById(anyInt(), any(PasswordUpdate.class))).thenReturn(new User());
        check(put(USER_CREDENTIALS_PATH + "/password/12", new PasswordUpdate(), User.class), serializedNonNull());

        verify(service).updateUserPasswordById(eq(12), any(PasswordUpdate.class));
    }

    @Test
    @ControllerJwt(webRole = WebRole.DEVELOPER)
    public void testUpdateUserPasswordByIdNonAdmin() throws Exception {
        when(service.updateUserPasswordById(anyInt(), any(PasswordUpdate.class))).thenReturn(new User());
        check(put(USER_CREDENTIALS_PATH + "/password/12", new PasswordUpdate()),
              error(HttpStatus.FORBIDDEN, "Insufficient Permissions for role 'DEVELOPER'"));

        verify(service, never()).updateUserPasswordById(anyInt(), any(PasswordUpdate.class));
    }

    @Test
    public void testresetUserPassword() throws Exception {
        when(service.resetUserPassword(anyString())).thenReturn(new User());
        check(put(USER_CREDENTIALS_PATH + "/password/reset", new PasswordUpdate("currentPass", "NewPass"), User.class),
              serializedNonNull());

        verify(service).resetUserPassword("NewPass");
    }
}
