package com.hydro.insite_user_microservice.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import com.hydro.common.dictionary.data.User;
import com.hydro.common.exception.NotFoundException;
import com.hydro.common.jwt.utility.JwtHolder;
import com.hydro.insite_user_microservice.client.domain.request.UserGetRequest;
import com.hydro.insite_user_microservice.dao.UserProfileDAO;
import com.hydro.test.factory.annotations.HydroServiceTest;
import com.hydro.test.factory.data.UserFactoryData;

/**
 * Test class for the User Profile Service.
 * 
 * @author Sam Butler
 * @since August 23, 2022
 */
@HydroServiceTest
public class UserProfileServiceTest {

    @Mock
    private JwtHolder jwtHolder;

    @Mock
    private UserProfileDAO userProfileDAO;

    @InjectMocks
    private UserProfileService service;

    @Test
    public void testGetUsers() {
        User user1 = UserFactoryData.userData();
        User user2 = new User();
        user2.setId(2);

        when(userProfileDAO.getUsers(any(UserGetRequest.class))).thenReturn(Arrays.asList(user1, user2));

        List<User> returnedUser = service.getUsers(new UserGetRequest());

        verify(userProfileDAO).getUsers(any(UserGetRequest.class));
        assertEquals(user1.getId(), returnedUser.get(0).getId(), "User 1 id should be 12");
        assertEquals(user2.getId(), returnedUser.get(1).getId(), "User 2 id should be 2");
    }

    @Test
    public void testGetCurrentUser() throws Exception {
        User user = UserFactoryData.userData();
        when(jwtHolder.getUserId()).thenReturn(12);
        when(userProfileDAO.getUserById(anyInt())).thenReturn(user);

        User returnedUser = service.getCurrentUser();

        verify(jwtHolder).getUserId();
        verify(userProfileDAO).getUserById(eq(12));
        assertEquals(user.getId(), returnedUser.getId(), "User id should be 12");
    }

    @Test
    public void testGetUserById() throws Exception {
        User user = UserFactoryData.userData();
        when(userProfileDAO.getUserById(anyInt())).thenReturn(user);

        User returnedUser = service.getUserById(12);

        verify(userProfileDAO).getUserById(eq(12));
        assertEquals(user.getId(), returnedUser.getId(), "User id should be 12");
    }

    @Test
    public void testGetUserByIdInvalidUserId() throws Exception {
        when(userProfileDAO.getUserById(anyInt())).thenThrow(new NotFoundException("User", 100));

        assertThrows(NotFoundException.class, () -> service.getUserById(100));
        verify(userProfileDAO).getUserById(eq(100));
    }

    @Test
    public void testDoesEmailExist() {
        when(userProfileDAO.getUsers(any(UserGetRequest.class))).thenReturn(Arrays.asList(UserFactoryData.userData()));
        assertTrue(service.doesEmailExist("test@user.com"), "Email should exist");
    }

    @Test
    public void testDoesEmailExistNotResults() {
        when(userProfileDAO.getUsers(any(UserGetRequest.class))).thenReturn(Arrays.asList());
        assertFalse(service.doesEmailExist("test@user.com"), "Email does not exist");
    }
}
