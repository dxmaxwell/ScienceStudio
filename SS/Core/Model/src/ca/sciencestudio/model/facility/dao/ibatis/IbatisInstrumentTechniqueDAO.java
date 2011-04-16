/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *     IbatisInstrumentTechniqueDAO class.
 *     
 */
package ca.sciencestudio.model.facility.dao.ibatis;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.orm.ibatis.support.SqlMapClientDaoSupport;

import ca.sciencestudio.model.facility.InstrumentTechnique;
import ca.sciencestudio.model.facility.dao.InstrumentTechniqueDAO;
import ca.sciencestudio.model.facility.ibatis.IbatisInstrumentTechnique;
import ca.sciencestudio.util.sql.SqlMapParameters;

/**
 * @author chabotd
 */
public class IbatisInstrumentTechniqueDAO extends SqlMapClientDaoSupport implements InstrumentTechniqueDAO {

	protected final Log logger = LogFactory.getLog(getClass());

	public InstrumentTechnique createInstrumentTechnique() {
		return new IbatisInstrumentTechnique();
	}
	
	public int addInstrumentTechnique(InstrumentTechnique instrumentTechnique) {
		Integer key = (Integer) getSqlMapClientTemplate().insert("addInstrumentTechnique", instrumentTechnique);
		logger.info("Added new InstrumentTechnique with key="+key.toString());
		return key.intValue();
	}

	public void editInstrumentTechnique(InstrumentTechnique instrumentTechnique) {
		int count = getSqlMapClientTemplate().update("editInstrumentTechnique", instrumentTechnique);
		logger.info("Edit InstrumentTechnique id="+instrumentTechnique.getId()+", rows affected="+count);
	}

	public InstrumentTechnique getInstrumentTechniqueById(int id) {
		return (InstrumentTechnique) getSqlMapClientTemplate().queryForObject("getInstrumentTechniqueById", id);
	}

	@SuppressWarnings("unchecked")
	public List<InstrumentTechnique> getInstrumentTechniqueListByInstrumentId(int id) {
		return getSqlMapClientTemplate().queryForList("getInstrumentTechniqueListByInstrumentId", id);
	}

	@SuppressWarnings("unchecked")
	public List<InstrumentTechnique> getInstrumentTechniqueListByTechniqueId(int id) {
		return getSqlMapClientTemplate().queryForList("getInstrumentTechniqueListByTechniqueId", id);
	}
	
	@SuppressWarnings("unchecked")
	public List<InstrumentTechnique> getInstrumentTechniqueListByLaboratoryId(int id) {
		List<InstrumentTechnique> list = getSqlMapClientTemplate().queryForList("getInstrumentTechniqueListByLaboratoryId", id);
		logger.debug("Get instrument technique list with laboratory id: " + id + ", size: " + list.size());
		return list;
	}

	public void removeInstrumentTechnique(int id) {
		int count = getSqlMapClientTemplate().delete("removeInstrumentTechnique", id);
		logger.info("Removed InstrumentTechnique id="+id+", rows affected="+count);
	}

	public InstrumentTechnique getInstrumentTechniqueByInstrumentIdAndTechniqueId(
			int instrumentId, int techniqueId) {
		SqlMapParameters params = new SqlMapParameters();
		params.setParam1(instrumentId);
		params.setParam2(techniqueId);
		return (InstrumentTechnique) getSqlMapClientTemplate().queryForObject(
				"getInstrumentTechniqueByInstrumentIdAndTechniqueId", params);
	}

}
