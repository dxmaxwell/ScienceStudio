/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *     IbatisExperimentBasicDAO class.
 *     
 */
package ca.sciencestudio.model.session.dao.ibatis;

import ca.sciencestudio.model.dao.ibatis.AbstractIbatisModelBasicDAO;
import ca.sciencestudio.model.facility.InstrumentTechnique;
import ca.sciencestudio.model.session.Experiment;
import ca.sciencestudio.model.session.Session;
import ca.sciencestudio.model.session.dao.ExperimentBasicDAO;
import ca.sciencestudio.model.session.dao.ibatis.support.IbatisExperiment;
import ca.sciencestudio.model.utilities.GID;

/**
 * @author maxweld
 *
 */
public class IbatisExperimentBasicDAO extends AbstractIbatisModelBasicDAO<Experiment, IbatisExperiment> implements ExperimentBasicDAO {

	@Override
	public String getGidType() {
		return Experiment.GID_TYPE;
	}

//	@SuppressWarnings("unchecked")
//	public List<Experiment> getExperimentListBySampleId(int sampleId) {
//		List<Experiment> list = getSqlMapClientTemplate().queryForList("getExperimentListBySampleId", sampleId);
//		logger.debug("Get Experiment list by sampleId: " + sampleId + ", size: " + list.size());
//		return list;
//	}
	
//	@SuppressWarnings("unchecked")
//	public List<Experiment> getExperimentListBySessionId(int sessionId) {
//		List<Experiment> list = getSqlMapClientTemplate().queryForList("getExperimentListBySessionId", sessionId);
//		logger.debug("Get Experiment list by sessionId: " + sessionId + ", size: " + list.size());
//		return list;
//	}
	
	@Override
	protected IbatisExperiment toIbatisModel(Experiment experiment) {
		if(experiment == null) {
			return null;
		}
		IbatisExperiment ibatisExperiment = new IbatisExperiment();
		GID gid = GID.parse(experiment.getGid());
		if((gid != null) && gid.isFacilityAndType(getGidFacility(), getGidType(), true, true)) {
			ibatisExperiment.setId(gid.getId());
		}
		GID sessionGid = GID.parse(experiment.getSessionGid());
		if((sessionGid != null) && sessionGid.isFacilityAndType(getGidFacility(), Session.GID_TYPE, true, true)) {
			ibatisExperiment.setSessionId(sessionGid.getId());
		}
		GID instrumentTechniqueGid = GID.parse(experiment.getInstrumentTechniqueGid());
		if((instrumentTechniqueGid != null) && instrumentTechniqueGid.isFacilityAndType(getGidFacility(), InstrumentTechnique.GID_TYPE, true, true)) {
			ibatisExperiment.setInstrumentTechniqueId(instrumentTechniqueGid.getId());
		}
		ibatisExperiment.setName(experiment.getName());
		ibatisExperiment.setDescription(experiment.getDescription());
		ibatisExperiment.setSourceGid(experiment.getSourceGid());
		return ibatisExperiment;
	}

	@Override
	protected Experiment toModel(IbatisExperiment ibatisExperiment) {
		if(ibatisExperiment == null) {
			return null;
		}
		Experiment experiment = new Experiment();
		experiment.setGid(GID.format(getGidFacility(), ibatisExperiment.getId(), getGidType()));
		experiment.setSessionGid(GID.format(getGidFacility(), ibatisExperiment.getSessionId(), Session.GID_TYPE));
		experiment.setInstrumentTechniqueGid(GID.format(getGidFacility(), ibatisExperiment.getInstrumentTechniqueId(), InstrumentTechnique.GID_TYPE));
		experiment.setName(ibatisExperiment.getName());
		experiment.setDescription(ibatisExperiment.getDescription());
		experiment.setSourceGid(ibatisExperiment.getSourceGid());
		return experiment;
	}

	@Override
	protected String getStatementName(String prefix, String suffix) {
		return prefix + "Experiment" + suffix;
	}
}
