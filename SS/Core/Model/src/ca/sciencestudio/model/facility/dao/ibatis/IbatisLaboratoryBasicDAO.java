/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *     IbatisLaboratoryBasicDAO Class.
 *     
 */
package ca.sciencestudio.model.facility.dao.ibatis;

import ca.sciencestudio.model.facility.Facility;
import ca.sciencestudio.model.facility.Laboratory;
import ca.sciencestudio.model.facility.dao.LaboratoryBasicDAO;
import ca.sciencestudio.model.facility.dao.ibatis.support.IbatisLaboratory;
import ca.sciencestudio.model.dao.ibatis.AbstractIbatisModelBasicDAO;
import ca.sciencestudio.model.utilities.GID;


/**
 * @author medrand
 *
 */
public class IbatisLaboratoryBasicDAO extends AbstractIbatisModelBasicDAO<Laboratory, IbatisLaboratory> implements LaboratoryBasicDAO {

	@Override
	public String getGidType() {
		return Laboratory.GID_TYPE;
	}
	
//	public Laboratory getLaboratoryByNameAndFacilityId(String laboratoryName, int facilityId) {	
//		List<Laboratory> list = getLaboratoryListByNameAndFacilityId(laboratoryName, facilityId);
//		if((list != null) && (list.size() > 0)) {
//			return list.get(0);
//		}
//		return null;
//	}

	
//	@SuppressWarnings("unchecked")
//	public List<Laboratory> getLaboratoryListByFacilityId(int facilityId) {
//		List<Laboratory> list = getSqlMapClientTemplate().queryForList("getLaboratoryListByFacilityId", facilityId);
//		return list;
//	}

//	@SuppressWarnings("unchecked")
//	public List<Laboratory> getLaboratoryListByName(String laboratoryName) {
//		List<Laboratory> list = getSqlMapClientTemplate().queryForList("getLaboratoryListByName", laboratoryName);
//		return list;
//	}
	
//	@SuppressWarnings("unchecked")
//	public List<Laboratory> getLaboratoryListByNameAndFacilityId(String laboratoryName, int facilityId) {	
//		SqlMapParameters params = new SqlMapParameters(laboratoryName, facilityId);
//		List<Laboratory> list = getSqlMapClientTemplate().queryForList("getLaboratoryListByNameAndFacilityId", params);
//		return list;
//	}
	
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
	protected Laboratory toModel(IbatisLaboratory ibatisLaboratory) {
		if(ibatisLaboratory == null) {
			return null;
		}
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
