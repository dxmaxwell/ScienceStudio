/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *     IbatisExperimentBasicDAO class.
 *     
 */
package ca.sciencestudio.model.session.dao.ibatis;

import java.util.Collections;
import java.util.List;

import org.springframework.dao.DataAccessException;

import ca.sciencestudio.model.dao.ibatis.AbstractIbatisModelBasicDAO;
import ca.sciencestudio.model.facility.InstrumentTechnique;
import ca.sciencestudio.model.session.Experiment;
import ca.sciencestudio.model.session.Session;
import ca.sciencestudio.model.session.dao.ExperimentBasicDAO;
import ca.sciencestudio.model.session.dao.ibatis.support.IbatisExperiment;
import ca.sciencestudio.model.utilities.GID;
import ca.sciencestudio.util.exceptions.ModelAccessException;

/**
 * @author maxweld
 *
 */
public class IbatisExperimentBasicDAO extends AbstractIbatisModelBasicDAO<Experiment> implements ExperimentBasicDAO {

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
	
	@Override
	public List<Experiment> getAllBySessionGid(String sessionGid) {
		GID gid = parseAndCheckGid(sessionGid, getGidFacility(), Session.GID_TYPE);
		if(gid == null) {
			return Collections.emptyList();
		}
		
		List<Experiment> experiments;
		try {
			experiments = toModelList(getSqlMapClientTemplate().queryForList(getStatementName("get", "ListBySessionId"), gid.getId()));
		}
		catch(DataAccessException e) {
			logger.warn("Data Access exception while getting Model list: " + e.getMessage());
			throw new ModelAccessException(e);
		}
		
		if(logger.isDebugEnabled()) {
			logger.debug("Get all Experiments by Session GID: " + sessionGid + ", size: " + experiments.size());
		}
		return Collections.unmodifiableList(experiments);
	}
	
	@Override
	public List<Experiment> getAllBySourceGid(String sourceGid) {
		List<Experiment> experiments;
		try {
			experiments = toModelList(getSqlMapClientTemplate().queryForList(getStatementName("get", "ListBySourceGid"), sourceGid));
		}
		catch(DataAccessException e) {
			logger.warn("Data Access exception while getting Model list: " + e.getMessage());
			throw new ModelAccessException(e);
		}
		
		if(logger.isDebugEnabled()) {
			logger.debug("Get all Experiments by source GID: " + sourceGid + ", size: " + experiments.size());
		}
		return Collections.unmodifiableList(experiments);
	}
	
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
	protected Experiment toModel(Object obj) {
		if(!(obj instanceof IbatisExperiment)) {
			return null;
		}
		IbatisExperiment ibatisExperiment = (IbatisExperiment)obj;
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
