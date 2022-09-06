package com.hydro.insite_subscription_microservice.client.domain;

import java.security.Principal;

import com.hydro.common.dictionary.data.HydroSystem;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * System principal for identifying systems on a websocket.
 * 
 * @author Sam Butler
 * @since March 24, 2022
 */
@Schema(description = "System Principal for subscription identification.")
public class SystemPrincipal implements Principal {

    @Schema(description = "The unique identifer of the use session")
    private String name;

    @Schema(description = "The system attached to the session.")
    private HydroSystem system;

    public SystemPrincipal(String name, HydroSystem system) {
        this.name = name;
        this.system = system;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public HydroSystem getSystem() {
        return system;
    }

    public void setSystem(HydroSystem system) {
        this.system = system;
    }
}
