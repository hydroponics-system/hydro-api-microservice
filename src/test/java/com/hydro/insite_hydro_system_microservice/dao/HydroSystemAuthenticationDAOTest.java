package com.hydro.insite_hydro_system_microservice.dao;

import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;

import com.hydro.test.factory.annotations.HydroDaoTest;
import com.hydro.utility.HydroDAOTestConfig;

/**
 * Test class for the Hydro System Authentication DAO.
 * 
 * @author Sam Butler
 * @since August 23, 2022
 */
@Sql("/scripts/system/hydrosystemDAO/init.sql")
@ContextConfiguration(classes = HydroDAOTestConfig.class)
@HydroDaoTest
public class HydroSystemAuthenticationDAOTest {

}
