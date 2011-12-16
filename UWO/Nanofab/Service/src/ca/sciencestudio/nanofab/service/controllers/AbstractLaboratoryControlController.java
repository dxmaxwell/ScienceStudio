/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *     AbstractLaboratoryControlController class.
 *     
 */
package ca.sciencestudio.nanofab.service.controllers;

import ca.sciencestudio.util.authz.Authorities;

/**
 * @author maxweld
 * 
 *
 */
public abstract class AbstractLaboratoryControlController extends AbstractLaboratoryAuthzController {

	protected boolean canObserveLaboratory() {
		return canReadLaboratory();
	}
	
	protected boolean canControlLaboratory() {
		Authorities authorities = getAuthorities();
		if(authorities == null) {
			return false;
		}
		return authorities.containsAny(SESSION_EXPERIMENTER, FACILITY_ADMIN_NANOFAB);
	}
}
