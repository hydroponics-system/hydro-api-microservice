package com.hydro.insite_hydro_system_microservice.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Sets;
import com.hydro.common.dictionary.data.HydroSystem;
import com.hydro.common.dictionary.data.PartNumber;
import com.hydro.common.dictionary.enums.WebRole;
import com.hydro.common.environment.AppEnvironmentService;
import com.hydro.common.exception.exceptions.InsufficientPermissionsException;
import com.hydro.common.exception.exceptions.NotFoundException;
import com.hydro.common.jwt.utility.JwtHolder;
import com.hydro.common.logger.HydroLogger;
import com.hydro.insite_common_microservice.util.CommonUtil;
import com.hydro.insite_hydro_system_microservice.client.domain.request.HydroSystemGetRequest;
import com.hydro.insite_hydro_system_microservice.dao.HydroSystemDAO;

import io.jsonwebtoken.lang.Assert;

/**
 * Grow Chamber History class that handles all service calls to the dao
 * 
 * @author Sam Butler
 * @since June 25, 2020
 */
@Transactional
@Service
public class HydroSystemService {

    @Autowired
    private HydroSystemDAO dao;

    @Autowired
    private HydroLogger LOGGER;

    @Autowired
    private AppEnvironmentService appEnvironmentService;

    @Autowired
    private JwtHolder jwtHolder;

    /**
     * Method for getting a list of systems based on the given request.
     * 
     * @param request The hydro get request
     * @return List of {@link HydroSystem} objects.
     */
    public List<HydroSystem> getSystems(HydroSystemGetRequest request) {
        List<HydroSystem> systems = dao.getSystems(request);
        return systems;
    }

    /**
     * Method for getting a system by system id.
     * 
     * @param id The systems unique identifier.
     * @return {@link HydroSystem} that matches the id
     */
    public HydroSystem getSystemById(int id) {
        HydroSystemGetRequest request = new HydroSystemGetRequest();
        request.setId(Sets.newHashSet(id));
        return getSystems(request).stream().findFirst().orElseThrow(() -> new NotFoundException("ID", id));
    }

    /**
     * Method for getting a system by uuid.
     * 
     * @param uuid The systems uuid.
     * @return {@link HydroSystem} that matches the uuid
     */
    public HydroSystem getSystemByUUID(String uuid) {
        HydroSystemGetRequest request = new HydroSystemGetRequest();
        request.setUuid(Sets.newHashSet(uuid));
        return getSystems(request).stream().findFirst().orElseThrow(() -> new NotFoundException("UUID", uuid));
    }

    /**
     * Method for registering a new hydroponic system.
     * 
     * @param systemName The name of the system to be registered.
     * @return {@link HydroSystem} that was registered.
     */
    public HydroSystem registerSystem(HydroSystem system) {
        Assert.hasLength(system.getName(), "Name is required for registering a system.");
        Assert.hasLength(system.getPassword(), "Password is required for registering a system.");

        LOGGER.info("Registering new system with name: '{}'", system.getName());
        HydroSystem sys = buildSystem(dao.getNextSystemId(), system);

        sys.setId(dao.registerSystem(sys));
        sys.setInsertDate(LocalDateTime.now());
        sys.setPassword(null);
        LOGGER.info("New system registered successfully with UUID: '{}'", sys.getUuid());
        return sys;
    }

    /**
     * Unregister a system by the given id. This will confirm that the system being
     * deleted is either by the user that created it or it is of a user with a role
     * of type ADMIN.
     * 
     * @param id The System unique identifier.
     */
    public void unregisterSystem(int id) {
        HydroSystem sys = getSystemById(id);

        if(jwtHolder.getUserId() != sys.getOwnerUserId() && !jwtHolder.getWebRole().equals(WebRole.ADMIN)) {
            throw new InsufficientPermissionsException("Insufficient permissions! You can not unregister this system.");
        }

        dao.unregisterSystem(id);
        LOGGER.info("System successfully unregistered with UUID: '{}'", sys.getUuid());
    }

    /**
     * Builds a hydro system object for the given system id.
     * 
     * @param systemId The system id
     * @return {@link Systen} object.
     */
    private HydroSystem buildSystem(long systemId, HydroSystem system) {
        HydroSystem sys = new HydroSystem();

        String env = appEnvironmentService.getEnvironment().getTextId();
        String pNumber = String.format("%06d%s%06d", CommonUtil.generateRandomNumber(6), env, systemId);
        PartNumber partNumber = new PartNumber(pNumber);

        sys.setName(system.getName());
        sys.setPartNumber(partNumber);
        sys.setUuid(UUID.nameUUIDFromBytes(partNumber.toString().getBytes()).toString());
        sys.setPassword(BCrypt.hashpw(system.getPassword(), BCrypt.gensalt()));
        sys.setOwnerUserId(jwtHolder.getUserId());
        return sys;
    }
}
