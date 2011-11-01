/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *     RestTechniqueAuthzDAO class.
 *     
 */
package ca.sciencestudio.model.facility.dao.rest;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.springframework.web.client.RestClientException;

import ca.sciencestudio.model.dao.Data;
import ca.sciencestudio.model.dao.SimpleData;
import ca.sciencestudio.model.dao.rest.AbstractRestModelAuthzDAO;
import ca.sciencestudio.model.facility.Technique;
import ca.sciencestudio.model.facility.dao.TechniqueAuthzDAO;
import ca.sciencestudio.model.facility.dao.rest.support.RestTechnique;
import ca.sciencestudio.util.exceptions.ModelAccessException;

/**
 * @author maxweld
 *
 */
public class RestTechniqueAuthzDAO extends AbstractRestModelAuthzDAO<Technique> implements TechniqueAuthzDAO {

	public static final String TECHNIQUE_MODEL_PATH = "/techniques";
	
	@Override
	public Data<List<Technique>> getAllByLaboratoryGid(String laboratoryGid) {
		List<Technique> techniques;
		try {
			techniques = Arrays.asList(getRestTemplate().getForObject(getRestUrl("", "laboratory={laboratory}"), getModelArrayClass(), laboratoryGid));
		}
		catch(RestClientException e) {
			logger.warn("Rest Client exception while getting Model list: " + e.getMessage());
			return new SimpleData<List<Technique>>(new ModelAccessException(e));
		}
		
		if(logger.isDebugEnabled()) {
			logger.debug("Get all Models, size: " + techniques.size());
		}
		return new SimpleData<List<Technique>>(Collections.unmodifiableList(techniques));
	}
	
	@Override
	protected Object toRestModel(Technique technique) {
		RestTechnique restTechnique = new RestTechnique();
		restTechnique.setName(technique.getName());
		restTechnique.setLongName(technique.getLongName());
		restTechnique.setDescription(technique.getDescription());
		return restTechnique;
	}

	@Override
	protected String getModelPath() {
		return TECHNIQUE_MODEL_PATH;
	}

	@Override
	protected Class<Technique> getModelClass() {
		return Technique.class;
	}

	@Override
	protected Class<Technique[]> getModelArrayClass() {
		return Technique[].class;
	}
}
