package ca.sciencestudio.rest.service.test;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.springframework.validation.Errors;

import ca.sciencestudio.model.project.Project;
import ca.sciencestudio.util.exceptions.ModelAccessException;
import ca.sciencestudio.model.project.dao.ProjectAuthzDAO;
import ca.sciencestudio.model.project.dao.ProjectBasicDAO;
import ca.sciencestudio.model.project.dao.delegating.DelegatingProjectAuthzDAO;
import ca.sciencestudio.model.project.dao.rest.RestProjectAuthzDAO;
import ca.sciencestudio.model.project.validators.ProjectValidator;

public abstract class ProjectDaoTest extends ModelDaoTest {
	
	private static final DateFormat DATE_FORMATER = new SimpleDateFormat("yyyy-MM-dd"); 
	
	public static void main(String[] args) throws Exception {
		
		ProjectValidator validator = new ProjectValidator();
		
		List<ProjectAuthzDAO> projectDAOs = new ArrayList<ProjectAuthzDAO>();
		
		RestProjectAuthzDAO localRestProjectDAO = new RestProjectAuthzDAO();
		localRestProjectDAO.setRestTemplate(REST_TEMPLATE);
		localRestProjectDAO.setBaseUrl("http://localhost:8080/ssrest/model");
		projectDAOs.add(localRestProjectDAO);
		
		RestProjectAuthzDAO remoteRestProjectDAO = new RestProjectAuthzDAO();
		remoteRestProjectDAO.setRestTemplate(REST_TEMPLATE);
		remoteRestProjectDAO.setBaseUrl("http://remotehost:8080/ssrest/model");
		projectDAOs.add(remoteRestProjectDAO);
		
		
		DelegatingProjectAuthzDAO delProjectDAO = new DelegatingProjectAuthzDAO();
		delProjectDAO.setProjectDAOs(projectDAOs);
		
		ProjectBasicDAO projectDAO = (ProjectBasicDAO)delProjectDAO;
		
		List<Project> projects = projectDAO.getAll();
		for(Project p : projects) {
			System.out.println(p);
		}
		System.out.println();
		
		Project project = new Project();
		project.setName("Rest Test Project Rest Test Project Rest Test Project Rest Test Project Rest Test Project Rest Test Project Rest Test Project Rest Test Project Rest Test Project Rest Test Project Rest Test Project Rest Test Project Rest Test Project Rest Test Project Rest Test Project Rest Test Project");
		project.setDescription("Rest Test Project Description!!");
		project.setStartDate(DATE_FORMATER.parse("2011-03-25"));
		project.setEndDate(DATE_FORMATER.parse("2011-04-14"));
		project.setStatus(Project.Status.ACTIVE.name());
		
		boolean success = projectDAO.add(project);
		if(!success) {
			System.out.println("Errors while adding Project");
			return;
		}
		
		System.out.println("Add Project: " + project);
		
		Project project2 = projectDAO.get(project.getGid());
		if(!project.equals(project2)) {
			System.out.println("Error while getting Project with GID: " + project.getGid());
			return;
		}
		
		System.out.println("Get Project: " + project);
		
		project.setName("Rest Test Project (Updated)");
		project.setDescription("Rest Test Project Description!! (Updated)");
		project.setStartDate(DATE_FORMATER.parse("2011-05-22"));
		project.setEndDate(DATE_FORMATER.parse("2011-06-10"));
		project.setStatus(Project.Status.INACTIVE.name());
		
		success = projectDAO.edit(project);
		if(!success) {
			System.out.println("Errors while editting Project");
			return;
		}
		
		System.out.println("Edit Project: " + project);
		
		project2 = projectDAO.get(project.getGid());
		if(!project.equals(project2)) {
			System.out.println("Error while getting Project with GID: " + project.getGid());
			return;
		}
		
		System.out.println("Get Project (again): " + project);
		
		success = projectDAO.remove(project.getGid());
		if(!success) {
			System.out.println("Error while removing Project with GID: " + project.getGid());
			return;
		}
		System.out.println();
		
		projects = projectDAO.getAll();
		for(Project p : projects) {
			System.out.println(p);
		}
	}

}
