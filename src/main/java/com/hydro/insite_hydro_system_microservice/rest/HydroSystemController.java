package com.hydro.insite_hydro_system_microservice.rest;

import static org.springframework.http.MediaType.*;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.hydro.common.annotations.interfaces.RestApiController;
import com.hydro.common.dictionary.data.HydroSystem;
import com.hydro.insite_hydro_system_microservice.client.domain.request.HydroSystemGetRequest;
import com.hydro.insite_hydro_system_microservice.openapi.TagHydroSystem;
import com.hydro.insite_hydro_system_microservice.service.HydroSystemService;
import com.hydro.insite_subscription_microservice.notification.SystemLinkNotification;

import io.swagger.v3.oas.annotations.Operation;

@RequestMapping("/api/system-app/system")
@RestApiController
@TagHydroSystem
public class HydroSystemController {

    @Autowired
    private HydroSystemService service;

    /**
     * Method for getting a list of systems based on the given request.
     * 
     * @param request The hydro get request
     * @return List of {@link HydroSystem} objects.
     */
    @Operation(summary = "Get a list of hydro systems.", description = "Will get a list of hydro systems based on the give HydroGetRequest.")
    @GetMapping(produces = APPLICATION_JSON_VALUE)
    public List<HydroSystem> getSystems(HydroSystemGetRequest request) {
        return service.getSystems(request);
    }

    /**
     * Method for getting a system by id.
     * 
     * @param uuid The systems unique identifier.
     * @return {@link HydroSystem} that matches the id
     */
    @Operation(summary = "Get a system by unique identifier.", description = "Will get the system by the system id.")
    @GetMapping(value = "/{id}", produces = APPLICATION_JSON_VALUE)
    public HydroSystem getSystemById(@PathVariable int id) {
        return service.getSystemById(id);
    }

    /**
     * Method for getting a system by uuid.
     * 
     * @param uuid The systems uuid.
     * @return {@link HydroSystem} that matches the uuid
     */
    @Operation(summary = "Get a system by unique identifier.", description = "Will get the system by the uuid.")
    @GetMapping(value = "/{uuid}/uuid", produces = APPLICATION_JSON_VALUE)
    public HydroSystem getSystemByUUID(@PathVariable String uuid) {
        return service.getSystemByUUID(uuid);
    }

    /**
     * Method for registering a new hydroponic system. It will require the name and
     * password of the system to be set in order to register.
     * 
     * @param sys The system object to register.
     * @param key The registration key for creating a system.
     * @return {@link HydroSystem} that was registered.
     */
    @Operation(summary = "Create a new entry for a system", description = "Will register a new system under the given name.")
    @PostMapping(value = "/register", produces = APPLICATION_JSON_VALUE)
    public HydroSystem registerSystem(@RequestBody HydroSystem sys) {
        return service.registerSystem(sys);
    }

    /**
     * Request process to link a user to a system.
     * 
     * @param request The request to link a system.
     * @return {@link SystemLinkNotification} the system link request with the code
     */
    @Operation(summary = "Request to link user to a system", description = "Will request to link a user to a system.")
    @PostMapping(value = "/link", produces = APPLICATION_JSON_VALUE)
    public SystemLinkNotification systemLinkRequest(@RequestBody SystemLinkNotification request) {
        return service.systemLinkRequest(request);
    }

    /**
     * Unregister a system by the given system id.
     * 
     * @param id The System unique identifier.
     */
    @Operation(summary = "Unregister a system.", description = "Takes in a system id and will unregister the system.")
    @DeleteMapping(value = "/{systemId}/unregister", produces = APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public void unregisterSystem(@PathVariable int systemId) {
        service.unregisterSystem(systemId);
    }
}
