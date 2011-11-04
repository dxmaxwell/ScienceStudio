/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *     InstrumentTechniqueAuthzController class.
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
import ca.sciencestudio.model.facility.InstrumentTechnique;
import ca.sciencestudio.model.facility.dao.InstrumentTechniqueBasicDAO;
import ca.sciencestudio.model.facility.validators.InstrumentTechniqueValidator;
import ca.sciencestudio.model.validators.ModelValidator;
import ca.sciencestudio.rest.service.controllers.AbstractModelAuthzController;
import ca.sciencestudio.util.exceptions.ModelAccessException;

/**
 * @author maxweld
 *
 *
 */
@Controller
public class InstrumentTechniqueAuthzController extends AbstractModelAuthzController<InstrumentTechnique> {

	private static final String INSTRUMENT_TECHNIQUE_MODEL_PATH = "/instrument/techniques";
	
	private InstrumentTechniqueBasicDAO instrumentTechniqueBasicDAO;
	
	private InstrumentTechniqueValidator instrumentTechniqueValidator;
	
	//
	//	Adding, Editing and Removing InstrumentTechniques currently only done by administrator. No REST API implemented. 
	//
	
	@ResponseBody
	@RequestMapping(value = INSTRUMENT_TECHNIQUE_MODEL_PATH + "/{gid}*", method = RequestMethod.GET)
	public Object get(@PathVariable String gid, HttpServletResponse response) throws Exception {
		return doGet(gid, response);
	}

	@ResponseBody
	@RequestMapping(value = INSTRUMENT_TECHNIQUE_MODEL_PATH + "*", method = RequestMethod.GET)
	public Object getAll(HttpServletResponse response) throws Exception {
		// No authorization checks required. Everyone is allowed to read this information. //
		try {
			return instrumentTechniqueBasicDAO.getAll();
		}
		catch(ModelAccessException e) {
			response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
			return Collections.emptyList();
		}
	}
			
	@ResponseBody
	@RequestMapping(value = INSTRUMENT_TECHNIQUE_MODEL_PATH + "*", method = RequestMethod.GET, params = "laboratory")
	public Object getAll(@RequestParam("laboratory") String laboratoryGid, HttpServletResponse response) throws Exception {
		// No authorization checks required. Everyone is allowed to read this information. //
		try {
			return instrumentTechniqueBasicDAO.getAllByLaboratoryGid(laboratoryGid);
		}
		catch(ModelAccessException e) {
			response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
			return Collections.emptyList();
		}
	}

	@Override
	public String getModelPath() {
		return INSTRUMENT_TECHNIQUE_MODEL_PATH;
	}
	
	@Override
	public ModelBasicDAO<InstrumentTechnique> getModelBasicDAO() {
		return instrumentTechniqueBasicDAO;
	}
	
	@Override
	public ModelValidator<InstrumentTechnique> getModelValidator() {
		return instrumentTechniqueValidator;
	}

	public InstrumentTechniqueBasicDAO getInstrumentTechniqueBasicDAO() {
		return instrumentTechniqueBasicDAO;
	}
	public void setInstrumentTechniqueBasicDAO(InstrumentTechniqueBasicDAO instrumentTechniqueBasicDAO) {
		this.instrumentTechniqueBasicDAO = instrumentTechniqueBasicDAO;
	}

	public InstrumentTechniqueValidator getInstrumentTechniqueValidator() {
		return instrumentTechniqueValidator;
	}
	public void setInstrumentTechniqueValidator(InstrumentTechniqueValidator instrumentTechniqueValidator) {
		this.instrumentTechniqueValidator = instrumentTechniqueValidator;
	}
}
