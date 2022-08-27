package com.hydro.utility.factory.resolver;

import java.util.Map;

import org.springframework.test.context.ActiveProfilesResolver;

import com.hydro.utility.factory.globals.GlobalsTest;

/**
 * Resolver method that decides what property file to use when running test for
 * REST classes.
 * 
 * @author Sam Butler
 * @since April 25, 2022
 */
public class ActiveProfileRestTestResolver implements ActiveProfilesResolver {

    @Override
    public String[] resolve(Class<?> testClass) {
        Map<String, String> env = System.getenv();
        return new String[] { env.containsKey("APP_ENVIRONMENT") ? GlobalsTest.PRODUCTION_TEST
                : GlobalsTest.LOCAL_TEST };
    }
}