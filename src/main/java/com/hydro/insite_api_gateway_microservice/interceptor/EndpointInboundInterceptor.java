package com.hydro.insite_api_gateway_microservice.interceptor;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;

import org.springframework.beans.factory.annotation.Autowired;

import com.hydro.insite_api_gateway_microservice.domain.abstracts.CommonInterceptor;
import com.hydro.insite_api_gateway_microservice.validator.EndpointInboundValidator;

/**
 * All endpoint request will be filtered through this class. It determines if
 * the request is allowed to be made or not.
 * 
 * @author Sam butler
 * @since Aug 6, 2021
 */
@WebFilter(urlPatterns = "/api/*")
public class EndpointInboundInterceptor extends CommonInterceptor {

    @Autowired
    private EndpointInboundValidator validator;

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
            throws IOException, ServletException {
        performFilter(validator, req, res, chain);
    }
}