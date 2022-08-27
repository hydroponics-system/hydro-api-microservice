package com.hydro.insite_common_microservice.annotations.interfaces;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import org.springframework.stereotype.Component;

import com.hydro.common.dictionary.enums.WebRole;
import com.hydro.common.jwt.utility.JwtTokenUtil;

/**
 * Annotation that will add an authorization token to the headers for rest
 * endpoint request. If this annotation is used without setting values then the
 * defaults will take place. It will call {@link JwtTokenUtil#generateToken()}
 * to create a Authorization token to appended with the endpoint request.
 * 
 * @author Sam Butler
 * @since July 31, 2021
 */
@Retention(RetentionPolicy.RUNTIME)
@Component
public @interface ControllerJwt {
    int userId() default 1;

    String firstName() default "Auth";

    String lastName() default "User";

    String email() default "test@user.com";

    WebRole webRole() default WebRole.ADMIN;
}