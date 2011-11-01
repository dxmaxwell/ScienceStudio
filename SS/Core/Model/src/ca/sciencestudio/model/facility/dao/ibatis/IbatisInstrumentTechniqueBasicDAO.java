/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *     IbatisInstrumentTechniqueDAO class.
 *     
 */
package ca.sciencestudio.model.facility.dao.ibatis;

import ca.sciencestudio.model.dao.ibatis.AbstractIbatisModelBasicDAO;
import ca.sciencestudio.model.facility.Instrument;
import ca.sciencestudio.model.facility.InstrumentTechnique;
import ca.sciencestudio.model.facility.Technique;
import ca.sciencestudio.model.facility.dao.InstrumentTechniqueBasicDAO;
import ca.sciencestudio.model.facility.dao.ibatis.support.IbatisInstrumentTechnique;
import ca.sciencestudio.model.utilities.GID;

/**
 * @author maxweld
 */
public class IbatisInstrumentTechniqueBasicDAO extends AbstractIbatisModelBasicDAO<InstrumentTechnique, IbatisInstrumentTechnique> implements InstrumentTechniqueBasicDAO {

	@Override
	public String getGidType() {
		return InstrumentTechnique.GID_TYPE;
	}	

//	@SuppressWarnings("unchecked")
//	public List<InstrumentTechnique> getInstrumentTechniqueListByInstrumentId(int id) {
//		return getSqlMapClientTemplate().queryForList("getInstrumentTechniqueListByInstrumentId", id);
//	}

//	@SuppressWarnings("unchecked")
//	public List<InstrumentTechnique> getInstrumentTechniqueListByTechniqueId(int id) {
//		return getSqlMapClientTemplate().queryForList("getInstrumentTechniqueListByTechniqueId", id);
//	}
	
//	@SuppressWarnings("unchecked")
//	public List<InstrumentTechnique> getInstrumentTechniqueListByLaboratoryId(int id) {
//		List<InstrumentTechnique> list = getSqlMapClientTemplate().queryForList("getInstrumentTechniqueListByLaboratoryId", id);
//		logger.debug("Get instrument technique list with laboratory id: " + id + ", size: " + list.size());
//		return list;
//	}

	
//	public InstrumentTechnique getInstrumentTechniqueByInstrumentIdAndTechniqueId(
//			int instrumentId, int techniqueId) {
//		SqlMapParameters params = new SqlMapParameters();
//		params.setParam1(instrumentId);
//		params.setParam2(techniqueId);
//		return (InstrumentTechnique) getSqlMapClientTemplate().queryForObject(
//				"getInstrumentTechniqueByInstrumentIdAndTechniqueId", params);
//	}

	@Override
	protected IbatisInstrumentTechnique toIbatisModel(InstrumentTechnique it) {
		if(it == null) {
			return null;
		}
		IbatisInstrumentTechnique iit = new IbatisInstrumentTechnique();
		GID gid = GID.parse(it.getGid());
		if((gid != null) && gid.isFacilityAndType(getGidFacility(), getGidType(), true, true)) {
			iit.setId(gid.getId());
		}
		GID instrumentGid = GID.parse(it.getInstrumentGid());
		if((instrumentGid != null) &&  instrumentGid.isFacilityAndType(getGidFacility(), Instrument.GID_TYPE, true, true)) {
			iit.setInstrumentId(instrumentGid.getId());
		}
		GID techniqueGid = GID.parse(it.getTechniqueGid());
		if((techniqueGid != null) && techniqueGid.isFacilityAndType(getGidFacility(), Technique.GID_TYPE, true, true)) {
			iit.setTechniqueId(techniqueGid.getId());
		}
		return iit;
	}
	
	@Override
	protected InstrumentTechnique toModel(IbatisInstrumentTechnique iit) {
		if(iit == null) {
			return null;
		}
		InstrumentTechnique it = new InstrumentTechnique();
		it.setGid(GID.format(getGidFacility(), iit.getId(), getGidType()));
		it.setInstrumentGid(GID.format(getGidFacility(), iit.getInstrumentId(), Instrument.GID_TYPE));
		it.setTechniqueGid(GID.format(getGidFacility(), iit.getTechniqueId(), Technique.GID_TYPE));
		return it;
	}
	
	@Override
	protected String getStatementName(String prefix, String suffix) {
		return prefix + "InstrumentTechnique" + suffix;
	}
}
