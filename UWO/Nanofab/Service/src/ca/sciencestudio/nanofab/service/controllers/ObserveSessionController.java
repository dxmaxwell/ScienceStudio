/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *      ObserveSessionController class.	     
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

/**
 * @author maxweld
 *
 */
@Controller
public class ObserveSessionController extends AbstractSessionController {
	
	@RequestMapping(value = "/session/observe.html", method = RequestMethod.GET)
	public String getSessionObserve(HttpServletRequest request, ModelMap model) {
		
		int projectId = nanofabSessionStateMap.getProjectId();
		
		Object admin = AuthorityUtil.buildRoleAuthority("ADMIN_NANOFAB");
		Object group = AuthorityUtil.buildProjectGroupAuthority(projectId);
		
		if(!SecurityUtil.hasAnyAuthority(group, admin)) {
			return onFailure("Permission Denied", "Not permitted to observe session.", model);
		}
		
		Person person = SecurityUtil.getPerson();
		
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
