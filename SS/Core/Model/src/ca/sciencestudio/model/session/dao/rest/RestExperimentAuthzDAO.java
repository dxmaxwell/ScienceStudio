/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *     RestExperimentAuthzDAO class.
 *     
 */
package ca.sciencestudio.model.session.dao.rest;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.springframework.web.client.RestClientException;

import ca.sciencestudio.model.session.Experiment;
import ca.sciencestudio.model.session.dao.ExperimentAuthzDAO;
import ca.sciencestudio.model.session.dao.rest.support.RestExperiment;
import ca.sciencestudio.model.dao.Data;
import ca.sciencestudio.model.dao.SimpleData;
import ca.sciencestudio.model.dao.rest.AbstractRestModelAuthzDAO;
import ca.sciencestudio.util.exceptions.ModelAccessException;

/**
 * @author maxweld
 *
 */
public class RestExperimentAuthzDAO extends AbstractRestModelAuthzDAO<Experiment> implements ExperimentAuthzDAO {
	
	public static final String EXPERIMENT_MODEL_PATH = "/experiments";
	
	@Override
	public Data<List<Experiment>> getAllBySessionGid(String user, String sessionGid) {
		List<Experiment> experiments;
		try {
			experiments = Arrays.asList(getRestTemplate().getForObject(getRestUrl("", "user={user}", "session={session}"), getModelArrayClass(), user, sessionGid));
		}
		catch(RestClientException e) {
			logger.warn("Rest Client exception while getting Model list: " + e.getMessage());
			return new SimpleData<List<Experiment>>(new ModelAccessException(e));
		}
		
		if(logger.isDebugEnabled()) {
			logger.debug("Get all Experiments by Session GID: " + sessionGid + ", size: " + experiments.size());
		}
		return new SimpleData<List<Experiment>>(Collections.unmodifiableList(experiments));
	}
	
	@Override
	protected Object toRestModel(Experiment experiment) {
		RestExperiment restExperiment = new RestExperiment();
		restExperiment.setSessionGid(experiment.getSessionGid());
		restExperiment.setName(experiment.getName());
		restExperiment.setDescription(experiment.getDescription());
		restExperiment.setSourceGid(experiment.getSourceGid());
		restExperiment.setInstrumentTechniqueGid(experiment.getInstrumentTechniqueGid());
		return restExperiment;
	}

	@Override
	protected String getModelPath() {
		return EXPERIMENT_MODEL_PATH;
	}

	@Override
	protected Class<Experiment> getModelClass() {
		return Experiment.class;
	}

	@Override
	protected Class<Experiment[]> getModelArrayClass() {
		return Experiment[].class;
	}
}
