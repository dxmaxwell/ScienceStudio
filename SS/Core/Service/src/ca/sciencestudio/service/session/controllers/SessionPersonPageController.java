/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *     SessionPersonPageController class.
 *     
 */
package ca.sciencestudio.service.session.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import ca.sciencestudio.model.dao.Data;
import ca.sciencestudio.model.person.Person;
import ca.sciencestudio.model.person.dao.PersonAuthzDAO;
import ca.sciencestudio.model.project.Project;
import ca.sciencestudio.model.project.dao.ProjectAuthzDAO;
import ca.sciencestudio.model.session.Session;
import ca.sciencestudio.model.session.SessionPerson;
import ca.sciencestudio.model.session.dao.SessionAuthzDAO;
import ca.sciencestudio.model.session.dao.SessionPersonAuthzDAO;
import ca.sciencestudio.security.util.SecurityUtil;
import ca.sciencestudio.service.controllers.AbstractModelController;
import ca.sciencestudio.service.session.backers.SessionPersonFormBacker;
import ca.sciencestudio.service.utilities.ModelPathUtils;
import ca.sciencestudio.util.authz.Authorities;
import ca.sciencestudio.util.web.EnumToOptionUtils;

/**
 * @author maxweld
 *
 */
@Controller
public class SessionPersonPageController extends AbstractModelController {
		
	private PersonAuthzDAO personAuthzDAO;

	private ProjectAuthzDAO projectAuthzDAO;
	
	private SessionAuthzDAO sessionAuthzDAO;

	private SessionPersonAuthzDAO sessionPersonAuthzDAO;
	
	@RequestMapping(value = ModelPathUtils.SESSION_PERSON_PATH + ".html", params = "session")
	public String sessionPersonsPage(@RequestParam("session") String sessionGid, ModelMap model) throws Exception {
		
		String user = SecurityUtil.getPersonGid();
		
		Data<Authorities> sessionAuthoritiesData = sessionAuthzDAO.getAuthorities(user, sessionGid);
				
		Session session = sessionAuthzDAO.get(user, sessionGid).get();
		if(session == null) {
			model.put("error", "Session not found.");
			return ERROR_VIEW;
		}
		
		Data<Authorities> projectAuthoritiesData = projectAuthzDAO.getAuthorities(user, session.getProjectGid());
		
		Project project = projectAuthzDAO.get(user, session.getProjectGid()).get();
		if(project == null) {
			model.put("error", "Project not found.");
			return ERROR_VIEW;
		}
		
		Authorities authorities = mergeAuthorities(projectAuthoritiesData.get(), sessionAuthoritiesData.get());
				
		model.put("sessionRoleOptions", EnumToOptionUtils.toList(SessionPerson.Role.values()));
		model.put("authorities", authorities);
		model.put("project", project);
		model.put("session", session);
		return "frag/sessionPersons";
	}
	
	@RequestMapping(value = ModelPathUtils.SESSION_PERSON_PATH + "/{sessionPersonGid}.html")
	public String sessionPersonPage(@PathVariable String sessionPersonGid, ModelMap model) throws Exception {
		
		String user = SecurityUtil.getPersonGid();
		
		SessionPerson sessionPerson = sessionPersonAuthzDAO.get(user, sessionPersonGid).get();
		if(sessionPerson == null) {
			model.put("error", "Session person not found.");
			return ERROR_VIEW;
		}
		
		Data<Person> dataPerson = personAuthzDAO.get(user, sessionPerson.getPersonGid());
		
		Data<Authorities> sessionAuthoritiesData = sessionAuthzDAO.getAuthorities(user, sessionPerson.getSessionGid());
		
		Session session = sessionAuthzDAO.get(user, sessionPerson.getSessionGid()).get();
		if(session == null) {
			model.put("error", "Session not found.");
			return ERROR_VIEW;
		}
		
		Data<Authorities> projectAuthoritiesData = projectAuthzDAO.getAuthorities(user, session.getProjectGid());
		
		Project project = projectAuthzDAO.get(user, session.getProjectGid()).get();
		if(project == null) {
			model.put("error", "Project not found.");
			return ERROR_VIEW;
		}
		
		Person person = dataPerson.get();
		if(person == null) {
			model.put("error", "Person not found.");
			return ERROR_VIEW;
		}
		
		
		Authorities authorities = mergeAuthorities(projectAuthoritiesData.get(), sessionAuthoritiesData.get());
				
		model.put("sessionPerson", new SessionPersonFormBacker(sessionPerson, person));
		model.put("sessionRoleOptions", EnumToOptionUtils.toList(SessionPerson.Role.values()));
		model.put("authorities", authorities);
		model.put("project", project);
		model.put("session", session);
		return "frag/sessionPerson";
	}

	public PersonAuthzDAO getPersonAuthzDAO() {
		return personAuthzDAO;
	}
	public void setPersonAuthzDAO(PersonAuthzDAO personAuthzDAO) {
		this.personAuthzDAO = personAuthzDAO;
	}

	public ProjectAuthzDAO getProjectAuthzDAO() {
		return projectAuthzDAO;
	}
	public void setProjectAuthzDAO(ProjectAuthzDAO projectAuthzDAO) {
		this.projectAuthzDAO = projectAuthzDAO;
	}

	public SessionAuthzDAO getSessionAuthzDAO() {
		return sessionAuthzDAO;
	}
	public void setSessionAuthzDAO(SessionAuthzDAO sessionAuthzDAO) {
		this.sessionAuthzDAO = sessionAuthzDAO;
	}

	public SessionPersonAuthzDAO getSessionPersonAuthzDAO() {
		return sessionPersonAuthzDAO;
	}
	public void setSessionPersonAuthzDAO(SessionPersonAuthzDAO sessionPersonAuthzDAO) {
		this.sessionPersonAuthzDAO = sessionPersonAuthzDAO;
	}
}
