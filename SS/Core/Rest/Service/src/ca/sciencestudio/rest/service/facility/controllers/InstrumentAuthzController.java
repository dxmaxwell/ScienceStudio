/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *     InstrumentAuthzController class.
 *     
 */
package ca.sciencestudio.rest.service.facility.controllers;

import java.util.Collections;

import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import ca.sciencestudio.model.Permissions;
import ca.sciencestudio.model.dao.ModelBasicDAO;
import ca.sciencestudio.model.facility.Instrument;
import ca.sciencestudio.model.facility.dao.InstrumentBasicDAO;
import ca.sciencestudio.model.facility.validators.InstrumentValidator;
import ca.sciencestudio.model.validators.ModelValidator;
import ca.sciencestudio.rest.service.controllers.AbstractModelAuthzController;
import ca.sciencestudio.util.exceptions.ModelAccessException;

/**
 * @author maxweld
 * 
 *
 */
@Controller
public class InstrumentAuthzController extends AbstractModelAuthzController<Instrument> {

	private static final String INSTRUMENT_MODEL_PATH = "/instruments";
	
	private InstrumentBasicDAO instrumentBasicDAO;
	
	private InstrumentValidator instrumentValidator;
	
	@ResponseBody
	@RequestMapping(value = INSTRUMENT_MODEL_PATH + "/perms*", method = RequestMethod.GET)
	public Permissions permissions(@RequestParam String user) {
		if(hasLoginRole(user, LOGIN_ROLE_ADMIN_FACILITY)) {
			return new Permissions(true);
		} else {
			return new Permissions(false);
		}
	}
	
	@ResponseBody
	@RequestMapping(value = INSTRUMENT_MODEL_PATH + "/{gid}/perms*", method = RequestMethod.GET)
	public Permissions permissions(@RequestParam String user, @PathVariable String gid) {
		if(hasLoginRole(user, LOGIN_ROLE_ADMIN_FACILITY)) {
			return new Permissions(true);
		} else {
			return new Permissions(false);
		}
	}
	
	//
	//	Adding, Editing and Removing Instruments currently only done by administrator. No REST API implemented. 
	//	

	@ResponseBody
	@RequestMapping(value = INSTRUMENT_MODEL_PATH + "/{gid}*", method = RequestMethod.GET)
	public Object get(@PathVariable String gid, HttpServletResponse response) throws Exception {
		// No authorization checks required. Everyone is allowed to read this information. //
		return doGet(gid, response);
	}

	@ResponseBody
	@RequestMapping(value = INSTRUMENT_MODEL_PATH + "*", method = RequestMethod.GET)
	public Object getAll(HttpServletResponse response) {
		try {
			return instrumentBasicDAO.getAll();
		}
		catch(ModelAccessException e) {
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			return Collections.emptyMap();
		}
	}
	
	@ResponseBody
	@RequestMapping(value = INSTRUMENT_MODEL_PATH + "*", method = RequestMethod.GET, params = "laboratory" )
	public Object getAll(@RequestParam("laboratory") String laboratoryGid, HttpServletResponse response) {
		try {
			return getInstrumentBasicDAO().getAllByLaboratoryGid(laboratoryGid);
		}
		catch(ModelAccessException e) {
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			return Collections.emptyList();
		}
	}
	
	@Override
	public String getModelPath() {
		return INSTRUMENT_MODEL_PATH;
	}
	
	@Override
	public ModelBasicDAO<Instrument> getModelBasicDAO() {
		return instrumentBasicDAO;
	}

	@Override
	public ModelValidator<Instrument> getModelValidator() {
		return instrumentValidator;
	}

	public InstrumentBasicDAO getInstrumentBasicDAO() {
		return instrumentBasicDAO;
	}
	public void setInstrumentBasicDAO(InstrumentBasicDAO instrumentBasicDAO) {
		this.instrumentBasicDAO = instrumentBasicDAO;
	}

	public InstrumentValidator getInstrumentValidator() {
		return instrumentValidator;
	}
	public void setInstrumentValidator(InstrumentValidator instrumentValidator) {
		this.instrumentValidator = instrumentValidator;
	}
}
