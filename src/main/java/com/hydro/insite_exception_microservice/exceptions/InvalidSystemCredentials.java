package com.hydro.insite_exception_microservice.exceptions;

/**
 * Exception thrown when user has invalid System credentials.
 * 
 * @author Sam Butler
 * @since July 31, 2021
 */
public class InvalidSystemCredentials extends BaseException {
    public InvalidSystemCredentials(String uuid) {
        super(String.format("Invalid Credentials for system UUID: '%s'", uuid));
    }
}