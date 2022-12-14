package com.hydro.insite_user_microservice.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.apache.http.auth.InvalidCredentialsException;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.security.crypto.bcrypt.BCrypt;

import com.hydro.common.dictionary.data.User;
import com.hydro.common.dictionary.enums.WebRole;
import com.hydro.common.exception.InsufficientPermissionsException;
import com.hydro.common.jwt.utility.JwtHolder;
import com.hydro.insite_auth_microservice.client.AuthenticationClient;
import com.hydro.insite_user_microservice.client.UserProfileClient;
import com.hydro.insite_user_microservice.client.domain.PasswordUpdate;
import com.hydro.insite_user_microservice.dao.UserCredentialsDAO;
import com.hydro.test.factory.annotations.HydroServiceTest;
import com.hydro.test.factory.data.UserFactoryData;

/**
 * Test class for the User Credentials Service.
 * 
 * @author Sam Butler
 * @since August 23, 2022
 */
@HydroServiceTest
public class UserCredentialsServiceTest {

    @Mock
    private JwtHolder jwtHolder;

    @Mock
    private AuthenticationClient authClient;

    @Mock
    private UserProfileClient userProfileClient;

    @Mock
    private UserCredentialsDAO userCredentialsDAO;

    @Captor
    private ArgumentCaptor<String> insertUserPasswordCaptor;

    @InjectMocks
    private UserCredentialsService service;

    @Test
    public void testInsertUserPassword() {
        service.insertUserPassword(12, "fakePassword");

        verify(userCredentialsDAO).insertUserPassword(eq(12), insertUserPasswordCaptor.capture());
        assertTrue(BCrypt.checkpw("fakePassword", insertUserPasswordCaptor.getValue()), "Password should be hashed");
    }

    @Test
    public void testUpdateUserPasswordValid() throws Exception {
        PasswordUpdate passUpdate = new PasswordUpdate();
        passUpdate.setCurrentPassword("currentPassword123!");
        passUpdate.setNewPassword("newPassword!");

        when(jwtHolder.getUserId()).thenReturn(12);
        when(jwtHolder.getEmail()).thenReturn("password@user.com");
        when(userProfileClient.getUserById(anyInt())).thenReturn(UserFactoryData.userData());

        User userUpdated = service.updateUserPassword(passUpdate);

        verify(authClient).authenticate("password@user.com", "currentPassword123!");
        verify(userCredentialsDAO).updateUserPassword(eq(12), anyString());
        verify(userProfileClient).getUserById(12);
        assertEquals(12, userUpdated.getId(), "User updated should be id 12");
    }

    @Test
    public void testUpdateUserPasswordAuthenticationFailed() throws Exception {
        when(authClient.authenticate(any(), any())).thenThrow(InvalidCredentialsException.class);
        when(jwtHolder.getEmail()).thenReturn("test@user.com");

        assertThrows(InvalidCredentialsException.class, () -> service.updateUserPassword(new PasswordUpdate()));
        verify(userCredentialsDAO, never()).updateUserPassword(anyInt(), any());
        verify(userProfileClient, never()).getUserById(anyInt());
    }

    @Test
    public void testUpdateUserPasswordInvalidPassword() throws Exception {
        PasswordUpdate passUpdate = new PasswordUpdate();
        passUpdate.setCurrentPassword("currentPassword123!");
        passUpdate.setNewPassword("");

        when(jwtHolder.getUserId()).thenReturn(12);
        when(jwtHolder.getEmail()).thenReturn("password@user.com");

        IllegalArgumentException e = assertThrows(IllegalArgumentException.class,
                                                  () -> service.updateUserPassword(passUpdate));

        verify(authClient).authenticate("password@user.com", "currentPassword123!");
        verify(userCredentialsDAO, never()).updateUserPassword(anyInt(), any());
        verify(userProfileClient, never()).getUserById(anyInt());
        assertEquals("Password can not be empty or null", e.getMessage(), "Exception Message");
    }

    @Test
    public void testUpdateUserPasswordByIdValid() throws Exception {
        PasswordUpdate passUpdate = new PasswordUpdate();
        passUpdate.setCurrentPassword("currentPassword123!");
        passUpdate.setNewPassword("newPassword!");

        User userToUpdate = new User();
        userToUpdate.setWebRole(WebRole.USER);
        userToUpdate.setId(5);

        when(userProfileClient.getUserById(anyInt())).thenReturn(userToUpdate);

        service.updateUserPasswordById(5, passUpdate);

        verify(userCredentialsDAO).updateUserPassword(eq(5), anyString());
        verify(userProfileClient, times(2)).getUserById(5);
    }

    @Test
    public void testUpdateUserPasswordByIdUserHasInsufficientPermissions() throws Exception {
        User userToUpdate = UserFactoryData.userData();

        when(userProfileClient.getUserById(anyInt())).thenReturn(userToUpdate);
        when(jwtHolder.getWebRole()).thenReturn(WebRole.DEVELOPER);

        InsufficientPermissionsException e = assertThrows(InsufficientPermissionsException.class, () -> service
                .updateUserPasswordById(5, new PasswordUpdate()));

        verify(userCredentialsDAO, never()).updateUserPassword(anyInt(), anyString());
        verify(userProfileClient).getUserById(5);
        assertEquals("Your role of 'DEVELOPER' can not update a user of role 'ADMIN'", e.getMessage(),
                     "Exception Message");
    }

    @Test
    public void testResetUserPasswordValid() throws Exception {
        when(jwtHolder.getUserId()).thenReturn(12);
        when(jwtHolder.getResetPassword()).thenReturn(true);

        service.resetUserPassword("newPass");

        verify(userCredentialsDAO).updateUserPassword(eq(12), anyString());
        verify(userProfileClient).getUserById(12);
    }

    @Test
    public void testResetUserPasswordInvalidResetPasswordToken() throws Exception {
        when(jwtHolder.getResetPassword()).thenReturn(false);

        Exception e = assertThrows(Exception.class, () -> service.resetUserPassword("newPass"));

        verify(userCredentialsDAO, never()).updateUserPassword(anyInt(), anyString());
        verify(userProfileClient, never()).getUserById(12);
        assertEquals("Invalid token for reset password!", e.getMessage(), "Exception Message");
    }
}
