/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *     ProjectPersonGridController class.
 *     
 */
package ca.sciencestudio.service.project.controllers;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import ca.sciencestudio.model.dao.Data;
import ca.sciencestudio.model.person.Person;
import ca.sciencestudio.model.person.dao.PersonAuthzDAO;
import ca.sciencestudio.model.project.ProjectPerson;
import ca.sciencestudio.model.project.dao.ProjectPersonAuthzDAO;
import ca.sciencestudio.security.util.SecurityUtil;
import ca.sciencestudio.service.project.backers.ProjectPersonGridBacker;
import ca.sciencestudio.service.utilities.ModelPathUtils;

/**
 * @author maxweld
 *
 */
@Controller
public class ProjectPersonGridController {

	private PersonAuthzDAO personAuthzDAO;
	
	private ProjectPersonAuthzDAO projectPersonAuthzDAO;
	
	@ResponseBody
	@RequestMapping(value = ModelPathUtils.PROJECT_PERSON_PATH + "/grid*")
	public List<ProjectPersonGridBacker> getProjectPersonGridList(@RequestParam("project") String projectGid) {
		
		String user = SecurityUtil.getPersonGid();
		
		List<PersonContainer> personContainerList = new ArrayList<PersonContainer>();
		
		List<ProjectPerson> projectPersonList = projectPersonAuthzDAO.getAllByProjectGid(user, projectGid).get();
		for(ProjectPerson projectPerson : projectPersonList) {
			Data<Person> dataPerson = personAuthzDAO.get(user, projectPerson.getPersonGid());
			personContainerList.add(new PersonContainer(projectPerson, dataPerson));
		}
		
		List<ProjectPersonGridBacker> projectPersonGridBackerList = new ArrayList<ProjectPersonGridBacker>();
		
		for(PersonContainer personContainer : personContainerList) {
			if(personContainer.getPerson() != null) {
				Person p = personContainer.getPerson();
				ProjectPerson pp = personContainer.getProjectPerson();
				projectPersonGridBackerList.add(new ProjectPersonGridBacker(pp, p));
			}	
		}
		
		return projectPersonGridBackerList;
	}

	
	private static class PersonContainer  {
		
		private Data<Person> dataPerson;
		private ProjectPerson projectPerson;
		
		public PersonContainer(ProjectPerson projectPerson, Data<Person> dataPerson) {
			this.projectPerson = projectPerson;
			this.dataPerson = dataPerson;
		}
		
		public Person getPerson() {
			return dataPerson.get();
		}
		
		public ProjectPerson getProjectPerson() {
			return projectPerson;
		}
	}
	
	
	public PersonAuthzDAO getPersonAuthzDAO() {
		return personAuthzDAO;
	}
	public void setPersonAuthzDAO(PersonAuthzDAO personAuthzDAO) {
		this.personAuthzDAO = personAuthzDAO;
	}

	public ProjectPersonAuthzDAO getProjectPersonAuthzDAO() {
		return projectPersonAuthzDAO;
	}
	public void setProjectPersonAuthzDAO(ProjectPersonAuthzDAO projectPersonAuthzDAO) {
		this.projectPersonAuthzDAO = projectPersonAuthzDAO;
	}
}
