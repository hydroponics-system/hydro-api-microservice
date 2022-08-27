package com.hydro.insite_hydro_system_microservice.client.domain.request;

import java.io.Serializable;

import javax.annotation.Nonnull;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * AuthenticationRequest for authenticating and updating System credentials.
 *
 * @author Sam Butler
 * @since August 1, 20222
 */
@Schema(description = "System Authentication Request")
public class SystemAuthenticationRequest implements Serializable {

    @Schema(description = "The uuid to authenticate with.")
    @Nonnull
    private String uuid;

    @Schema(description = "The password associated with the uid.")
    @Nonnull
    private String password;

    public SystemAuthenticationRequest() {}

    public SystemAuthenticationRequest(String uuid, String password) {
        this.uuid = uuid;
        this.password = password;
    }

    public String getUuid() {
        return this.uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getPassword() {
        return this.password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
