/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *     IbatisLaboratoryBasicDAO Class.
 *     
 */
package ca.sciencestudio.model.facility.dao.ibatis;

import java.util.Collections;
import java.util.List;

import org.springframework.dao.DataAccessException;

import ca.sciencestudio.model.facility.Facility;
import ca.sciencestudio.model.facility.Laboratory;
import ca.sciencestudio.model.facility.dao.LaboratoryBasicDAO;
import ca.sciencestudio.model.facility.dao.ibatis.support.IbatisLaboratory;
import ca.sciencestudio.model.dao.ibatis.AbstractIbatisModelBasicDAO;
import ca.sciencestudio.model.utilities.GID;
import ca.sciencestudio.util.exceptions.ModelAccessException;


/**
 * @author maxweld
 *
 */
public class IbatisLaboratoryBasicDAO extends AbstractIbatisModelBasicDAO<Laboratory> implements LaboratoryBasicDAO {

	@Override
	public String getGidType() {
		return Laboratory.GID_TYPE;
	}
	
	@Override
	public List<Laboratory> getAllByFacilityGid(String facilityGid) {
		GID gid = parseAndCheckGid(facilityGid, getGidFacility(), Facility.GID_TYPE);
		if(gid == null) {
			return Collections.emptyList();
		}
		
		List<Laboratory> techniques;
		try {
			techniques = toModelList(getSqlMapClientTemplate().queryForList(getStatementName("get", "ListByFacilityId"), gid.getId()));
		}
		catch(DataAccessException e) {
			logger.warn("Data Access exception while getting Laboratory list: " + e.getMessage());
			throw new ModelAccessException(e);
		}
		
		if(logger.isDebugEnabled()) {
			logger.debug("Get all Laboratories by Facility GID: " + facilityGid + ", size: " + techniques.size());
		}
		return Collections.unmodifiableList(techniques);
	}
	
	@Override
	protected IbatisLaboratory toIbatisModel(Laboratory laboratory) {
		if(laboratory == null) {
			return null;
		}
		IbatisLaboratory ibatisLaboratory = new IbatisLaboratory();
		GID gid = GID.parse(laboratory.getGid());
		if((gid != null) && gid.isFacilityAndType(getGidFacility(), getGidType(), true, true)) {
			ibatisLaboratory.setId(gid.getId());
		}
		GID facilityGid = GID.parse(laboratory.getFacilityGid());
		if((facilityGid != null) && facilityGid.isFacilityAndType(getGidFacility(), Facility.GID_TYPE, true, true)) {
			ibatisLaboratory.setFacilityId(facilityGid.getId());
		}
		ibatisLaboratory.setName(laboratory.getName());
		ibatisLaboratory.setLongName(laboratory.getLongName());
		ibatisLaboratory.setDescription(laboratory.getDescription());
		ibatisLaboratory.setPhoneNumber(laboratory.getPhoneNumber());
		ibatisLaboratory.setEmailAddress(laboratory.getEmailAddress());
		ibatisLaboratory.setLocation(laboratory.getLocation());
		ibatisLaboratory.setViewUrl(laboratory.getViewUrl());
		return ibatisLaboratory;
	}
	
	@Override
	protected Laboratory toModel(Object obj) {
		if(!(obj instanceof IbatisLaboratory)) {
			return null;
		}
		IbatisLaboratory ibatisLaboratory = (IbatisLaboratory)obj;
		Laboratory laboratory = new Laboratory();
		laboratory.setGid(GID.format(getGidFacility(), ibatisLaboratory.getId(), getGidType()));
		laboratory.setFacilityGid(GID.format(getGidFacility(), ibatisLaboratory.getFacilityId(), Facility.GID_TYPE));
		laboratory.setName(ibatisLaboratory.getName());
		laboratory.setLongName(ibatisLaboratory.getLongName());
		laboratory.setDescription(ibatisLaboratory.getDescription());
		laboratory.setPhoneNumber(ibatisLaboratory.getPhoneNumber());
		laboratory.setEmailAddress(ibatisLaboratory.getEmailAddress());
		laboratory.setLocation(ibatisLaboratory.getLocation());
		laboratory.setViewUrl(ibatisLaboratory.getViewUrl());
		return laboratory;
	}

	@Override
	protected String getStatementName(String prefix, String suffix) {
		return prefix + "Laboratory" + suffix;
	}	
}
