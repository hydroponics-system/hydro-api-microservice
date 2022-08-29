package com.hydro.insite_auth_microservice.rest;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.hydro.common.annotations.interfaces.RestApiController;
import com.hydro.common.dictionary.data.User;
import com.hydro.common.environment.AppEnvironmentService;
import com.hydro.insite_auth_microservice.client.domain.AuthToken;
import com.hydro.insite_auth_microservice.client.domain.request.AuthenticationRequest;
import com.hydro.insite_auth_microservice.openapi.TagAuthentication;
import com.hydro.insite_auth_microservice.service.AuthenticationService;

import io.swagger.v3.oas.annotations.Operation;

/**
 * Generates a JWT after being passed a request
 *
 * @author Sam Butler
 * @since August 1, 2021
 */
@RequestMapping("/api")
@RestApiController
@TagAuthentication
public class AuthenticationController {

    private static final Logger LOGGER = LoggerFactory.getLogger(AuthenticationController.class);

    @Autowired
    private AuthenticationService service;

    @Autowired
    private AppEnvironmentService environmentService;

    /**
     * Generates a JWT token from a request
     *
     * @param authenticationRequest A email and password request.
     * @return a JWT token.
     * @throws Exception if authentication request does not match a user.
     */
    @Operation(summary = "Authentication for a user", description = "Generates a unique JWT token for an authenticated user.")
    @PostMapping(path = "/authenticate", produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<AuthToken<User>> authenticateUser(@RequestBody AuthenticationRequest authenticationRequest)
            throws Exception {
        LOGGER.info("Environment Signing Key: '{}'", environmentService.getSigningKey());
        return ResponseEntity.ok(service.authenticate(authenticationRequest));
    }

    /**
     * Reauthenticates a user and generate a new token.
     *
     * @param authenticationRequest A email and password request.
     * @return a new JWT token.
     * @throws Exception If user does not exist.
     */
    @Operation(summary = "Re-authenticate a user", description = "Will re-authenticate a user. An existing token is required.")
    @PostMapping(path = "/reauthenticate", produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<AuthToken<User>> reauthenticateUser() throws Exception {
        return ResponseEntity.ok(service.reauthenticate());
    }
}
