package com.hydro.utility.factory.annotations;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;

import com.hydro.utility.factory.config.DataSourceTestConfiguration;
import com.hydro.utility.factory.config.test.HydroDAOTestConfig;
import com.hydro.utility.factory.resolver.ActiveProfileDaoTestResolver;

/**
 * Annotation id for test that deal with dao classes.
 * 
 * @author Sam Butler
 * @since July 31, 2021
 */
@Retention(RetentionPolicy.RUNTIME)
@ContextConfiguration(classes = HydroDAOTestConfig.class)
@SpringBootTest(classes = { DataSourceTestConfiguration.class })
@ActiveProfiles(resolver = ActiveProfileDaoTestResolver.class)
public @interface HydroDaoTest {}