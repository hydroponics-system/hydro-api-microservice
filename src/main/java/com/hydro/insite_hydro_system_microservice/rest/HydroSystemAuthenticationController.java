package com.hydro.insite_hydro_system_microservice.rest;

import static org.springframework.http.MediaType.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.hydro.common.annotations.interfaces.HasAccess;
import com.hydro.common.annotations.interfaces.RestApiController;
import com.hydro.common.dictionary.data.HydroSystem;
import com.hydro.common.dictionary.enums.WebRole;
import com.hydro.insite_auth_microservice.client.domain.AuthToken;
import com.hydro.insite_hydro_system_microservice.client.domain.request.SystemAuthenticationRequest;
import com.hydro.insite_hydro_system_microservice.openapi.TagHydroSystem;
import com.hydro.insite_hydro_system_microservice.service.HydroSystemAuthenticationService;

import io.swagger.v3.oas.annotations.Operation;

@RequestMapping("/api/system-app")
@RestApiController
@TagHydroSystem
public class HydroSystemAuthenticationController {

    @Autowired
    private HydroSystemAuthenticationService service;

    /**
     * Generates a JWT token from a request
     *
     * @param authRequest A uuid and password request.
     * @return a JWT token.
     * @throws Exception if authentication request does not match a system.
     */
    @Operation(summary = "Authentication for a hydro system", description = "Generates a unique JWT token for an authenticated system.")
    @PostMapping(path = "/authenticate", produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<AuthToken<HydroSystem>> authenticateSystem(
            @RequestBody SystemAuthenticationRequest authRequest) throws Exception {
        return ResponseEntity.ok(service.authenticate(authRequest));
    }

    /**
     * Acknowledges a system verification check with the user.
     * 
     * @param userId The user id to link to the system.
     * @throws Exception If the user does not exist for that id.
     */
    @Operation(summary = "Verifies a user connection to a system", description = "Takes a user id to link a system to a user since the verification was successful")
    @PostMapping(path = "/verification/user/{userId}")
    @HasAccess(WebRole.SYSTEM)
    public void acknowledgeSystemVerification(@PathVariable int userId) throws Exception {
        service.acknowledgeSystemVerification(userId);
    }
}
