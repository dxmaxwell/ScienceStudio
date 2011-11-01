/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *     SessionPersonGridController class.
 *     
 */
package ca.sciencestudio.service.session.controllers;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import ca.sciencestudio.model.dao.Data;
import ca.sciencestudio.model.person.Person;
import ca.sciencestudio.model.person.dao.PersonAuthzDAO;
import ca.sciencestudio.model.session.SessionPerson;
import ca.sciencestudio.model.session.dao.SessionPersonAuthzDAO;
import ca.sciencestudio.security.util.SecurityUtil;
import ca.sciencestudio.service.session.backers.SessionPersonGridBacker;
import ca.sciencestudio.service.utilities.ModelPathUtils;

/**
 * @author maxweld
 *
 */
@Controller
public class SessionPersonGridController {

	private PersonAuthzDAO personAuthzDAO;
	
	private SessionPersonAuthzDAO sessionPersonAuthzDAO;
	
	@ResponseBody
	@RequestMapping(value = ModelPathUtils.SESSION_PERSON_PATH + "/grid*", params = "session")
	public List<SessionPersonGridBacker> getSessionPersonGridList(@RequestParam("session") String sessionGid) {
		
		String user = SecurityUtil.getPersonGid();
		
		List<PersonContainer> personContainerList = new ArrayList<PersonContainer>();
		
		List<SessionPerson> sessionPersonList = sessionPersonAuthzDAO.getAllBySessionGid(user, sessionGid).get();
		for(SessionPerson sessionPerson : sessionPersonList) {
			Data<Person> dataPerson = personAuthzDAO.get(user, sessionPerson.getPersonGid());
			personContainerList.add(new PersonContainer(sessionPerson, dataPerson));
		}
		
		List<SessionPersonGridBacker> sessionPersonGridBackerList = new ArrayList<SessionPersonGridBacker>();
		
		for(PersonContainer personContainer : personContainerList) {
			if(personContainer.getPerson() != null) {
				Person p = personContainer.getPerson();
				SessionPerson sp = personContainer.getSessionPerson();
				sessionPersonGridBackerList.add(new SessionPersonGridBacker(sp, p));
			}	
		}
		
		return sessionPersonGridBackerList;
	}
	
	private static class PersonContainer  {
		
		private Data<Person> dataPerson;
		private SessionPerson sessionPerson;
		
		public PersonContainer(SessionPerson sessionPerson, Data<Person> dataPerson) {
			this.sessionPerson = sessionPerson;
			this.dataPerson = dataPerson;
		}
		
		public Person getPerson() {
			return dataPerson.get();
		}
		
		public SessionPerson getSessionPerson() {
			return sessionPerson;
		}
	}

	public PersonAuthzDAO getPersonAuthzDAO() {
		return personAuthzDAO;
	}
	public void setPersonAuthzDAO(PersonAuthzDAO personAuthzDAO) {
		this.personAuthzDAO = personAuthzDAO;
	}

	public SessionPersonAuthzDAO getSessionPersonAuthzDAO() {
		return sessionPersonAuthzDAO;
	}
	public void setSessionPersonAuthzDAO(SessionPersonAuthzDAO sessionPersonAuthzDAO) {
		this.sessionPersonAuthzDAO = sessionPersonAuthzDAO;
	}
}
