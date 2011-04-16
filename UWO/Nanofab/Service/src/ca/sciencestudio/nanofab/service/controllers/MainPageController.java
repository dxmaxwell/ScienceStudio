/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *      MainPageController class.
 */
package ca.sciencestudio.nanofab.service.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import ca.sciencestudio.security.util.AuthorityUtil;
import ca.sciencestudio.security.util.SecurityUtil;
import ca.sciencestudio.nanofab.state.NanofabSessionStateMap;

/**
 * @author maxweld
 *
 */
@Controller
public class MainPageController {

	private static final String MODEL_KEY_PERSON_UID = "personUid";
	
	private static final String SUCCESS_VIEW = "page/main";
	private static final String FAILURE_VIEW = "page/error";

	private static final String DEFAULT_PERSON_UID = "";
	
	private NanofabSessionStateMap nanofabSessionStateMap;
	
	@RequestMapping(value = "/main.html", method = RequestMethod.GET)
	public String getMainPage(@RequestParam int sessionId, ModelMap model) {
		
		int runningSessionId = nanofabSessionStateMap.getRunningSessionId();
		
		if(runningSessionId != sessionId) {
			model.put("error", "Session has not been started.");
			model.put("errorMessage", "<a href=\"\">Try Again</a>");
			return FAILURE_VIEW;
		}
		
		int projectId = nanofabSessionStateMap.getProjectId();
		Object admin = AuthorityUtil.buildRoleAuthority("ADMIN_NANOFAB");
		Object group = AuthorityUtil.buildProjectGroupAuthority(projectId);
		Object exptr = AuthorityUtil.buildProjectExperimenterAuthority(projectId);
		
		if(!SecurityUtil.hasAnyAuthority(group, admin)) {
			model.put("error", "Not permitted to view session.");
			model.put("errorMessage", "<a href=\"\">Try Again</a>");
			return FAILURE_VIEW;
		}
		
		if(nanofabSessionStateMap.getControllerUid().equals(DEFAULT_PERSON_UID)) {
			if(SecurityUtil.hasAnyAuthority(exptr, admin)) {
				nanofabSessionStateMap.setController(SecurityUtil.getPerson());
			}
		}
		
		model.put(MODEL_KEY_PERSON_UID, SecurityUtil.getPerson().getUid());
		return SUCCESS_VIEW;
	}
	
	public void setNanofabSessionStateMap(NanofabSessionStateMap nanofabSessionStateMap) {
		this.nanofabSessionStateMap = nanofabSessionStateMap;
	}
	public NanofabSessionStateMap getNanofabSessionStateMap() {
		return nanofabSessionStateMap;
	}
}
