/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *     LaboratoryController class.
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

import ca.sciencestudio.model.facility.Laboratory;
import ca.sciencestudio.model.facility.dao.LaboratoryBasicDAO;
import ca.sciencestudio.model.facility.validators.LaboratoryValidator;
import ca.sciencestudio.rest.service.controllers.AbstractModelController;

/**
 * @author maxweld
 * 
 *
 */
@Controller
public class LaboratoryController extends AbstractModelController<Laboratory, LaboratoryBasicDAO, LaboratoryValidator> {

	private static final String LABORATORY_MODEL_URL = "/laboratories";
	
	public LaboratoryController() {
		setValidator(new LaboratoryValidator());
	}
	
	@Override
	@ResponseBody
	@RequestMapping(value = LABORATORY_MODEL_URL + "*", method = RequestMethod.POST)
	public List<String> add(@RequestBody Laboratory l, HttpServletRequest request, HttpServletResponse response) throws Exception {
		// TODO Auto-generated method stub
		return super.add(l, request, response);
	}

	@Override
	@ResponseBody
	@RequestMapping(value = LABORATORY_MODEL_URL + "/{facility}*", method = RequestMethod.POST)
	public List<String> add(@RequestBody Laboratory l, @PathVariable String facility, HttpServletRequest request, HttpServletResponse response) throws Exception {
		// TODO Auto-generated method stub
		return super.add(l, facility, request, response);
	}

	@Override
	@ResponseBody
	@RequestMapping(value = LABORATORY_MODEL_URL + "/{gid}*", method = RequestMethod.PUT)
	public void edit(@RequestBody Laboratory l, @PathVariable String gid, HttpServletResponse response) throws Exception {
		// TODO Auto-generated method stub
		super.edit(l, gid, response);
	}

	@Override
	@ResponseBody
	@RequestMapping(value = LABORATORY_MODEL_URL + "/{gid}*", method = RequestMethod.DELETE)
	public void remove(@PathVariable String gid, HttpServletResponse response) throws Exception {
		// TODO Auto-generated method stub
		super.remove(gid, response);
	}

	@Override
	@ResponseBody
	@RequestMapping(value = LABORATORY_MODEL_URL + "/{gid}*", method = RequestMethod.GET)
	public Object get(@PathVariable String gid, HttpServletResponse response) throws Exception {
		// TODO Auto-generated method stub
		return super.get(gid, response);
	}

	@Override
	@ResponseBody
	@RequestMapping(value = LABORATORY_MODEL_URL + "*", method = RequestMethod.GET)
	public List<Laboratory> getAll(HttpServletResponse response) {
		// TODO Auto-generated method stub
		return super.getAll(response);
	}

	@Override
	protected String getModelUrl() {
		return LABORATORY_MODEL_URL;
	}
	
	public LaboratoryBasicDAO getLaboratoryBasicDAO() {
		return getModelBasicDAO();
	}
	public void setLaboratoryBasicDAO(LaboratoryBasicDAO laboratoryBasicDAO) {
		setModelBasicDAO(laboratoryBasicDAO);
	}
}
