/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *     FacilityController class.
 *     
 */
package ca.sciencestudio.rest.service.facility.controllers;

import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import ca.sciencestudio.model.facility.Facility;
import ca.sciencestudio.model.facility.dao.FacilityBasicDAO;
import ca.sciencestudio.model.facility.validators.FacilityValidator;
import ca.sciencestudio.rest.service.controllers.AbstractModelController;
import ca.sciencestudio.util.exceptions.ModelAccessException;

/**
 * @author maxweld
 *
 */
@Controller
public class FacilityController extends AbstractModelController<Facility, FacilityBasicDAO, FacilityValidator> {

	private static final String FACILITY_MODEL_URL = "/facilities";
	
	public FacilityController() {
		setValidator(new FacilityValidator());
	}
	
	//
	// Ad-hoc addition of facilities is not permitted. No RESTful API is currently needed. 
	//
	//@Override
	//@ResponseBody
	//@RequestMapping(value = FACILITY_MODEL_URL + "*", method = RequestMethod.POST)
	//public List<String> add(@RequestBody Facility facility, HttpServletRequest request, HttpServletResponse response) throws Exception {
	//	return super.add(facility, request, response);
	//}
	//
	//@Override
	//@ResponseBody
	//@RequestMapping(value = FACILITY_MODEL_URL + "/{name}*", method = RequestMethod.POST)
	//public List<String> add(@RequestBody Facility facility, @PathVariable String name, HttpServletRequest request, HttpServletResponse response) throws Exception {
	//	return super.add(facility, name, request, response);
	//}

	@Override
	@ResponseBody
	@RequestMapping(value = FACILITY_MODEL_URL + "/{gid}*", method = RequestMethod.PUT)
	public void edit(@RequestBody Facility facility, @PathVariable String gid, HttpServletResponse response) throws Exception {
		// TODO Auto-generated method stub
		super.edit(facility, gid, response);
	}

	//
	// Ad-hoc removal of facilities is not permitted. No RESTful API is currently needed.
	//
	//@Override
	//@ResponseBody
	//@RequestMapping(value = FACILITY_MODEL_URL + "/{gid}*", method = RequestMethod.DELETE)
	//public void remove(@PathVariable String gid, HttpServletResponse response) throws Exception {
	//	super.remove(gid, response);
	//}

	@Override
	@ResponseBody
	@RequestMapping(value = FACILITY_MODEL_URL + "/{gid}*", method = RequestMethod.GET)
	public Object get(@PathVariable String gid, HttpServletResponse response) throws Exception {
		// TODO Auto-generated method stub
		return super.get(gid, response);
	}
	
	@ResponseBody
	@RequestMapping(value = FACILITY_MODEL_URL + "/name/{name}*", method = RequestMethod.GET)
	public Object getByName(@PathVariable String name, HttpServletResponse response) throws Exception {
		Facility facility;
		try {
			facility = getFacilityBasicDAO().getByName(name);
		}
		catch(ModelAccessException e) {
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			return Collections.emptyMap();
		}
		
		if(facility == null) {
			response.setStatus(HttpServletResponse.SC_NOT_FOUND);
			return Collections.emptyMap();
		}
		// TODO: Auto-generated method stub
		return facility; 
	}
	
	@Override
	@ResponseBody
	@RequestMapping(value = FACILITY_MODEL_URL + "*", method = RequestMethod.GET)
	public List<Facility> getAll(HttpServletResponse response) {
		// TODO Auto-generated method stub
		return super.getAll(response);
	}

	@Override
	protected String getModelUrl() {
		return FACILITY_MODEL_URL;
	}
	
	public FacilityBasicDAO getFacilityBasicDAO() {
		return getModelBasicDAO();
	}
	public void setFacilityBasicDAO(FacilityBasicDAO facilityBasicDAO) {
		setModelBasicDAO(facilityBasicDAO);
	}
}
