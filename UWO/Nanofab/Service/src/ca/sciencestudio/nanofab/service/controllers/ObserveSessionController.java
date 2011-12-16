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

import ca.sciencestudio.util.net.Tunnel;
import ca.sciencestudio.security.util.SecurityUtil;

/**
 * @author maxweld
 *
 */
@Controller
public class ObserveSessionController extends AbstractSessionController {
	
	@RequestMapping(value = "/session/observe.html", method = RequestMethod.GET)
	public String getSessionObserve(HttpServletRequest request, ModelMap model) {
		
		if(!canObserveLaboratory()) {
			return onFailure("Permission Denied", "Not permitted to observe session.", model);
		}
		
		String user = SecurityUtil.getPersonGid();
		
		InetAddress acceptAddress;
		try {
			acceptAddress = InetAddress.getByName(request.getRemoteAddr());
		}
		catch(UnknownHostException e) {
			return onFailure("Communication Error", "Unable to get remote address from request.", model);
		}

		Tunnel tunnel;
		try {
			tunnel = tunnelManager.open(user, acceptAddress, true);
		}
		catch(IOException e) {
			return onFailure("Communication Error", "Unable to open tunnel for VNC connection.", model);
		}
		
		return onSuccess(tunnel.getLocalPort(), model);
	}
}
