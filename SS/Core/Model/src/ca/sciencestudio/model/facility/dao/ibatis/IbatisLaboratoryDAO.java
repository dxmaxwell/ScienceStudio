/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *     IbatisLaboratoryDAO Class.
 *     
 */
package ca.sciencestudio.model.facility.dao.ibatis;

import java.util.List;

import org.springframework.orm.ibatis.support.SqlMapClientDaoSupport;

import ca.sciencestudio.model.facility.Laboratory;
import ca.sciencestudio.model.facility.dao.LaboratoryDAO;
import ca.sciencestudio.model.facility.ibatis.IbatisLaboratory;
import ca.sciencestudio.util.sql.SqlMapParameters;


/**
 * @author medrand
 *
 */
public class IbatisLaboratoryDAO extends SqlMapClientDaoSupport implements LaboratoryDAO {

	public Laboratory createLaboratory() {
		return new IbatisLaboratory();
	}
	
	public int addLaboratory(Laboratory laboratory) {
		int key = (Integer) getSqlMapClientTemplate().insert("addLaboratory", laboratory);
		logger.info("Added new laboratory with key: " + key);
		return key;
	}

	public void editLaboratory(Laboratory laboratory) {
		int count = getSqlMapClientTemplate().update("editLaboratory", laboratory);
		logger.info("Rows affected: " + count);
	}

	@SuppressWarnings("unchecked")
	public List<Laboratory> getLaboratoryListByFacilityId(int facilityId) {
		List<Laboratory> list = getSqlMapClientTemplate().queryForList("getLaboratoryListByFacilityId", facilityId);
		return list;
	}

	@SuppressWarnings("unchecked")
	public List<Laboratory> getLaboratoryListByName(String laboratoryName) {
		List<Laboratory> list = getSqlMapClientTemplate().queryForList("getLaboratoryListByName", laboratoryName);
		return list;
	}
	
	@SuppressWarnings("unchecked")
	public List<Laboratory> getLaboratoryListByNameAndFacilityId(String laboratoryName, int facilityId) {	
		SqlMapParameters params = new SqlMapParameters(laboratoryName, facilityId);
		List<Laboratory> list = getSqlMapClientTemplate().queryForList("getLaboratoryListByNameAndFacilityId", params);
		return list;
	}
	
	public Laboratory getLaboratoryById(int laboratoryId) {
		Laboratory lab = (Laboratory) getSqlMapClientTemplate().queryForObject("getLaboratoryById", laboratoryId);
		return lab;
	}

	@SuppressWarnings("unchecked")
	public List<Laboratory> getLaboratoryList() {
		List<Laboratory> list = getSqlMapClientTemplate().queryForList("getLaboratoryList");
		return list;
	}

	public Laboratory getLaboratoryByNameAndFacilityId(String laboratoryName, int facilityId) {	
		List<Laboratory> list = getLaboratoryListByNameAndFacilityId(laboratoryName, facilityId);
		if((list != null) && (list.size() > 0)) {
			return list.get(0);
		}
		return null;
	}
	
	public void removeLaboratory(int laboratoryId) {
		int count = getSqlMapClientTemplate().update("removeLaboratory", laboratoryId);
		logger.info("Rows affected: " + count);
	}

}
