/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *     IbatisInstrumentDAO class.
 *     
 */
package ca.sciencestudio.model.facility.dao.ibatis;

import java.util.List;

import org.springframework.orm.ibatis.support.SqlMapClientDaoSupport;

import ca.sciencestudio.model.facility.Instrument;
import ca.sciencestudio.model.facility.dao.InstrumentDAO;
import ca.sciencestudio.model.facility.ibatis.IbatisInstrument;
import ca.sciencestudio.util.sql.SqlMapParameters;

/**
 * @author chabotd
 */
public class IbatisInstrumentDAO extends SqlMapClientDaoSupport implements InstrumentDAO {

	@Override
	public Instrument createInstrument() {
		return new IbatisInstrument();
	}

	public int addInstrument(Instrument instrument) {
		int key = (Integer) getSqlMapClientTemplate().insert("addInstrument", instrument);
		logger.info("Addded new Instrument with key = " + key);
		return key;
	}

	public void editInstrument(Instrument instrument) {
		int count = getSqlMapClientTemplate().update("editInstrument", instrument);
		logger.info("Edit Instrument name="+instrument.getName()+", rows affected="+count);
	}

	public Instrument getInstrumentById(int id) {
		return (Instrument) getSqlMapClientTemplate().queryForObject("getInstrumentById", id);
	}
	
	public Instrument getInstrumentByNameAndLaboratoryId(String instrumentName, int laboratoryId) {
		List<Instrument> instruments = getInstrumentListByNameAndLaboratoryId(instrumentName, laboratoryId);
		if((instruments != null) && (instruments.size() > 0)) {
			return instruments.get(0);
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	public List<Instrument> getInstrumentListByLaboratoryId(int laboratoryId) {
		return getSqlMapClientTemplate().queryForList("getInstrumentListByLaboratoryId", laboratoryId);
	}

	@SuppressWarnings("unchecked")
	public List<Instrument> getInstrumentListByName(String name) {
		return getSqlMapClientTemplate().queryForList("getInstrumentListByName", name);
	}
	
	@SuppressWarnings("unchecked")
	public List<Instrument> getInstrumentListByNameAndLaboratoryId(String instrumentName, int laboratoryId) {
		SqlMapParameters params = new SqlMapParameters(instrumentName, laboratoryId);
		return getSqlMapClientTemplate().queryForList("getInstrumentListByNameAndLaboratoryId", params);
	}
	
	public void removeInstrument(int instrumentId) {
		int count = getSqlMapClientTemplate().delete("removeInstrument", instrumentId);
		logger.info("Remove instrument id="+instrumentId+", rows affected="+count);
	}

}
