package com.hydro.insite_hydro_system_microservice.dao;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Repository;

import com.hydro.common.exception.NotFoundException;
import com.hydro.sql.abstracts.BaseDao;
import com.hydro.sql.builder.SqlParamBuilder;

/**
 * Class that handles all the dao calls to the database for hydro system
 * authentication.
 * 
 * @author Sam Butler
 * @since May 25, 2021
 */
@Repository
public class HydroSystemAuthenticationDAO extends BaseDao {

    @Autowired
    public HydroSystemAuthenticationDAO(DataSource source) {
        super(source);
    }

    /**
     * Get the {@link BCrypt} hashed password for the given system uuid.
     * 
     * @param uuid The uuid assocaited with the system.
     * @return {@link String} of the hashed password.
     * @throws Exception If there is not user for the given uuid.
     */
    public String getSystemAuthPassword(String uuid) throws Exception {
        try {
            return get(getSql("getSystemHashedPassword"), parameterSource(UUID, uuid), String.class);
        }
        catch(Exception e) {
            throw new NotFoundException("System UUID", uuid);
        }
    }

    /**
     * Acknowledges a system verification check with the user.
     * 
     * @param userId The user id to link to the system.
     */
    public void assignUserToSystem(int userId, int systemId) {
        var params = SqlParamBuilder.with().withParam(USER_ID, userId).withParam(ID, systemId).build();
        post(getSql("updateSystemOwner"), params);
    }
}
