package com.hydro.insite_hydro_system_microservice.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import com.hydro.common.abstracts.AbstractMapper;
import com.hydro.common.dictionary.data.HydroSystem;
import com.hydro.common.dictionary.data.PartNumber;
import com.hydro.insite_grow_chamber_history_microservice.client.domain.GrowChamberLog;

/**
 * Mapper class to map a Grow Chamber Log Object {@link GrowChamberLog}
 * 
 * @author Sam Butler
 * @since June 25, 2020
 */
public class HydroSystemMapper extends AbstractMapper<HydroSystem> {
	public static HydroSystemMapper HYDRO_SYSTEM_MAPPER = new HydroSystemMapper();

	public HydroSystem mapRow(ResultSet rs, int rowNum) throws SQLException {
		HydroSystem sys = new HydroSystem();

		sys.setId(rs.getInt(ID));
		sys.setUuid(rs.getString(UUID));
		sys.setPartNumber(new PartNumber(rs.getString(PART_NUMBER)));
		sys.setName(rs.getString(NAME));
		sys.setInsertDate(rs.getTimestamp(INSERT_DATE).toLocalDateTime());
		sys.setOwnerUserId(rs.getInt(OWNER_USER_ID));

		return sys;
	}
}
