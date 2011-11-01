/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *     AbstractSessionAuthzController abstract class.
 *     
 */
package ca.sciencestudio.rest.service.controllers;

import java.util.List;

import ca.sciencestudio.model.Model;

import ca.sciencestudio.model.project.ProjectPerson;
import ca.sciencestudio.model.project.dao.ProjectPersonAuthzDAO;
import ca.sciencestudio.model.session.SessionPerson;
import ca.sciencestudio.model.session.dao.SessionBasicDAO;
import ca.sciencestudio.model.session.dao.SessionPersonBasicDAO;

/**
 * @author maxweld
 * 
 *
 */
public abstract class AbstractSessionAuthzController<T extends Model> extends AbstractModelAuthzController<T> {

	private SessionBasicDAO sessionBasicDAO;
	private SessionPersonBasicDAO sessionPersonBasicDAO;
	private ProjectPersonAuthzDAO projectPersonAuthzDAO;

	protected List<ProjectPerson> getProjectPersons(String user, String personGid) {
		return projectPersonAuthzDAO.getAllByPersonGid(user, personGid).get();
	}
	
	protected boolean isProjectMember(String user, String personGid, String projectGid) {
		List<ProjectPerson> projectPersons = getProjectPersons(user, personGid);
		for(ProjectPerson projectPerson : projectPersons) {
			if(projectPerson.getProjectGid().equals(projectGid)) {
				return true;
			}
			
		}
		return false;
	}
	
	protected boolean hasProjectRole(String user, String personGid, String projectGid, ProjectPerson.Role projectRole) {
		return hasProjecctRole(user, personGid, projectGid, projectRole.name());
	}
	
	protected boolean hasProjecctRole(String user, String personGid, String projectGid, String projectRole) {
		List<ProjectPerson> projectPersons = getProjectPersons(user, personGid);
		for(ProjectPerson projectPerson : projectPersons) {
			if(projectPerson.getProjectGid().equals(projectGid) && projectPerson.getRole().equals(projectRole)) {
				return true;
			}
		}
		return false;
	}
	
	protected List<SessionPerson> getSessionPersons(String personGid) {
		return sessionPersonBasicDAO.getAllByPersonGid(personGid);
	}
	
	protected boolean isSessionMember(String personGid, String sessionGid) {
		List<SessionPerson> sessionPersons = getSessionPersons(personGid);
		for(SessionPerson sessionPerson : sessionPersons) {
			if(sessionPerson.getSessionGid().equals(sessionGid)) {
				return true;
			}
		}
		return false;
	}
	
	protected boolean hasSessionRole(String personGid, String sessionGid, SessionPerson.Role sessionRole) {
		return hasSessionRole(personGid, sessionGid, sessionRole.name());
	}
	
	protected boolean hasSessionRole(String personGid, String sessionGid, String sessionRole) {
		List<SessionPerson> sessionPersons = getSessionPersons(personGid);
		for(SessionPerson sessionPerson : sessionPersons) {
			if(sessionPerson.getSessionGid().equals(sessionGid) && sessionPerson.getRole().equals(sessionRole)) {
				return true;
			}
		}
		return false;
	}

	public SessionBasicDAO getSessionBasicDAO() {
		return sessionBasicDAO;
	}
	public void setSessionBasicDAO(SessionBasicDAO sessionBasicDAO) {
		this.sessionBasicDAO = sessionBasicDAO;
	}

	public SessionPersonBasicDAO getSessionPersonBasicDAO() {
		return sessionPersonBasicDAO;
	}
	public void setSessionPersonBasicDAO(SessionPersonBasicDAO sessionPersonBasicDAO) {
		this.sessionPersonBasicDAO = sessionPersonBasicDAO;
	}

	public ProjectPersonAuthzDAO getProjectPersonAuthzDAO() {
		return projectPersonAuthzDAO;
	}
	public void setProjectPersonAuthzDAO(ProjectPersonAuthzDAO projectPersonAuthzDAO) {
		this.projectPersonAuthzDAO = projectPersonAuthzDAO;
	}
}
