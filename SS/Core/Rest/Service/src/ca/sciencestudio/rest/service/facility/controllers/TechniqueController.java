/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *     TechniqueController class.
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

import ca.sciencestudio.model.facility.Technique;
import ca.sciencestudio.model.facility.dao.TechniqueBasicDAO;
import ca.sciencestudio.model.facility.validators.TechniqueValidator;
import ca.sciencestudio.rest.service.controllers.AbstractModelController;

/**
 * @author maxweld
 *
 */
@Controller
public class TechniqueController extends AbstractModelController<Technique, TechniqueBasicDAO, TechniqueValidator> {

	private static final String TECHNIQUE_MODEL_URL = "/techniques";
	
	public TechniqueController() {
		setValidator(new TechniqueValidator());
	}

	@Override
	@ResponseBody
	@RequestMapping(value = TECHNIQUE_MODEL_URL + "*", method = RequestMethod.POST)
	public List<String> add(@RequestBody Technique t, HttpServletRequest request, HttpServletResponse response) throws Exception {
		// TODO Auto-generated method stub
		return super.add(t, request, response);
	}

	@Override
	@ResponseBody
	@RequestMapping(value = TECHNIQUE_MODEL_URL + "/{facility}*", method = RequestMethod.POST)
	public List<String> add(@RequestBody Technique t, @PathVariable String facility, HttpServletRequest request, HttpServletResponse response) throws Exception {
		// TODO Auto-generated method stub
		return super.add(t, facility, request, response);
	}

	@Override
	@ResponseBody
	@RequestMapping(value = TECHNIQUE_MODEL_URL + "/{gid}*", method = RequestMethod.PUT)
	public void edit(@RequestBody Technique t, @PathVariable String gid, HttpServletResponse response) throws Exception {
		// TODO Auto-generated method stub
		super.edit(t, gid, response);
	}

	@Override
	@ResponseBody
	@RequestMapping(value = TECHNIQUE_MODEL_URL + "/{gid}*", method = RequestMethod.DELETE)
	public void remove(@PathVariable String gid, HttpServletResponse response) throws Exception {
		// TODO Auto-generated method stub
		super.remove(gid, response);
	}

	@Override
	@ResponseBody
	@RequestMapping(value = TECHNIQUE_MODEL_URL + "/{gid}*", method = RequestMethod.GET)
	public Object get(@PathVariable String gid, HttpServletResponse response) throws Exception {
		// TODO Auto-generated method stub
		return super.get(gid, response);
	}

	@Override
	@ResponseBody
	@RequestMapping(value = TECHNIQUE_MODEL_URL + "*", method = RequestMethod.GET)
	public List<Technique> getAll(HttpServletResponse response) {
		// TODO Auto-generated method stub
		return super.getAll(response);
	}

	@Override
	protected String getModelUrl() {
		return TECHNIQUE_MODEL_URL;
	}
	
	public TechniqueBasicDAO getTechniqueBasicDAO() {
		return getModelBasicDAO();
	}
	public void setTechniqueBasicDAO(TechniqueBasicDAO techniqueBasicDAO) {
		setModelBasicDAO(techniqueBasicDAO);
	}
}
