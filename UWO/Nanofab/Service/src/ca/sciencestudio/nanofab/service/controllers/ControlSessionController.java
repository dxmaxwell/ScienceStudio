/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *      ControlSessionController class.	     
 */
package ca.sciencestudio.nanofab.service.controllers;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import ca.sciencestudio.model.person.Person;
import ca.sciencestudio.util.net.Tunnel;
import ca.sciencestudio.security.util.AuthorityUtil;
import ca.sciencestudio.security.util.SecurityUtil;
import ca.sciencestudio.nanofab.service.controllers.AbstractSessionController;

/**
 * @author maxweld
 *
 */
@Controller
public class ControlSessionController extends AbstractSessionController {
	
	@RequestMapping(value = "/session/control.html", method = RequestMethod.GET)
	public String getSessionControl(HttpServletRequest request, ModelMap model) {
		
		int projectId = nanofabSessionStateMap.getProjectId();
		
		Object admin = AuthorityUtil.buildRoleAuthority("ADMIN_NANOFAB");
		Object exptr = AuthorityUtil.buildProjectExperimenterAuthority(projectId);
		
		if(!SecurityUtil.hasAnyAuthority(exptr, admin)) {
			return onFailure("Permission Denied", "Not permitted to control session.", model);
		}
		
		Person person = SecurityUtil.getPerson();
		
		tunnelManager.close(nanofabSessionStateMap.getControllerUid());
		nanofabSessionStateMap.setController(person);
		
		InetAddress acceptAddress;
		try {
			acceptAddress = InetAddress.getByName(request.getRemoteAddr());
		}
		catch(UnknownHostException e) {
			return onFailure("Communication Error", "Unable to get remote address from request.", model);
		}
		
		Tunnel tunnel;
		try {
			tunnel = tunnelManager.open(person.getUid(), acceptAddress, true);
		}
		catch(IOException e) {
			return onFailure("Communication Error", "Unable to open tunnel for VNC connection.", model);
		}
		
		return onSuccess(tunnel.getLocalPort(), model);
	}
}
