package com.hydro.insite_auth_microservice.dao;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.test.context.jdbc.Sql;

import com.hydro.common.exception.NotFoundException;
import com.hydro.utility.factory.annotations.HydroDaoTest;

/**
 * Test class for the Authenticate DAO.
 * 
 * @author Sam Butler
 * @since August 23, 2022
 */
@Sql("/scripts/auth/authenticationDAO/init.sql")
@HydroDaoTest
public class AuthenticationDAOTest {

    @Autowired
    private AuthenticationDAO dao;

    @Test
    public void testGetUserAuthPasswordValidEmail() throws Exception {
        String hashedPass = dao.getUserAuthPassword("test@mail.com");

        assertNotNull(hashedPass, "Hashed Password not null");
        assertTrue(BCrypt.checkpw("testPassword", hashedPass), "Passwords should match");
    }

    @Test
    public void testGetUserAuthPasswordEmailNotFound() {
        NotFoundException e = assertThrows(NotFoundException.class, () -> dao.getUserAuthPassword("notFound@mail.com"));
        assertEquals("User Email not found for id: 'notFound@mail.com'", e.getMessage(), "Message should match");
    }
}
