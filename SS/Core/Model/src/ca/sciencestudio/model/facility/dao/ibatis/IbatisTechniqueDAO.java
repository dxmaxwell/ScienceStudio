/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *     IbatisTechniqueDAO class.
 *     
 */
package ca.sciencestudio.model.facility.dao.ibatis;

import java.util.List;

import org.springframework.orm.ibatis.support.SqlMapClientDaoSupport;

import ca.sciencestudio.model.facility.Technique;
import ca.sciencestudio.model.facility.dao.TechniqueDAO;
import ca.sciencestudio.model.facility.ibatis.IbatisTechnique;
import ca.sciencestudio.util.sql.SqlMapParameters;


/**
 * @author medrand
 *
 */
public class IbatisTechniqueDAO extends SqlMapClientDaoSupport implements TechniqueDAO {

	@Override
	public Technique createTechnique() {
		return new IbatisTechnique();
	}

	public int addTechnique(Technique technique) {
		int key = (Integer) getSqlMapClientTemplate().insert("addTechnique", technique);
		logger.info("Added new technique with key: " + key);
		return key;
	}

	public void editTechnique(Technique technique) {
		int count = getSqlMapClientTemplate().update("editTechnique", technique);
		logger.info("Rows affected: " + count);
	}

	public Technique getTechniqueById(int techniqueId) {
		Technique technique = (Technique) getSqlMapClientTemplate().queryForObject("getTechniqueById", techniqueId);
		return technique;
	}

	public Technique getTechniqueByNameAndInstrumentId(String techniqueName, int instrumentId) {
		List<Technique> list = getTechniqueListByNameAndInstrumentId(techniqueName, instrumentId);
		if((list != null) && (list.size() > 0)) {
			return list.get(0);
		}
		return null;
	}
	
	@SuppressWarnings("unchecked")
	public List<Technique> getTechniqueList() {
		List<Technique> list = getSqlMapClientTemplate().queryForList("getTechniqueList");
		return list;
	}

	@SuppressWarnings("unchecked")
	public List<Technique> getTechniqueListByName(String techniqueName) {
		List<Technique> list = getSqlMapClientTemplate().queryForList("getTechniqueListByName", techniqueName);
		return list;
	}
	
	@SuppressWarnings("unchecked")
	public List<Technique> getTechniqueListByNameAndInstrumentId(String techniqueName, int instrumentId) {
		SqlMapParameters params = new SqlMapParameters(techniqueName, instrumentId);
		List<Technique> list = getSqlMapClientTemplate().queryForList("getTechniqueListByNameAndInstrumentId", params);
		return list;
	}
	
	public void removeTechnique(int techniqueId) {
		int count = getSqlMapClientTemplate().update("removeTechnique", techniqueId);
		logger.info("Rows affected: " + count);
	}

	@SuppressWarnings("unchecked")
	public List<Technique> getTechniqueListByInstrumentId(int instrumentId) {
		List<Technique> list = getSqlMapClientTemplate().queryForList("getTechniqueListByInstrumentId", instrumentId);
		return list;
	}

	@SuppressWarnings("unchecked")
	public List<Technique> getTechniqueListByLaboratoryId(int laboratoryId) {
		List<Technique> list = getSqlMapClientTemplate().queryForList("getTechniqueListByLaboratoryId", laboratoryId);
		logger.debug("Get technique with laboratory id: " + laboratoryId + ", size: " + laboratoryId);
		return list;
	}
}
