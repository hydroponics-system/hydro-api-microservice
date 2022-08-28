package com.hydro.insite_hydro_system_microservice.service;

import static com.hydro.test.factory.data.HydroSystemFactoryData.hydroSystem;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.Collections;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import com.hydro.common.dictionary.data.HydroSystem;
import com.hydro.common.dictionary.enums.Environment;
import com.hydro.common.dictionary.enums.WebRole;
import com.hydro.common.environment.AppEnvironmentService;
import com.hydro.common.exception.InsufficientPermissionsException;
import com.hydro.common.exception.NotFoundException;
import com.hydro.common.jwt.utility.JwtHolder;
import com.hydro.common.jwt.utility.JwtTokenUtil;
import com.hydro.insite_hydro_system_microservice.client.domain.request.HydroSystemGetRequest;
import com.hydro.insite_hydro_system_microservice.dao.HydroSystemDAO;
import com.hydro.test.factory.annotations.HydroServiceTest;

/**
 * Test class for the Hydro Service.
 * 
 * @author Sam Butler
 * @since August 23, 2022
 */
@HydroServiceTest
public class HydroSystemServiceTest {

    @Mock
    private HydroSystemDAO hydroSystemDAO;

    @Mock
    private JwtTokenUtil jwtTokenUtil;

    @Mock
    private AppEnvironmentService appEnvironmentService;

    @Mock
    private JwtHolder jwtHolder;

    @InjectMocks
    private HydroSystemService service;

    @Test
    public void testGetSystems() {
        service.getSystems(new HydroSystemGetRequest());

        verify(hydroSystemDAO).getSystems(any(HydroSystemGetRequest.class));
    }

    @Test
    public void testGetSystemByIdExists() {
        when(hydroSystemDAO.getSystems(any(HydroSystemGetRequest.class))).thenReturn(Arrays.asList(hydroSystem()));

        HydroSystem sys = service.getSystemById(1);

        verify(hydroSystemDAO).getSystems(any(HydroSystemGetRequest.class));
        assertEquals(1, sys.getId(), "System ID");
    }

    @Test
    public void testGetSystemByIdNotFound() {
        when(hydroSystemDAO.getSystems(any(HydroSystemGetRequest.class))).thenReturn(Collections.emptyList());

        NotFoundException e = assertThrows(NotFoundException.class, () -> service.getSystemById(12));

        verify(hydroSystemDAO).getSystems(any(HydroSystemGetRequest.class));
        assertEquals("ID not found for id: '12'", e.getMessage(), "Exception Message");
    }

    @Test
    public void testGetSystemByUUIDExists() {
        when(hydroSystemDAO.getSystems(any(HydroSystemGetRequest.class))).thenReturn(Arrays.asList(hydroSystem()));

        HydroSystem sys = service.getSystemByUUID("71d9ec65-265b-3388-a6e4-654128dr5678");

        verify(hydroSystemDAO).getSystems(any(HydroSystemGetRequest.class));
        assertEquals("71d9ec65-265b-3388-a6e4-654128dr5678", sys.getUuid(), "System ID");
    }

    @Test
    public void testGetSystemByUUIDNotFound() {
        when(hydroSystemDAO.getSystems(any(HydroSystemGetRequest.class))).thenReturn(Collections.emptyList());

        NotFoundException e = assertThrows(NotFoundException.class, () -> service.getSystemByUUID("invalid"));

        verify(hydroSystemDAO).getSystems(any(HydroSystemGetRequest.class));
        assertEquals("UUID not found for id: 'invalid'", e.getMessage(), "Exception Message");
    }

    @Test
    public void testRegisterSystem() {
        when(hydroSystemDAO.registerSystem(any(HydroSystem.class))).thenReturn(10);
        when(hydroSystemDAO.getNextSystemId()).thenReturn(10L);
        when(appEnvironmentService.getEnvironment()).thenReturn(Environment.DEVELOPMENT);
        when(jwtHolder.getUserId()).thenReturn(12);

        HydroSystem s = service.registerSystem(new HydroSystem("testSystem", "fakePassword"));

        verify(hydroSystemDAO).registerSystem(any(HydroSystem.class));
        assertEquals(10, s.getId(), "System Id");
        assertEquals("testSystem", s.getName(), "System Name");
        assertEquals(Environment.DEVELOPMENT, s.getPartNumber().getEnvironment(), "Part Number Environment");
        assertEquals(10, s.getPartNumber().getSystemId(), "Part Number System Id");
        assertEquals(UUID.nameUUIDFromBytes(s.getPartNumber().toString().getBytes()).toString(), s.getUuid(), "UUID");
        assertEquals(12, s.getOwnerUserId(), "Owner Id");
        assertNull(s.getPassword(), "Password should be cleared to null");
    }

    @Test
    public void testRegisterSystemWithNoName() {
        IllegalArgumentException e = assertThrows(IllegalArgumentException.class,
                                                  () -> service.registerSystem(new HydroSystem("", "fakePassword")));

        verify(hydroSystemDAO, never()).registerSystem(any());
        assertEquals("Name is required for registering a system.", e.getMessage(), "Exception Message");
    }

    @Test
    public void testRegisterSystemWithNoPassword() {
        IllegalArgumentException e = assertThrows(IllegalArgumentException.class,
                                                  () -> service.registerSystem(new HydroSystem("Temp", "")));

        verify(hydroSystemDAO, never()).registerSystem(any());
        assertEquals("Password is required for registering a system.", e.getMessage(), "Exception Message");
    }

    @Test
    public void testUnregisterSystemRoleAdmin() {
        when(hydroSystemDAO.getSystems(any(HydroSystemGetRequest.class))).thenReturn(Arrays.asList(hydroSystem()));
        when(jwtHolder.getUserId()).thenReturn(12);
        when(jwtHolder.getWebRole()).thenReturn(WebRole.ADMIN);

        service.unregisterSystem(8);
        verify(hydroSystemDAO).unregisterSystem(8);
    }

    @Test
    public void testUnregisterSystemUserWhoCreatedSystem() {
        when(hydroSystemDAO.getSystems(any(HydroSystemGetRequest.class))).thenReturn(Arrays.asList(hydroSystem()));
        when(jwtHolder.getUserId()).thenReturn(2);

        service.unregisterSystem(8);
        verify(hydroSystemDAO).unregisterSystem(8);
    }

    @Test
    public void testUnregisterSystemInsufficentPermissions() {
        when(hydroSystemDAO.getSystems(any(HydroSystemGetRequest.class))).thenReturn(Arrays.asList(hydroSystem()));
        when(jwtHolder.getUserId()).thenReturn(12);
        when(jwtHolder.getWebRole()).thenReturn(WebRole.USER);

        InsufficientPermissionsException e = assertThrows(InsufficientPermissionsException.class,
                                                          () -> service.unregisterSystem(8));

        verify(hydroSystemDAO, never()).unregisterSystem(anyInt());
        assertEquals("Insufficient permissions! You can not unregister this system.", e.getMessage(),
                     "Exception Message");
    }

}
