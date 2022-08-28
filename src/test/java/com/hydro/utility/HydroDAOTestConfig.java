package com.hydro.utility;

import org.springframework.context.annotation.ComponentScan;

/**
 * Defines the the base component scan packages for the DAO.
 * 
 * @author Sam Butler
 * @since April 25, 2022
 */
@ComponentScan(basePackages = { "com.hydro.insite_hydro_system_microservice.dao",
                                "com.hydro.insite_user_microservice.dao", "com.hydro.insite_auth_microservice.dao" })
public class HydroDAOTestConfig {}