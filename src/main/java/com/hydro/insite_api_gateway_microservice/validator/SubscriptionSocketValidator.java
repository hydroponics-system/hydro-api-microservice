package com.hydro.insite_api_gateway_microservice.validator;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Component;

import com.hydro.insite_api_gateway_microservice.domain.abstracts.CommonTokenValidator;
import com.hydro.insite_exception_microservice.exceptions.JwtTokenException;

/**
 * Subscription token validator for confirming a token on a request header.
 *
 * @author Sam butler
 * @since Dec 5, 2020
 */
@Component
public class SubscriptionSocketValidator extends CommonTokenValidator {

    /**
     * Checks to see if the token on the request is valid. If it is not valid then
     * it will throw an exception, otherwise it wil continue. It will confirm that
     * the token is in the right environment, check that it has the correct fields,
     * that it is not expired, and the token signature is valid.
     *
     * @param request The request that is being made to the endpint
     * @return {@link Boolean} of the validation status of the request.
     * @throws JwtTokenException If the jwt token is not valid.
     */
    public void validateRequest(HttpServletRequest request) throws JwtTokenException {
        final String jwtToken = request.getQueryString();
        runTokenValidation(jwtToken, false);
        storeToken(jwtToken);
    }
}
