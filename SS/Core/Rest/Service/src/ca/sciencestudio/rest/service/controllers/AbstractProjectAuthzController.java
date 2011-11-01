/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *     AbstractProjectAuthzController abstract class.
 *     
 */
package ca.sciencestudio.rest.service.controllers;

import ca.sciencestudio.model.Model;
import ca.sciencestudio.security.authz.accessors.ProjectAuthorityAccessor;

/**
 * @author maxweld
 *
 *
 *
 */
public abstract class AbstractProjectAuthzController<T extends Model> extends AbstractModelAuthzController<T> {
	
	protected ProjectAuthorityAccessor projectAuthorityAccessor;

	public ProjectAuthorityAccessor getProjectAuthorityAccessor() {
		return projectAuthorityAccessor;
	}
	public void setProjectAuthorityAccessor(ProjectAuthorityAccessor projectAuthorityAccessor) {
		this.projectAuthorityAccessor = projectAuthorityAccessor;
	}
}
