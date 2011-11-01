/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *     SampleController class.
 *     
 */
package ca.sciencestudio.service.sample.controllers;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import ca.sciencestudio.model.sample.Sample;
import ca.sciencestudio.model.sample.dao.SampleAuthzDAO;
import ca.sciencestudio.security.util.SecurityUtil;
import ca.sciencestudio.service.controllers.AbstractModelController;
import ca.sciencestudio.service.utilities.ModelPathUtils;

/**
 * @author maxweld
 *
 */
@Controller
public class SampleController extends AbstractModelController {

	private SampleAuthzDAO sampleAuthzDAO;
	
	@ResponseBody
	@RequestMapping(value = ModelPathUtils.SAMPLE_PATH + "*")
	public List<Sample> getSampleList(@RequestParam("project") String projectGid, ModelMap model) {
		return sampleAuthzDAO.getAllByProjectGid(SecurityUtil.getPersonGid(), projectGid).get();
	}
	
	public SampleAuthzDAO getSampleAuthzDAO() {
		return sampleAuthzDAO;
	}
	public void setSampleAuthzDAO(SampleAuthzDAO sampleAuthzDAO) {
		this.sampleAuthzDAO = sampleAuthzDAO;
	}
}
