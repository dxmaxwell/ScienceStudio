/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *     ProjectPersonController class.
 *     
 */
package ca.sciencestudio.service.project.controllers;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import ca.sciencestudio.model.person.Person;
import ca.sciencestudio.model.person.dao.PersonDAO;
import ca.sciencestudio.model.project.ProjectPerson;
import ca.sciencestudio.model.project.dao.ProjectPersonDAO;
import ca.sciencestudio.security.util.AuthorityUtil;
import ca.sciencestudio.security.util.SecurityUtil;
import ca.sciencestudio.service.project.backers.ProjectPersonGridBacker;

/**
 * @author maxweld
 *
 */
@Controller
public class ProjectPersonGridController {

	@Autowired
	private PersonDAO personDAO;
	
	@Autowired
	private ProjectPersonDAO projectPersonDAO;
	
	@RequestMapping(value = "/project/{projectId}/persons/grid.{format}", method = RequestMethod.GET)
	public String getProjectPersonGridList(@PathVariable int projectId, @PathVariable String format, ModelMap model) {
		
		List<ProjectPersonGridBacker> projectPersonGridBackerList = new ArrayList<ProjectPersonGridBacker>();
		
		Object admin = AuthorityUtil.ROLE_ADMIN_PROJECTS;
		Object team = AuthorityUtil.buildProjectGroupAuthority(projectId);
		if(SecurityUtil.hasAnyAuthority(admin,team)) {
			
			List<ProjectPerson> projectPersonList = projectPersonDAO.getProjectPersonListByProjectId(projectId);
			for(ProjectPerson projectPerson : projectPersonList) {
				
				Person person = personDAO.getPersonByUid(projectPerson.getPersonUid());
				if(person != null) {			
					projectPersonGridBackerList.add(new ProjectPersonGridBacker(projectPerson, person));
				}
			}
		}
		
		model.put("response", projectPersonGridBackerList);
		return "response-" + format;
	}
	
	public PersonDAO getPersonDAO() {
		return personDAO;
	}
	public void setPersonDAO(PersonDAO personDAO) {
		this.personDAO = personDAO;
	}

	public ProjectPersonDAO getProjectPersonDAO() {
		return projectPersonDAO;
	}
	public void setProjectPersonDAO(ProjectPersonDAO projectPersonDAO) {
		this.projectPersonDAO = projectPersonDAO;
	}
}
