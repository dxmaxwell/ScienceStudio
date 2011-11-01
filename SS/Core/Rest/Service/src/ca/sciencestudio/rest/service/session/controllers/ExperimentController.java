/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *     ExperimentController class.
 *     
 */
package ca.sciencestudio.rest.service.session.controllers;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import ca.sciencestudio.model.session.Experiment;
import ca.sciencestudio.model.session.dao.ExperimentBasicDAO;
import ca.sciencestudio.model.session.validators.ExperimentValidator;
import ca.sciencestudio.rest.service.controllers.AbstractModelController;

/**
 * @author maxweld
 * 
 *
 */
@Controller
public class ExperimentController extends AbstractModelController<Experiment, ExperimentBasicDAO, ExperimentValidator> {

	private static final String EXPERIMENT_MODEL_URL = "/experiments";
	
	public ExperimentController() {
		setValidator(new ExperimentValidator());
	}
	
	@Override
	@ResponseBody
	@RequestMapping(value = EXPERIMENT_MODEL_URL + "*", method = RequestMethod.POST)
	public List<String> add(@RequestBody Experiment e, HttpServletRequest request, HttpServletResponse response) throws Exception {
		// TODO Auto-generated method stub
		return super.add(e, request, response);
	}

	@Override
	@ResponseBody
	@RequestMapping(value = EXPERIMENT_MODEL_URL + "/{facility}*", method = RequestMethod.POST)
	public List<String> add(@RequestBody Experiment e, @PathVariable String facility, HttpServletRequest request, HttpServletResponse response) throws Exception {
		// TODO Auto-generated method stub
		return super.add(e, facility, request, response);
	}

	@Override
	@ResponseBody
	@RequestMapping(value = EXPERIMENT_MODEL_URL + "/{gid}*", method = RequestMethod.PUT)
	public void edit(@RequestBody Experiment e, @PathVariable String gid, HttpServletResponse response) throws Exception {
		// TODO Auto-generated method stub
		super.edit(e, gid, response);
	}

	@Override
	@ResponseBody
	@RequestMapping(value = EXPERIMENT_MODEL_URL + "/{gid}*", method = RequestMethod.DELETE)
	public void remove(@PathVariable String gid, HttpServletResponse response) throws Exception {
		// TODO Auto-generated method stub
		super.remove(gid, response);
	}

	@Override
	@ResponseBody
	@RequestMapping(value = EXPERIMENT_MODEL_URL + "/{gid}*", method = RequestMethod.GET)
	public Object get(@PathVariable String gid, HttpServletResponse response) throws Exception {
		// TODO Auto-generated method stub
		return super.get(gid, response);
	}

	@Override
	@ResponseBody
	@RequestMapping(value = EXPERIMENT_MODEL_URL + "*", method = RequestMethod.GET)
	public List<Experiment> getAll(HttpServletResponse response) {
		// TODO Auto-generated method stub
		return super.getAll(response);
	}

	@Override
	protected String getModelUrl() {
		return EXPERIMENT_MODEL_URL;
	}
	
	public ExperimentBasicDAO getExperimentBasicDAO() {
		return getModelBasicDAO();
	}
	public void setExperimentBasicDAO(ExperimentBasicDAO experimentBasicDAO) {
		setModelBasicDAO(experimentBasicDAO);
	}
}
