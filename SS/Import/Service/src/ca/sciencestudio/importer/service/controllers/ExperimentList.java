/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *     ExperimentList class.
 *     
 */
package ca.sciencestudio.importer.service.controllers;

import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.eclipse.jetty.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import ca.sciencestudio.model.dao.Data;
import ca.sciencestudio.model.session.Experiment;
import ca.sciencestudio.model.session.Session;
import ca.sciencestudio.model.session.dao.ExperimentAuthzDAO;
import ca.sciencestudio.model.session.dao.SessionAuthzDAO;
import ca.sciencestudio.security.util.SecurityUtil;
import ca.sciencestudio.util.web.FormResponseMap;

@Controller
public class ExperimentList {

	@Autowired
	private ExperimentAuthzDAO experimentAuthzDAO;

	@Autowired
	private SessionAuthzDAO sessionAuthzDAO;

	@ResponseBody
	@RequestMapping(value = "/experiments*", method = RequestMethod.POST)
	public FormResponseMap requestHandler(@RequestParam String sessionGid, HttpServletResponse response) {

		String user = SecurityUtil.getPersonGid();

		Session session = sessionAuthzDAO.get(user, sessionGid).get();
		if (session == null) {
			response.setStatus(HttpStatus.BAD_REQUEST_400);
			return new FormResponseMap(false, "Session not found.");
		}

		Data<List<Experiment>> experimentListData = experimentAuthzDAO.getAllBySessionGid(user, sessionGid);

		List<Experiment> experimentList = experimentListData.get();
		if (experimentList == null) {
			response.setStatus(HttpStatus.BAD_REQUEST_400);
			return new FormResponseMap(false, "Experiments not found.");
		}

		FormResponseMap formResponse = new FormResponseMap(true);
		formResponse.put("experiments", experimentList);
		return formResponse;
	}

}
