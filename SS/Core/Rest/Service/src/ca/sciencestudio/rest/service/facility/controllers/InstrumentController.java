/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *     InstrumentController class.
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

import ca.sciencestudio.model.facility.Instrument;
import ca.sciencestudio.model.facility.dao.InstrumentBasicDAO;
import ca.sciencestudio.model.facility.validators.InstrumentValidator;
import ca.sciencestudio.rest.service.controllers.AbstractModelController;

/**
 * @author maxweld
 * 
 *
 */
@Controller
public class InstrumentController extends AbstractModelController<Instrument, InstrumentBasicDAO, InstrumentValidator> {

	private static final String INSTRUMENT_MODEL_URL = "/instruments";
	
	@Override
	@ResponseBody
	@RequestMapping(value = INSTRUMENT_MODEL_URL + "*", method = RequestMethod.POST)
	public List<String> add(@RequestBody Instrument instrument, HttpServletRequest request, HttpServletResponse response) throws Exception {
		// TODO Auto-generated method stub
		return super.add(instrument, request, response);
	}

	@Override
	@ResponseBody
	@RequestMapping(value = INSTRUMENT_MODEL_URL + "/{facility}*", method = RequestMethod.POST)
	public List<String> add(@RequestBody Instrument instrument, @PathVariable String facility, HttpServletRequest request, HttpServletResponse response) throws Exception {
		// TODO Auto-generated method stub
		return super.add(instrument, facility, request, response);
	}

	@Override
	@ResponseBody
	@RequestMapping(value = INSTRUMENT_MODEL_URL + "/{gid}*", method = RequestMethod.PUT)
	public void edit(@RequestBody Instrument instrument, @PathVariable String gid, HttpServletResponse response) throws Exception {
		// TODO Auto-generated method stub
		super.edit(instrument, gid, response);
	}

	@Override
	@ResponseBody
	@RequestMapping(value = INSTRUMENT_MODEL_URL + "/{gid}*", method = RequestMethod.DELETE)
	public void remove(@PathVariable String gid, HttpServletResponse response)
			throws Exception {
		// TODO Auto-generated method stub
		super.remove(gid, response);
	}

	@Override
	@ResponseBody
	@RequestMapping(value = INSTRUMENT_MODEL_URL + "/{gid}*", method = RequestMethod.GET)
	public Object get(@PathVariable String gid, HttpServletResponse response) throws Exception {
		// TODO Auto-generated method stub
		return super.get(gid, response);
	}

	@Override
	@ResponseBody
	@RequestMapping(value = INSTRUMENT_MODEL_URL + "*", method = RequestMethod.GET)
	public List<Instrument> getAll(HttpServletResponse response) {
		// TODO Auto-generated method stub
		return super.getAll(response);
	}
	
//	@ResponseBody
//	@RequestMapping(value = INSTRUMENT_MODEL_URL + "*", method = RequestMethod.GET, params = "laboratoryId" )
//	public List<Instrument> getAllByLaboratoryId(@RequestParam int laboratoryId, HttpServletResponse response) {
//		try {
//			return getInstrumentBasicDAO().getAllByLaboratoryId(laboratoryId);
//		}
//		catch(ModelAccessException e) {
//			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
//			return Collections.emptyList();
//		}
//	}
	
//	@ResponseBody
//	@RequestMapping(value = INSTRUMENT_MODEL_URL + "*", method = RequestMethod.GET, params = "name")
//	public List<Instrument> getAllByName(@RequestParam String name, HttpServletResponse response) {
//		try {
//			return getInstrumentBasicDAO().getAllByName(name);
//		}
//		catch(ModelAccessException e) {
//			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
//			return Collections.emptyList();
//		}
//	}
	
//	@ResponseBody
//	@RequestMapping(value = INSTRUMENT_MODEL_URL + "*", method = RequestMethod.GET, params = { "name", "laboratoryId" })
//	public List<Instrument> getAllByNameAndLaboratoryId(String name, int laboratoryId, HttpServletResponse response) {
//		try {
//			return getInstrumentBasicDAO().getAllByNameAndLaboratoryId(name, laboratoryId);
//		}
//		catch(ModelAccessException e) {
//			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
//			return Collections.emptyList();
//		}
//	}
	
	@Override
	protected String getModelUrl() {
		return INSTRUMENT_MODEL_URL;
	}
	
	public InstrumentBasicDAO getInstrumentBasicDAO() {
		return getModelBasicDAO();
	}
	public void setInstrumentBasicDAO(InstrumentBasicDAO instrumentBasicDAO) {
		setModelBasicDAO(instrumentBasicDAO);
	}
}
