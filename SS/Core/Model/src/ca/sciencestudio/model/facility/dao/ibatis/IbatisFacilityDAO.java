/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *     IbatisFacilityDAO class.
 *     
 */
package ca.sciencestudio.model.facility.dao.ibatis;

import java.util.List;

import org.springframework.orm.ibatis.support.SqlMapClientDaoSupport;

import ca.sciencestudio.model.facility.Facility;
import ca.sciencestudio.model.facility.dao.FacilityDAO;
import ca.sciencestudio.model.facility.ibatis.IbatisFacility;

/**
 * @author maxweld
 * 
 */
public class IbatisFacilityDAO extends SqlMapClientDaoSupport implements FacilityDAO {

	public Facility createFacility() {
		return new IbatisFacility();
	}
	
	public int addFacility(Facility facility) {
		int key = (Integer)getSqlMapClientTemplate().insert("addFacility", facility);
		logger.info("Added new facility with key: " + key);
		return key;
	}

	public void editFacility(Facility facility) {
		int count = getSqlMapClientTemplate().update("editFacility", facility);
		logger.info("Rows affected: "+count);
	}

	public void removeFacility(int facilityId) {
		int count = getSqlMapClientTemplate().delete("removeFacility", new Integer(facilityId));
		logger.info("Rows affected: "+count);
	}

	public Facility getFacilityById(int facilityId) {
		Facility facility = (Facility)getSqlMapClientTemplate().queryForObject("getFacilityById", new Integer(facilityId));
		return facility;
	}

	public Facility getFacilityByName(String name) {
		List<Facility> facilities = getFacilityListByName(name);
		if((facilities != null) && (facilities.size() > 0)) {
			return facilities.get(0);
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	public List<Facility> getFacilityList() {
		List<Facility> facilities = getSqlMapClientTemplate().queryForList("getFacilityList");
		return facilities;
	}

	@SuppressWarnings("unchecked")
	public List<Facility> getFacilityListByName(String name) {
		List<Facility> facilities = getSqlMapClientTemplate().queryForList("getFacilityListByName", name);
		return facilities;
	}
}
