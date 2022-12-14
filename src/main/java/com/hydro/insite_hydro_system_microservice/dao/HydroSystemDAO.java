package com.hydro.insite_hydro_system_microservice.dao;

import static com.hydro.insite_hydro_system_microservice.mapper.HydroSystemMapper.*;

import java.util.List;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import com.hydro.common.dictionary.data.HydroSystem;
import com.hydro.insite_hydro_system_microservice.client.domain.request.HydroSystemGetRequest;
import com.hydro.sql.abstracts.BaseDao;
import com.hydro.sql.builder.SqlParamBuilder;

/**
 * Class that handles all the dao calls to the database for grow chamber logs.
 * 
 * @author Sam Butler
 * @since May 25, 2021
 */
@Repository
public class HydroSystemDAO extends BaseDao {

    @Autowired
    public HydroSystemDAO(DataSource source) {
        super(source);
    }

    /**
     * Method for getting a list of systems based on the given request.
     * 
     * @param request The hydro get request
     * @return List of {@link HydroSystem} objects.
     */
    public List<HydroSystem> getSystems(HydroSystemGetRequest request) {
        var params = SqlParamBuilder.with().withParam(ID, request.getId()).withParam(UUID, request.getUuid())
                .withParam(PART_NUMBER, request.getPartNumber()).withParam(NAME, request.getName()).build();

        return getPage(getSql("getSystems", params), params, HYDRO_SYSTEM_MAPPER);
    }

    /**
     * Method for registering a new hydroponic system.
     * 
     * @param systemName The name of the system to be registered.
     * @return {@link Integer} of the unique identifer of the system.
     */
    public int registerSystem(HydroSystem sys) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        MapSqlParameterSource params = SqlParamBuilder.with().withParam(UUID, sys.getUuid())
                .withParam(PART_NUMBER, sys.getPartNumber().toString()).withParam(NAME, sys.getName())
                .withParam(PASSWORD, sys.getPassword()).build();

        post(getSql("registerSystem", params), params, keyHolder);
        return keyHolder.getKey().intValue();
    }

    /**
     * Unregister a system by the given uuid. This will confirm that the system
     * being deleted is either by the user that created it or it is of a user with a
     * role of type ADMIN.
     * 
     * @param uuid The System unique identifier.
     */
    public void unregisterSystem(int id) {
        var params = parameterSource(ID, id);
        delete(getSql("unregisterSystem", params), params);
    }

    /**
     * Gets the next auto increment id of a system that will be inserted.
     * 
     * @return {@link Long} of the next auto increment id of a system.
     */
    public long getNextSystemId() {
        return get(getSql("nextSystemId"), Long.class);
    }
}
