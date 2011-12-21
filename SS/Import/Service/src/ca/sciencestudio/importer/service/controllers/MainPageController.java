/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *     MainPageController class.
 *     
 */
package ca.sciencestudio.importer.service.controllers;

import javax.servlet.http.HttpServletResponse;

import org.eclipse.jetty.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import ca.sciencestudio.model.facility.dao.LaboratoryAuthzDAO;
import ca.sciencestudio.model.session.Session;
import ca.sciencestudio.model.session.dao.SessionAuthzDAO;
import ca.sciencestudio.security.util.SecurityUtil;
import ca.sciencestudio.util.authz.Authorities;

@Controller
public class MainPageController {

	@Autowired
	private LaboratoryAuthzDAO laboratoryAuthzDAO;

	@Autowired
	private SessionAuthzDAO sessionAuthzDAO;

	@RequestMapping(value = { "/", "/main.html" }, method = RequestMethod.GET)
	public String handleRequest(@RequestParam("session") String sessionGid, HttpServletResponse response, ModelMap model) {

		String user = SecurityUtil.getPersonGid();

		Session session = sessionAuthzDAO.get(user, sessionGid).get();
		if (session == null) {
			response.setStatus(HttpStatus.BAD_REQUEST_400);
			model.put("error", "Session not found.");
			return "page/error";
		}

		String labName = laboratoryAuthzDAO.get(session.getLaboratoryGid()).get().getName();

		Authorities authorities = LabAuthz.createInstance(labName, sessionGid, sessionAuthzDAO).getAuthorities();

		model.put("sessionGid", sessionGid);
		model.put("authorities", authorities);
		return "page/main";
	}

}
