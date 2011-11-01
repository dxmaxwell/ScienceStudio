/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *     IbatisInstrumentBasicDAO class.
 *     
 */
package ca.sciencestudio.model.facility.dao.ibatis;

import ca.sciencestudio.model.facility.Laboratory;
import ca.sciencestudio.model.facility.Instrument;
import ca.sciencestudio.model.facility.dao.InstrumentBasicDAO;
import ca.sciencestudio.model.facility.dao.ibatis.support.IbatisInstrument;
import ca.sciencestudio.model.dao.ibatis.AbstractIbatisModelBasicDAO;
import ca.sciencestudio.model.utilities.GID;

/**
 * @author maxweld
 */
public class IbatisInstrumentBasicDAO extends AbstractIbatisModelBasicDAO<Instrument, IbatisInstrument> implements InstrumentBasicDAO {

	@Override
	public String getGidType() {
		return Instrument.GID_TYPE;
	}

//	@SuppressWarnings("unchecked")
//	public List<Instrument> getAllByLaboratoryId(int laboratoryId) {
//		List<Instrument> instruments;
//		try {
//			instruments = toModelList(getSqlMapClientTemplate().queryForList(getStatementName("get", "ListByLaboratoryId"), laboratoryId));
//		}
//		catch(DataAccessException e) {
//			logger.warn("Data Access exception while getting Instrument list: " + e.getMessage());
//			throw new ModelAccessException(e);
//		}
//		
//		if(logger.isDebugEnabled()) {
//			logger.debug("Get all Instruments by laboratoryId: " + laboratoryId + ", size: " + instruments.size());
//		}
//		return Collections.unmodifiableList(instruments);
//	}

//	@SuppressWarnings("unchecked")
//	public List<Instrument> getAllByName(String name) {
//		List<Instrument> instruments;
//		try {
//			instruments = toModelList(getSqlMapClientTemplate().queryForList(getStatementName("get", "ListByName"), name));
//		}
//		catch(DataAccessException e) {
//			logger.warn("Data Access exception while getting Instrument list: " + e.getMessage());
//			throw new ModelAccessException(e);
//		}
//		
//		if(logger.isDebugEnabled()) {
//			logger.debug("Get all Instruments by name: " + name + ", size: " + instruments.size());
//		}
//		return Collections.unmodifiableList(instruments);
//	}

//	@SuppressWarnings("unchecked")
//	public List<Instrument> getAllByNameAndLaboratoryId(String name, int laboratoryId) {
//		SqlMapParameters params = new SqlMapParameters(name, laboratoryId);
//		
//		List<Instrument> instruments;
//		try {
//			instruments = toModelList(getSqlMapClientTemplate().queryForList(getStatementName("get", "ListByNameAndLaboratoryId"), params));
//		}
//		catch(DataAccessException e) {
//			logger.warn("Data Access exception while getting Instrument list: " + e.getMessage());
//			throw new ModelAccessException(e);
//		}
//		
//		if(logger.isDebugEnabled()) {
//			logger.debug("Get all Instruments by name: " + name + ", and laboratoryId: " + laboratoryId + ", size: " + instruments.size());
//		}
//		return Collections.unmodifiableList(instruments);
//	}
	
	@Override
	protected IbatisInstrument toIbatisModel(Instrument instrument) {
		if(instrument == null) {
			return null;
		}
		IbatisInstrument ibatisInstrument = new IbatisInstrument();
		GID gid = GID.parse(instrument.getGid());
		if((gid != null) && gid.isFacilityAndType(getGidFacility(), getGidType(), true, true)) {
			ibatisInstrument.setId(gid.getId());
		}
		GID laboratoryGid = GID.parse(instrument.getLaboratoryGid());
		if((laboratoryGid != null) && laboratoryGid.isFacilityAndType(getGidFacility(), Laboratory.GID_TYPE, true, true)) {
			ibatisInstrument.setLaboratoryId(laboratoryGid.getId());
		}
		ibatisInstrument.setName(instrument.getName());
		ibatisInstrument.setLongName(instrument.getLongName());
		ibatisInstrument.setDescription(instrument.getDescription());
		return ibatisInstrument;
	}

	@Override
	protected Instrument toModel(IbatisInstrument ibatisInstrument) {
		if(ibatisInstrument == null) {
			return null;
		}
		Instrument instrument = new Instrument();
		instrument.setGid(GID.format(getGidFacility(), ibatisInstrument.getId(), getGidType()));
		instrument.setLaboratoryGid(GID.format(getGidFacility(), ibatisInstrument.getLaboratoryId(), Laboratory.GID_TYPE));
		instrument.setName(ibatisInstrument.getName());
		instrument.setLongName(ibatisInstrument.getLongName());
		instrument.setDescription(ibatisInstrument.getDescription());
		return instrument;
	}

	@Override
	protected String getStatementName(String prefix, String suffix) {
		return prefix + "Instrument" + suffix;
	}
}
