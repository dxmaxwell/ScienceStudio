/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *     RestSampleAuthzDAO class.
 *     
 */
package ca.sciencestudio.model.sample.dao.rest;

import ca.sciencestudio.model.sample.Sample;
import ca.sciencestudio.model.sample.dao.SampleAuthzDAO;
import ca.sciencestudio.model.sample.dao.rest.support.RestSample;
import ca.sciencestudio.model.dao.rest.AbstractRestModelAuthzDAO;

/**
 * @author maxweld
 *
 */
public class RestSampleAuthzDAO extends AbstractRestModelAuthzDAO<Sample, RestSample> implements SampleAuthzDAO {

	@Override
	protected RestSample toRestModel(Sample sample) {
		RestSample restSample = new RestSample();
		restSample.setProjectId(sample.getProjectId());
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
	protected String getModelUrl() {
		return getBaseUrl() + "/projects";
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
