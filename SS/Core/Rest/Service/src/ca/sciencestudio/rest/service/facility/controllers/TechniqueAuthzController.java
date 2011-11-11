/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *     TechniqueAuthzController class.
 *     
 */
package ca.sciencestudio.rest.service.facility.controllers;

import java.util.Collections;

import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import ca.sciencestudio.model.dao.ModelBasicDAO;
import ca.sciencestudio.model.facility.Technique;
import ca.sciencestudio.model.facility.dao.TechniqueBasicDAO;
import ca.sciencestudio.model.facility.validators.TechniqueValidator;
import ca.sciencestudio.model.validators.ModelValidator;
import ca.sciencestudio.rest.service.controllers.AbstractModelAuthzController;
import ca.sciencestudio.util.exceptions.ModelAccessException;

/**
 * @author maxweld
 *
 */
@Controller
public class TechniqueAuthzController extends AbstractModelAuthzController<Technique> {

	private static final String TECHNIQUE_MODEL_PATH = "/techniques";
	
	private TechniqueBasicDAO techniqueBasicDAO;
	
	private TechniqueValidator techniqueValidator;
	
	
	//
	//	Adding, Editing and Removing InstrumentTechniques currently only done by administrator. No REST API implemented. 
	//

	@ResponseBody
	@RequestMapping(value = TECHNIQUE_MODEL_PATH + "/{gid}*", method = RequestMethod.GET)
	public Object get(@PathVariable String gid, HttpServletResponse response) throws Exception {
		// No authorization checks required. Everyone is allowed to read this information. //
		return doGet(gid, response);
	}

	@ResponseBody
	@RequestMapping(value = TECHNIQUE_MODEL_PATH + "*", method = RequestMethod.GET)
	public Object getAll(HttpServletResponse response) throws Exception {
		// No authorization checks required. Everyone is allowed to read this information. //
		try {
			return techniqueBasicDAO.getAll();
		}
		catch(ModelAccessException e) {
			response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
			return Collections.emptyList();
		}
	}

	@ResponseBody
	@RequestMapping(value = TECHNIQUE_MODEL_PATH + "*", method = RequestMethod.GET, params = "laboratory")
	public Object getAllByLaboratoryGid(@RequestParam("laboratory") String laboratoryGid, HttpServletResponse response) throws Exception {
		// No authorization checks required. Everyone is allowed to read this information. //
		try {
			return techniqueBasicDAO.getAllByLaboratoryGid(laboratoryGid);
		}
		catch(ModelAccessException e) {
			response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
			return Collections.emptyList();
		}
	}
	
	@ResponseBody
	@RequestMapping(value = TECHNIQUE_MODEL_PATH + "*", method = RequestMethod.GET, params = "instrument")
	public Object getAllByInstrumentGid(@RequestParam("instrument") String instrumentGid, HttpServletResponse response) throws Exception {
		// No authorization checks required. Everyone is allowed to read this information. //
		try {
			return techniqueBasicDAO.getAllByInstrumentGid(instrumentGid);
		}
		catch(ModelAccessException e) {
			response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
			return Collections.emptyList();
		}
	}
	
	@Override
	public String getModelPath() {
		return TECHNIQUE_MODEL_PATH;
	}
	
	@Override
	public ModelBasicDAO<Technique> getModelBasicDAO() {
		return techniqueBasicDAO;
	}
	
	@Override
	public ModelValidator<Technique> getModelValidator() {
		return techniqueValidator;
	}

	public TechniqueBasicDAO getTechniqueBasicDAO() {
		return techniqueBasicDAO;
	}
	public void setTechniqueBasicDAO(TechniqueBasicDAO techniqueBasicDAO) {
		this.techniqueBasicDAO = techniqueBasicDAO;
	}

	public TechniqueValidator getTechniqueValidator() {
		return techniqueValidator;
	}
	public void setTechniqueValidator(TechniqueValidator techniqueValidator) {
		this.techniqueValidator = techniqueValidator;
	}
}
