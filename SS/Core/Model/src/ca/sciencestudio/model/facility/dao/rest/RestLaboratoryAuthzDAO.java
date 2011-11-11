/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *     RestLaboratoryAuthzDAO interface.
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
import ca.sciencestudio.model.facility.Laboratory;
import ca.sciencestudio.model.facility.dao.LaboratoryAuthzDAO;
import ca.sciencestudio.model.facility.dao.rest.support.RestLaboratory;
import ca.sciencestudio.util.exceptions.ModelAccessException;

/**
 * @author maxweld
 * 
 *
 */
public class RestLaboratoryAuthzDAO extends AbstractRestModelAuthzDAO<Laboratory> implements LaboratoryAuthzDAO {

	public static final String LABORATORY_MODEL_PATH = "/model/laboratories";

	@Override
	public Data<List<Laboratory>> getAllByFacilityGid(String facilityGid) {
		List<Laboratory> laboratories;
		try {
			laboratories = Arrays.asList(getRestTemplate().getForObject(getModelUrl("", "facility={facility}"), getModelArrayClass(), facilityGid));
		}
		catch(RestClientException e) {
			logger.warn("Rest Client exception while getting Laboratory list: " + e.getMessage());
			return new SimpleData<List<Laboratory>>(new ModelAccessException(e));
		}
		
		if(logger.isDebugEnabled()) {
			logger.debug("Get all Models, size: " + laboratories.size());
		}
		return new SimpleData<List<Laboratory>>(Collections.unmodifiableList(laboratories));
	}
	
	@Override
	protected Object toRestModel(Laboratory laboratory) {
		RestLaboratory restLaboratory = new RestLaboratory();
		restLaboratory.setFacilityGid(laboratory.getFacilityGid());
		restLaboratory.setName(laboratory.getName());
		restLaboratory.setLongName(laboratory.getLongName());
		restLaboratory.setDescription(laboratory.getDescription());
		restLaboratory.setPhoneNumber(laboratory.getPhoneNumber());
		restLaboratory.setEmailAddress(laboratory.getEmailAddress());
		restLaboratory.setLocation(laboratory.getLocation());
		restLaboratory.setViewUrl(laboratory.getViewUrl());
		return restLaboratory;
	}

	@Override
	protected String getModelPath() {
		return LABORATORY_MODEL_PATH;
	}

	@Override
	protected Class<Laboratory> getModelClass() {
		return Laboratory.class;
	}

	@Override
	protected Class<Laboratory[]> getModelArrayClass() {
		return Laboratory[].class;
	}
}
