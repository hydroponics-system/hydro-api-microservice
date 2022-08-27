package com.hydro.insite_hydro_system_microservice.dao;

import org.springframework.test.context.jdbc.Sql;

import com.hydro.utility.factory.annotations.HydroDaoTest;

/**
 * Test class for the Hydro System Authentication DAO.
 * 
 * @author Sam Butler
 * @since August 23, 2022
 */
@Sql("/scripts/system/hydrosystemDAO/init.sql")
@HydroDaoTest
public class HydroSystemAuthenticationDAOTest {

}
