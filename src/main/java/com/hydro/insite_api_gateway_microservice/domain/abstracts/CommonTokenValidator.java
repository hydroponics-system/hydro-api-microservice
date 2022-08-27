package com.hydro.insite_api_gateway_microservice.domain.abstracts;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.hydro.common.dictionary.enums.Environment;
import com.hydro.common.environment.AppEnvironmentService;
import com.hydro.common.jwt.utility.JwtHolder;
import com.hydro.common.jwt.utility.JwtTokenUtil;
import com.hydro.insite_api_gateway_microservice.domain.interfaces.BaseRequestValidator;
import com.hydro.insite_exception_microservice.exceptions.JwtTokenException;

/**
 * Common abstract validator for tokens.
 * 
 * @author Sam Butler
 * @since August 26, 2022
 */
@Component
public abstract class CommonTokenValidator implements BaseRequestValidator {
    protected static final String TOKEN_PREFIX = "Bearer:";

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private AppEnvironmentService appEnvironmentService;

    @Autowired
    private JwtHolder jwtHolder;

    /**
     * Will take in a string token and confirm that it is valid. It will check that
     * token is valid (not null) and if the prefix check is enabled it will confirm
     * it has the {@code Bearer:} tag on the token. It will check that it is the
     * correct environment and the fields match what are expected. It will finally
     * check that the token is not expired.
     * 
     * @param token       The token to validate.
     * @param prefixCheck Determines if should check for the {@code Bearer:} prefix.
     * @return boolean confirming if it was a valid token.
     * @throws JwtTokenException If the token is invalid.
     */
    protected void runTokenValidation(String token, boolean prefixCheck) {
        String extractedToken = extractToken(token);

        checkValidToken(token, prefixCheck);
        checkCorrectEnvironment(extractedToken);
        checkCorrectFields(extractedToken);
        checkTokenExpiration(extractedToken);
    }

    /**
     * Boolean method that determines if a request should be filtered or not. It
     * will process the antmatchers and determine the result.
     * 
     * @param request  The request to validate
     * @param matchers The matchers to check against.
     * @return {@link Boolean} of the filter status.
     */
    protected boolean shouldNotFilter(HttpServletRequest request, List<AntPathRequestMatcher> matchers) {
        return matchers.stream().anyMatch(matcher -> matcher.matches(request));
    }

    /**
     * Checks to see if the passed in token contains the required bearer prefix.
     * 
     * @param rawToken The token to validate.
     * @return Boolean if the token contains the prefix or not.
     */
    protected boolean containsBearerPrefix(String rawToken) {
        return StringUtils.hasText(rawToken) && rawToken.startsWith(TOKEN_PREFIX);
    }

    /**
     * Runs validation that the token exist. If it does not exist then it will throw
     * an exception with the appropriate message.
     * 
     * @param token       The token to validate.
     * @param prefixCheck Determines if should check for the {@code Bearer:} prefix.
     * @throws JwtTokenException If the token is invalid.
     */
    protected void checkValidToken(String token, boolean prefixCheck) {
        if(!StringUtils.hasText(token)) {
            throw new JwtTokenException("Missing JWT Token.");
        }
        else if(prefixCheck && !containsBearerPrefix(token)) {
            throw new JwtTokenException("JWT Token does not begin with 'Bearer:'");
        }
    }

    /**
     * Validates that the environemnt is correct. If the environment signed on the
     * token does not match the application the request is asking for then it will
     * throw an exception.
     *
     * @param token to be parsed
     * @throws JwtTokenException If the token is invalid.
     */
    protected void checkCorrectEnvironment(String token) {
        Environment environment = Environment.valueOf(jwtTokenUtil.getAllClaimsFromToken(token).get("env").toString());

        if(!appEnvironmentService.getEnvironment().equals(environment)) {
            throw new JwtTokenException("JWT token doesn't match accessing environment!");
        }
    }

    /**
     * Checks to see if it has the required fields on the token. If the tokens can
     * not be parsed and the expected required fields are not on the token then it
     * will throw an exception that it could not be parsed.
     *
     * @param token - Token to confirm field claims on
     * @throws JwtTokenException If the token is invalid.
     */
    protected void checkCorrectFields(String token) {
        try {
            jwtTokenUtil.getExpirationDateFromToken(token);
            jwtTokenUtil.getAllClaimsFromToken(token);
        }
        catch(Exception e) {
            throw new JwtTokenException("Could not process JWT token. Invalid signature!");
        }
    }

    /**
     * Checks to see if the users token is expired. If the token is no longer valid
     * and is passed the expiration time, it will throw an exception.
     * 
     * @param token The token to validate.
     * @throws JwtTokenException If the token is expired.
     */
    protected void checkTokenExpiration(String token) {
        if(jwtTokenUtil.isTokenExpired(token)) {
            throw new JwtTokenException("JWT Token is expired! Please re-authenticate.");
        }
    }

    /**
     * Extracts out the token from the headers in the request. It will remove the
     * {@code Bearer:} prefix from the header and retrurn just the token. If the
     * token header is null or does not contain text then it will return null. If
     * the token does not contain the prefix then it will return the same token that
     * was passed.
     * 
     * @param tokenHeader The token to parse.
     * @return The token value.
     */
    protected String extractToken(String tokenHeader) {
        return StringUtils.hasText(tokenHeader) ? tokenHeader.replace(TOKEN_PREFIX, "").trim() : null;
    }

    /**
     * Sets the token to the current thread local instance.
     * 
     * @param token The token to store.
     */
    protected void storeToken(String token) {
        jwtHolder.setToken(extractToken(token));
    }
}
