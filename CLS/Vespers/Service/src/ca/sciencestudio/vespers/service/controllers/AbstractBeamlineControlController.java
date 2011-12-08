/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *     AbstractBeamlineControlController class.
 *     
 */
package ca.sciencestudio.vespers.service.controllers;

import ca.sciencestudio.util.authz.Authorities;

/**
 * @author maxweld
 * 
 *
 */
public abstract class AbstractBeamlineControlController extends AbstractBeamlineAuthzController {
	
	protected boolean canObserveBeamline() {
		return canReadBeamline();
	}
	
	protected boolean canControlBeamline() {
		Authorities authorities = getAuthorities();
		if(authorities == null) {
			return false;
		}
		return authorities.containsAny(SESSION_EXPERIMENTER, FACILITY_ADMIN_VESPERS);
	}
}
