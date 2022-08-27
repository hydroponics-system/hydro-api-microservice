package com.hydro.insite_api_gateway_microservice.interceptor;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

import javax.servlet.http.HttpServletRequest;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.http.HttpStatus;

import com.hydro.common.jwt.utility.JwtHolder;
import com.hydro.insite_api_gateway_microservice.validator.EndpointInboundValidator;
import com.hydro.insite_common_microservice.annotations.interfaces.ControllerJwt;
import com.hydro.utility.factory.abstracts.BaseControllerTest;
import com.hydro.utility.factory.annotations.HydroRestTest;

/**
 * Test for Endpoint Inbound Interceptor.
 * 
 * @author Sam Butler
 * @since August 25, 2022
 */
@HydroRestTest
@ControllerJwt
public class EndpointInboundInterceptorTest extends BaseControllerTest {

    @SpyBean
    private EndpointInboundValidator endpointValidator;

    @SpyBean
    private JwtHolder jwtHolder;

    @Test
    public void testDoFilterForInvalidRequestPath() {
        check(get("/invalid"), error(HttpStatus.NOT_FOUND));
        verify(endpointValidator, never()).validateRequest(any());
    }

    @Test
    public void testDoFilterValidRequestPath() {
        check(get("/api/test"), error(HttpStatus.NOT_FOUND));
        verify(endpointValidator).validateRequest(any(HttpServletRequest.class));
        verify(jwtHolder).clearToken();
    }
}
