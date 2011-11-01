/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *     SampleController class.
 *     
 */
package ca.sciencestudio.rest.service.sample.controllers;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import ca.sciencestudio.model.sample.Sample;
import ca.sciencestudio.model.sample.dao.SampleBasicDAO;
import ca.sciencestudio.model.sample.validators.SampleValidator;
import ca.sciencestudio.rest.service.controllers.AbstractModelController;

/**
 * @author maxweld
 *
 */
@Controller
public class SampleController extends AbstractModelController<Sample, SampleBasicDAO, SampleValidator> {

	private static final String SAMPLE_MODEL_URL = "/samples";
	
	public SampleController() {
		setValidator(new SampleValidator());
	}
	
	@Override
	@ResponseBody
	@RequestMapping(value = SAMPLE_MODEL_URL + "*", method = RequestMethod.POST)
	public List<String> add(@RequestBody Sample sample, HttpServletRequest request, HttpServletResponse response) throws Exception {
		return super.add(sample, request, response);
	}

	@Override
	@ResponseBody
	@RequestMapping(value = SAMPLE_MODEL_URL + "/{facility}*", method = RequestMethod.POST)
	public List<String> add(@RequestBody Sample sample, @PathVariable String facility, HttpServletRequest request, HttpServletResponse response) throws Exception {
		return super.add(sample, facility, request, response);
	}

	@Override
	@ResponseBody
	@RequestMapping(value = SAMPLE_MODEL_URL + "/{gid}*", method = RequestMethod.PUT)
	public void edit(@RequestBody Sample sample, @PathVariable String gid, HttpServletResponse response) throws Exception {
		super.edit(sample, gid, response);
	}

	@Override
	@ResponseBody
	@RequestMapping(value = SAMPLE_MODEL_URL + "/{gid}*", method = RequestMethod.DELETE)
	public void remove(@PathVariable String gid, HttpServletResponse response) throws Exception {
		super.remove(gid, response);
	}

	@Override
	@ResponseBody
	@RequestMapping(value = SAMPLE_MODEL_URL + "/{gid}*", method = RequestMethod.GET)
	public Object get(@PathVariable String gid, HttpServletResponse response) throws Exception {
		return super.get(gid, response);
	}
	
	@Override
	@ResponseBody
	@RequestMapping(value = SAMPLE_MODEL_URL + "*", method = RequestMethod.GET)
	public List<Sample> getAll(HttpServletResponse response) {
		return super.getAll(response);
	}

	@Override
	protected String getModelUrl() {
		return SAMPLE_MODEL_URL;
	}
	
	public SampleBasicDAO getSampleBasicDAO() {
		return getModelBasicDAO();
	}
	public void setSampleBasicDAO(SampleBasicDAO sampleBasicDAO) {
		setModelBasicDAO(sampleBasicDAO);
	}
}
