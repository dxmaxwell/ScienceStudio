/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *     RestSampleAuthzDAO class.
 *     
 */
package ca.sciencestudio.model.sample.dao.rest;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.springframework.web.client.RestClientException;

import ca.sciencestudio.model.sample.Sample;
import ca.sciencestudio.model.sample.dao.SampleAuthzDAO;
import ca.sciencestudio.model.sample.dao.rest.support.RestSample;
import ca.sciencestudio.model.dao.Data;
import ca.sciencestudio.model.dao.SimpleData;
import ca.sciencestudio.model.dao.rest.AbstractRestModelAuthzDAO;
import ca.sciencestudio.util.exceptions.ModelAccessException;

/**
 * @author maxweld
 *
 */
public class RestSampleAuthzDAO extends AbstractRestModelAuthzDAO<Sample> implements SampleAuthzDAO {

	public static final String SAMPLE_MODEL_PATH = "/samples";
	
	@Override
	public Data<List<Sample>> getAllByProjectGid(String user, String projectGid) {
		List<Sample> samples;
		try {
			samples = Arrays.asList(getRestTemplate().getForObject(getRestUrl("", "user={user}", "project={project}"), getModelArrayClass(), user, projectGid));
		}
		catch(RestClientException e) {
			logger.warn("Rest Client exception while getting Model list: " + e.getMessage());
			return new SimpleData<List<Sample>>(new ModelAccessException(e));
		}
		
		if(logger.isDebugEnabled()) {
			logger.debug("Get all Sample with Project GID: " + projectGid  + ", size: " + samples.size());
		}
		
		return new SimpleData<List<Sample>>(Collections.unmodifiableList(samples));
	}
	
	@Override
	protected RestSample toRestModel(Sample sample) {
		RestSample restSample = new RestSample();
		restSample.setProjectGid(sample.getProjectGid());
		restSample.setName(sample.getName());
		restSample.setDescription(sample.getDescription());
		restSample.setCasNumber(sample.getCasNumber());
		restSample.setState(sample.getState());
		restSample.setQuantity(sample.getQuantity());
		restSample.setHazards(sample.getHazards());
		restSample.setOtherHazards(sample.getOtherHazards());
		return restSample;
	}

	@Override
	protected String getModelPath() {
		return SAMPLE_MODEL_PATH;
	}

	@Override
	protected Class<Sample> getModelClass() {
		return Sample.class;
	}

	@Override
	protected Class<Sample[]> getModelArrayClass() {
		return Sample[].class;
	}
}
