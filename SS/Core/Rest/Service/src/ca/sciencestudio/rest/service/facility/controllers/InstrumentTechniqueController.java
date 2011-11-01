/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *     InstrumentTechniqueController class.
 *     
 */
package ca.sciencestudio.rest.service.facility.controllers;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import ca.sciencestudio.model.facility.InstrumentTechnique;
import ca.sciencestudio.model.facility.dao.InstrumentTechniqueBasicDAO;
import ca.sciencestudio.model.facility.validators.InstrumentTechniqueValidator;
import ca.sciencestudio.rest.service.controllers.AbstractModelController;

/**
 * @author maxweld
 *
 *
 */
@Controller
public class InstrumentTechniqueController extends AbstractModelController<InstrumentTechnique, InstrumentTechniqueBasicDAO, InstrumentTechniqueValidator> {

	private static final String INSTRUMENT_TECHNIQUE_MODEL_URL = "/instrument/techniques";
	
	@Override
	@ResponseBody
	@RequestMapping(value = INSTRUMENT_TECHNIQUE_MODEL_URL + "*", method = RequestMethod.POST)
	public List<String> add(@RequestBody InstrumentTechnique it, HttpServletRequest request, HttpServletResponse response) throws Exception {
		// TODO Auto-generated method stub
		return super.add(it, request, response);
	}
	
	@Override
	@ResponseBody
	@RequestMapping(value = INSTRUMENT_TECHNIQUE_MODEL_URL + "/{facility}*", method = RequestMethod.POST)
	public List<String> add(@RequestBody InstrumentTechnique it, @PathVariable String facility, HttpServletRequest request, HttpServletResponse response) throws Exception {
		// TODO Auto-generated method stub
		return super.add(it, facility, request, response);
	}

	@Override
	@ResponseBody
	@RequestMapping(value = INSTRUMENT_TECHNIQUE_MODEL_URL + "/{gid}*", method = RequestMethod.PUT)
	public void edit(@RequestBody InstrumentTechnique it, @PathVariable String gid, HttpServletResponse response) throws Exception {
		// TODO Auto-generated method stub
		super.edit(it, gid, response);
	}
	
	@Override
	@ResponseBody
	@RequestMapping(value = INSTRUMENT_TECHNIQUE_MODEL_URL + "/{gid}*", method = RequestMethod.DELETE)
	public void remove(@PathVariable String gid, HttpServletResponse response) throws Exception {
		// TODO Auto-generated method stub
		super.remove(gid, response);
	}

	@Override
	@ResponseBody
	@RequestMapping(value = INSTRUMENT_TECHNIQUE_MODEL_URL + "/{gid}*", method = RequestMethod.GET)
	public Object get(@PathVariable String gid, HttpServletResponse response) throws Exception {
		// TODO Auto-generated method stub
		return super.get(gid, response);
	}

	@Override
	@ResponseBody
	@RequestMapping(value = INSTRUMENT_TECHNIQUE_MODEL_URL + "*", method = RequestMethod.GET)
	public List<InstrumentTechnique> getAll(HttpServletResponse response) {
		// TODO Auto-generated method stub
		return super.getAll(response);
	}

	@Override
	protected String getModelUrl() {
		return INSTRUMENT_TECHNIQUE_MODEL_URL;
	}
	
	public InstrumentTechniqueBasicDAO getInstrumentTechniqueBasicDAO() {
		return getModelBasicDAO();
	}
	public void setInstrumentTechniqueBasicDAO(InstrumentTechniqueBasicDAO instrumentTechniqueBasicDAO) {
		setModelBasicDAO(instrumentTechniqueBasicDAO);
	}
}
