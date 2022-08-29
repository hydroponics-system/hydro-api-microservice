package com.hydro.insite_user_microservice.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import com.hydro.common.dictionary.data.User;
import com.hydro.common.dictionary.enums.WebRole;
import com.hydro.common.exception.InsufficientPermissionsException;
import com.hydro.common.exception.NotFoundException;
import com.hydro.common.jwt.utility.JwtHolder;
import com.hydro.insite_user_microservice.client.UserCredentialsClient;
import com.hydro.insite_user_microservice.dao.UserProfileDAO;
import com.hydro.test.factory.annotations.HydroServiceTest;
import com.hydro.test.factory.data.UserFactoryData;

/**
 * Test class for the Manage User Profile Service.
 * 
 * @author Sam Butler
 * @since August 23, 2022
 */
@HydroServiceTest
public class ManageUserProfileServiceTest {

    @Mock
    private JwtHolder jwtHolder;

    @Mock
    private UserCredentialsClient userCredentialsClient;

    @Mock
    private UserProfileDAO dao;

    @InjectMocks
    private ManageUserProfileService service;

    // @Test
    // public void testFake() {
    // String temp =
    // "eyJhbGciOiJIUzUxMiJ9.eyJ3ZWJSb2xlIjoiVVNFUiIsImZpcnN0TmFtZSI6IlNhbXVlbCIsImxhc3ROYW1lIjoiQnV0bGVyIiwicGFzc3dvcmRSZXNldCI6ZmFsc2UsImVudiI6IkxPQ0FMIiwiZXhwIjoxNjYxNzQ5NjYwLCJ1c2VySWQiOjEsImlhdCI6MTY2MTczMTY2MCwiZW1haWwiOiJzYW1idXRsZXIxMDE3QGljbG91ZC5jb20iLCJqd3RUeXBlIjoiV0VCIn0.famJlEmvyRtmHmdDr5nsIkrkRHdrU1zsZlwFeNvwsqdlkH3i6E-jYCrxVn1kxF3kNQ7z7CNpyL5mIEtapZAxXQ";
    // Claims newData = (Claims) Jwts.parser()
    // .setSigningKey("155e821eb53ac3597c7b723d68acf386279f132e3336768c8aecc4b65e5be031").parse(temp)
    // .getBody();
    // int test = 1;
    // }

    @Test
    public void testCreateUser() throws Exception {
        User newUser = new User();
        newUser.setPassword("NewUserPassword!");

        when(dao.insertUser(any(User.class))).thenReturn(1);

        service.createUser(newUser);

        verify(dao).insertUser(any(User.class));
        verify(userCredentialsClient).insertUserPassword(1, "NewUserPassword!");
    }

    @Test
    public void testUpdateUserProfile() throws Exception {
        when(jwtHolder.getUserId()).thenReturn(12);

        service.updateUserProfile(UserFactoryData.userData());

        verify(dao).updateUserProfile(eq(12), any(User.class));
    }

    @Test
    public void testUpdateUserProfileByIdValid() throws Exception {
        User userToUpdate = UserFactoryData.userData();
        userToUpdate.setWebRole(WebRole.SYSTEM_USER);

        when(dao.getUserById(anyInt())).thenReturn(userToUpdate);
        when(jwtHolder.getWebRole()).thenReturn(WebRole.ADMIN);

        service.updateUserProfileById(5, userToUpdate);

        verify(dao).updateUserProfile(eq(5), any(User.class));
    }

    @Test
    public void testUpdateUserProfileByIdUserHasInsufficientPermissions() throws Exception {
        User userToUpdate = UserFactoryData.userData();

        when(dao.getUserById(anyInt())).thenReturn(userToUpdate);
        when(jwtHolder.getWebRole()).thenReturn(WebRole.ADMIN);

        InsufficientPermissionsException e = assertThrows(InsufficientPermissionsException.class,
                                                          () -> service.updateUserProfileById(5, userToUpdate));

        verify(dao, never()).updateUserProfile(anyInt(), any());
        assertEquals("Your role of 'ADMIN' can not update a user of role 'ADMIN'", e.getMessage(), "Exception Message");
    }

    @Test
    public void testUpdateUserLastLoginToNowValid() throws Exception {
        service.updateUserLastLoginToNow(1);
        verify(dao).updateUserLastLoginToNow(1);
    }

    @Test
    public void testUpdateUserLastLoginToNowUserDoesNotExist() throws Exception {
        when(dao.getUserById(anyInt())).thenThrow(NotFoundException.class);

        assertThrows(NotFoundException.class, () -> service.updateUserLastLoginToNow(1));

        verify(dao, never()).updateUserLastLoginToNow(anyInt());
    }

    @Test
    public void testDeleteUserValid() throws Exception {
        User userToDelete = UserFactoryData.userData();
        userToDelete.setWebRole(WebRole.SYSTEM_USER);

        when(dao.getUserById(anyInt())).thenReturn(userToDelete);
        when(jwtHolder.getWebRole()).thenReturn(WebRole.ADMIN);

        service.deleteUser(5);
        verify(dao).deleteUser(5);
    }

    @Test
    public void testDeleteUserUserHasInsufficientPermissions() throws Exception {
        User userToDelete = UserFactoryData.userData();

        when(dao.getUserById(anyInt())).thenReturn(userToDelete);
        when(jwtHolder.getWebRole()).thenReturn(WebRole.ADMIN);

        InsufficientPermissionsException e = assertThrows(InsufficientPermissionsException.class,
                                                          () -> service.deleteUser(5));

        verify(dao, never()).deleteUser(anyInt());
        assertEquals("Your role of 'ADMIN' can not delete a user of role 'ADMIN'", e.getMessage(), "Exception Message");
    }
}
