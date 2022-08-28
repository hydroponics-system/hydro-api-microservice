package com.hydro.insite_hydro_system_microservice.client;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;

import com.hydro.common.annotations.interfaces.Client;
import com.hydro.common.dictionary.data.HydroSystem;
import com.hydro.insite_hydro_system_microservice.client.domain.request.HydroSystemGetRequest;
import com.hydro.insite_hydro_system_microservice.service.HydroSystemService;

/**
 * Client method for Grow chamber history.
 * 
 * @author Sam Butler
 * @since July 31, 2021
 */
@Client
public class HydroSystemClient {

    @Lazy
    @Autowired
    private HydroSystemService service;

    /**
     * Method for getting a list of systems based on the given request.
     * 
     * @param request The hydro get request
     * @return List of {@link HydroSystem} objects.
     */
    public List<HydroSystem> getSystems(HydroSystemGetRequest request) {
        return service.getSystems(request);
    }
}
