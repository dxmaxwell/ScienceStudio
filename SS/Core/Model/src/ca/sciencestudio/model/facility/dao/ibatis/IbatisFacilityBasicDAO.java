/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *     IbatisFacilityBasicDAO class.
 *     
 */
package ca.sciencestudio.model.facility.dao.ibatis;

import org.springframework.dao.DataAccessException;

import ca.sciencestudio.model.facility.Facility;
import ca.sciencestudio.model.facility.dao.FacilityBasicDAO;
import ca.sciencestudio.model.facility.dao.ibatis.support.IbatisFacility;
import ca.sciencestudio.model.dao.ibatis.AbstractIbatisModelBasicDAO;
import ca.sciencestudio.model.utilities.GID;
import ca.sciencestudio.util.exceptions.ModelAccessException;

/**
 * @author maxweld
 * 
 */
public class IbatisFacilityBasicDAO extends AbstractIbatisModelBasicDAO<Facility> implements FacilityBasicDAO {

	@Override
	public String getGidType() {
		return Facility.GID_TYPE;
	}
	
	@Override
	public Facility getByName(String name) {
		Facility facility;
		try {
			facility = toModel((IbatisFacility)getSqlMapClientTemplate().queryForObject(getStatementName("get", "ByName"), name));
		}
		catch(DataAccessException e) {
			logger.warn("Data Access exception while getting Facility: " + e.getMessage());
			throw new ModelAccessException(e);
		}
		
		if(logger.isDebugEnabled()) {
			logger.debug("Get Facility with name: " + name);
		}
		return facility;
	}
	
	@Override
	protected IbatisFacility toIbatisModel(Facility facility) {
		if(facility == null) {
			return null;
		}
		IbatisFacility ibatisFacility = new IbatisFacility();
		GID gid = GID.parse(facility.getGid());
		if((gid != null) && gid.isFacilityAndType(getGidFacility(), getGidType(), true, true)) {
			ibatisFacility.setId(gid.getId());
		}
		ibatisFacility.setName(facility.getName());
		ibatisFacility.setLongName(facility.getLongName());
		ibatisFacility.setDescription(facility.getDescription());
		ibatisFacility.setPhoneNumber(facility.getPhoneNumber());
		ibatisFacility.setEmailAddress(facility.getEmailAddress());
		ibatisFacility.setLocation(facility.getLocation());
		ibatisFacility.setAuthcUrl(facility.getAuthcUrl());
		ibatisFacility.setHomeUrl(facility.getHomeUrl());
		return ibatisFacility;
	}
	
	@Override
	protected Facility toModel(Object obj) {
		if(!(obj instanceof IbatisFacility)) {
			return null;
		}
		IbatisFacility ibatisFacility = (IbatisFacility)obj;
		Facility facility = new Facility();
		facility.setGid(GID.format(getGidFacility(), ibatisFacility.getId(), getGidType()));
		facility.setName(ibatisFacility.getName());
		facility.setLongName(ibatisFacility.getLongName());
		facility.setDescription(ibatisFacility.getDescription());
		facility.setPhoneNumber(ibatisFacility.getPhoneNumber());
		facility.setEmailAddress(ibatisFacility.getEmailAddress());
		facility.setLocation(ibatisFacility.getLocation());
		facility.setAuthcUrl(ibatisFacility.getAuthcUrl());
		facility.setHomeUrl(ibatisFacility.getHomeUrl());
		return facility;
	}
	
	@Override
	protected String getStatementName(String prefix, String suffix) {
		return prefix + "Facility" + suffix;
	}
}
