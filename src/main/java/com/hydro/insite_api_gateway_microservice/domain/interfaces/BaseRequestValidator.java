package com.hydro.insite_api_gateway_microservice.domain.interfaces;

import javax.servlet.http.HttpServletRequest;

/**
 * Base interface for request validators.
 * 
 * @author Sam Butler
 * @since August 24, 2022
 */
public interface BaseRequestValidator {

    /**
     * Method to check to see if the request is valid.
     *
     * @param request The request that is being made to the endpint
     * @return {@link Boolean} of the validation status of the request.
     */
    public void validateRequest(HttpServletRequest req);
}
