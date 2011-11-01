/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *     AbstractSessionAuthzController abstract class.
 *     
 */
package ca.sciencestudio.rest.service.controllers;

import ca.sciencestudio.model.Model;

import ca.sciencestudio.model.project.dao.ProjectAuthzDAO;
import ca.sciencestudio.security.authz.accessors.SessionAuthorityAccessor;

/**
 * @author maxweld
 * 
 *
 */
public abstract class AbstractSessionAuthzController<T extends Model> extends AbstractModelAuthzController<T> {

	protected ProjectAuthzDAO projectAuthzDAO;
	
	protected SessionAuthorityAccessor sessionAuthorityAccessor;

	public ProjectAuthzDAO getProjectAuthzDAO() {
		return projectAuthzDAO;
	}
	public void setProjectAuthzDAO(ProjectAuthzDAO projectAuthzDAO) {
		this.projectAuthzDAO = projectAuthzDAO;
	}

	public SessionAuthorityAccessor getSessionAuthorityAccessor() {
		return sessionAuthorityAccessor;
	}
	public void setSessionAuthorityAccessor(SessionAuthorityAccessor sessionAuthorityAccessor) {
		this.sessionAuthorityAccessor = sessionAuthorityAccessor;
	}
}
