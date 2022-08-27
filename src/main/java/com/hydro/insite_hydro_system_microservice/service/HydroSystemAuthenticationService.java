package com.hydro.insite_hydro_system_microservice.service;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Sets;
import com.hydro.common.dictionary.data.HydroSystem;
import com.hydro.common.jwt.utility.JwtTokenUtil;
import com.hydro.insite_auth_microservice.client.domain.AuthToken;
import com.hydro.insite_exception_microservice.exceptions.InvalidSystemCredentials;
import com.hydro.insite_hydro_system_microservice.client.HydroSystemClient;
import com.hydro.insite_hydro_system_microservice.client.domain.request.HydroSystemGetRequest;
import com.hydro.insite_hydro_system_microservice.client.domain.request.SystemAuthenticationRequest;
import com.hydro.insite_hydro_system_microservice.dao.HydroSystemAuthenticationDAO;

@Transactional
@Service
public class HydroSystemAuthenticationService {

    @Autowired
    private HydroSystemAuthenticationDAO dao;

    @Autowired
    private HydroSystemClient hydroSystemClient;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    /**
     * Generates a JWT token from a request
     *
     * @param request A uuid and password request.
     * @return a new JWT.
     * @throws Exception - if authentication request does not match a system.
     */
    public AuthToken<HydroSystem> authenticate(SystemAuthenticationRequest request) throws Exception {
        HydroSystem system = verifySystem(request.getUuid(), request.getPassword());

        String token = jwtTokenUtil.generateToken(system);
        return new AuthToken<HydroSystem>(token, LocalDateTime.now(), jwtTokenUtil.getExpirationDateFromToken(token),
                                          system);
    }

    /**
     * Verifies system credentials.
     *
     * @param uuid     Entered uuid at login.
     * @param password Password entered at login.
     * @throws Exception Throw an exception if the credentials do not match.
     */
    private HydroSystem verifySystem(String uuid, String password) throws Exception {
        if(BCrypt.checkpw(password, dao.getSystemAuthPassword(uuid))) {
            return getAuthenticatedSystem(uuid);
        }
        else {
            throw new InvalidSystemCredentials(uuid);
        }
    }

    /**
     * Get a user based on their email address. Used when a user has sucessfully
     * authenticated.
     * 
     * @param email The email to search for.
     * @return {@link HydroSystem} object of the authenticated system.
     * @throws Exception
     */
    private HydroSystem getAuthenticatedSystem(String uuid) throws Exception {
        HydroSystemGetRequest request = new HydroSystemGetRequest();
        request.setUuid(Sets.newHashSet(uuid));
        return hydroSystemClient.getSystems(request).get(0);
    }
}
