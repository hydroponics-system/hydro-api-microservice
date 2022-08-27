package com.hydro.insite_api_gateway_microservice.domain.abstracts;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerExceptionResolver;

import com.hydro.common.jwt.utility.JwtHolder;
import com.hydro.insite_api_gateway_microservice.domain.interfaces.BaseRequestValidator;

/**
 * Common interceptor to extend common functionality for API's.
 * 
 * @author Sam butler
 * @since Aug 6, 2021
 */
@Component
public abstract class CommonInterceptor implements Filter {

    @Autowired
    private JwtHolder jwtHolder;

    @Autowired
    @Qualifier("handlerExceptionResolver")
    private HandlerExceptionResolver resolver;

    /**
     * Performs a validate request on the given params and decides if it will
     * perform the filter chain. This request will be used to validate that it can
     * access the endpoint and it has the correct permissions.
     *
     * @param v     The validator to perform.
     * @param req   The request to pass to the function.
     * @param res   The response to parse.
     * @param chain The filter chain to perform on the request.
     */
    protected void performFilter(BaseRequestValidator v, ServletRequest req, ServletResponse res, FilterChain chain) {
        try {
            v.validateRequest((HttpServletRequest) req);
            chain.doFilter(req, res);
        }
        catch(Exception e) {
            resolveException(req, res, e);
        }
        finally {
            clearThreadToken();
        }
    }

    /**
     * Clears the current process token stored on the local thread of the request
     * instance.
     */
    private void clearThreadToken() {
        jwtHolder.clearToken();
    }

    /**
     * Method to resolve exceptions that occur when filtering the request.
     * 
     * @param req The request that is passed.
     * @param res The response to validate.
     * @param e   The exception that was thrown.
     */
    private void resolveException(ServletRequest req, ServletResponse res, Exception e) {
        resolver.resolveException((HttpServletRequest) req, getAllowedOriginResponse(res), null, e);
    }

    /**
     * Appends the allowed origins to the response.
     * 
     * @param res The response to modify.
     * @return The updated response.
     */
    private HttpServletResponse getAllowedOriginResponse(ServletResponse res) {
        HttpServletResponse r = (HttpServletResponse) res;
        r.setHeader("Access-Control-Allow-Origin", "*");
        return r;
    }
}