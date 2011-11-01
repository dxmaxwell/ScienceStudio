/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *     IbatisTechniqueBasicDAO class.
 *     
 */
package ca.sciencestudio.model.facility.dao.ibatis;

import ca.sciencestudio.model.facility.Technique;
import ca.sciencestudio.model.facility.dao.TechniqueBasicDAO;
import ca.sciencestudio.model.facility.dao.ibatis.support.IbatisTechnique;
import ca.sciencestudio.model.dao.ibatis.AbstractIbatisModelBasicDAO;
import ca.sciencestudio.model.utilities.GID;

/**
 * @author maxweld
 *
 */
public class IbatisTechniqueBasicDAO extends AbstractIbatisModelBasicDAO<Technique, IbatisTechnique> implements TechniqueBasicDAO {

	@Override
	public String getGidType() {
		return Technique.GID_TYPE;
	}

//	public Technique getTechniqueByNameAndInstrumentId(String techniqueName, int instrumentId) {
//		List<Technique> list = getTechniqueListByNameAndInstrumentId(techniqueName, instrumentId);
//		if((list != null) && (list.size() > 0)) {
//			return list.get(0);
//		}
//		return null;
//	}
	
//	@SuppressWarnings("unchecked")
//	public List<Technique> getTechniqueListByName(String techniqueName) {
//		List<Technique> list = getSqlMapClientTemplate().queryForList("getTechniqueListByName", techniqueName);
//		return list;
//	}
	
//	@SuppressWarnings("unchecked")
//	public List<Technique> getTechniqueListByNameAndInstrumentId(String techniqueName, int instrumentId) {
//		SqlMapParameters params = new SqlMapParameters(techniqueName, instrumentId);
//		List<Technique> list = getSqlMapClientTemplate().queryForList("getTechniqueListByNameAndInstrumentId", params);
//		return list;
//	}
	
//	@SuppressWarnings("unchecked")
//	public List<Technique> getTechniqueListByInstrumentId(int instrumentId) {
//		List<Technique> list = getSqlMapClientTemplate().queryForList("getTechniqueListByInstrumentId", instrumentId);
//		return list;
//	}

//	@SuppressWarnings("unchecked")
//	public List<Technique> getTechniqueListByLaboratoryId(int laboratoryId) {
//		List<Technique> list = getSqlMapClientTemplate().queryForList("getTechniqueListByLaboratoryId", laboratoryId);
//		logger.debug("Get technique with laboratory id: " + laboratoryId + ", size: " + laboratoryId);
//		return list;
//	}
	
	@Override
	protected IbatisTechnique toIbatisModel(Technique technique) {
		if(technique == null) {
			return null;
		}
		IbatisTechnique ibatisTechnique = new IbatisTechnique();
		GID gid = GID.parse(technique.getGid());
		if((gid != null) && gid.isFacilityAndType(getGidFacility(), getGidType(), true, true)) {
			ibatisTechnique.setId(gid.getId());
		}
		ibatisTechnique.setName(technique.getName());
		ibatisTechnique.setLongName(technique.getLongName());
		ibatisTechnique.setDescription(technique.getDescription());
		return ibatisTechnique;
	}
	
	@Override
	protected Technique toModel(IbatisTechnique ibatisTechnique) {
		if(ibatisTechnique == null) {
			return null;
		}
		Technique technique = new Technique();
		technique.setGid(GID.format(getGidFacility(), ibatisTechnique.getId(), getGidType()));
		technique.setName(ibatisTechnique.getName());
		technique.setLongName(ibatisTechnique.getLongName());
		technique.setDescription(ibatisTechnique.getDescription());
		return technique;
	}

	@Override
	protected String getStatementName(String prefix, String suffix) {
		return prefix + "Technique" + suffix;
	}
}
