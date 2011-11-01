/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *     FacilityAuthzController class.
 *     
 */
package ca.sciencestudio.rest.service.facility.controllers;

import java.util.Collections;

import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import ca.sciencestudio.model.dao.ModelBasicDAO;
import ca.sciencestudio.model.facility.Facility;
import ca.sciencestudio.model.facility.dao.FacilityBasicDAO;
import ca.sciencestudio.model.facility.validators.FacilityValidator;
import ca.sciencestudio.model.validators.ModelValidator;
import ca.sciencestudio.rest.service.controllers.AbstractModelAuthzController;
import ca.sciencestudio.util.exceptions.ModelAccessException;

/**
 * @author maxweld
 *
 */
@Controller
public class FacilityAuthzController extends AbstractModelAuthzController<Facility> {

	private static final String FACILITY_MODEL_PATH = "/facilities";
	
	private FacilityBasicDAO facilityBasicDAO;
	
	private FacilityValidator facilityValidator;
	
	//
	//	Adding, Editing and Removing Facilities currently only done by administrator. No REST API implemented. 
	//

	@ResponseBody
	@RequestMapping(value = FACILITY_MODEL_PATH + "/{gid}*", method = RequestMethod.GET)
	public Object get(@PathVariable String gid, HttpServletResponse response) throws Exception {
		// No authorization checks required. Everyone is allowed to read this information. //
		Facility facility;
		try {
			facility = getModelBasicDAO().get(gid);
		}
		catch(ModelAccessException e) {
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			return Collections.emptyMap();
		}
		
		if(facility == null) {
			try {
				facility = getFacilityBasicDAO().getByName(gid);
			}
			catch(ModelAccessException e) {
				response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
				return Collections.emptyMap();
			}
		}
		
		if(facility == null) {
			response.setStatus(HttpServletResponse.SC_NOT_FOUND);
			return Collections.emptyMap();
		}
		return facility;
	}
	
	@ResponseBody
	@RequestMapping(value = FACILITY_MODEL_PATH + "*", method = RequestMethod.GET)
	public Object getAll(HttpServletResponse response) {
		// No authorization checks required. Everyone is allowed to read this information. //
		try {
			return facilityBasicDAO.getAll();
		}
		catch(ModelAccessException e) {
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			return Collections.emptyMap();
		}
	}

	@Override
	public String getModelPath() {
		return FACILITY_MODEL_PATH;
	}

	@Override
	public ModelBasicDAO<Facility> getModelBasicDAO() {
		return facilityBasicDAO;
	}

	@Override
	public ModelValidator<Facility> getModelValidator() {
		return facilityValidator;
	}

	public FacilityBasicDAO getFacilityBasicDAO() {
		return facilityBasicDAO;
	}
	public void setFacilityBasicDAO(FacilityBasicDAO facilityBasicDAO) {
		this.facilityBasicDAO = facilityBasicDAO;
	}

	public FacilityValidator getFacilityValidator() {
		return facilityValidator;
	}
	public void setFacilityValidator(FacilityValidator facilityValidator) {
		this.facilityValidator = facilityValidator;
	}
}
