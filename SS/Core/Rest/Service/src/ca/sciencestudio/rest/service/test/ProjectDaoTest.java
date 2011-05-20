package ca.sciencestudio.rest.service.test;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.springframework.validation.Errors;

import ca.sciencestudio.model.Project;
import ca.sciencestudio.model.dao.ProjectDAO;
import ca.sciencestudio.model.dao.delegating.DelegatingProjectDAO;
import ca.sciencestudio.model.dao.rest.RestProjectDAO;
import ca.sciencestudio.model.dao.support.ModelAccessException;
import ca.sciencestudio.model.validators.ProjectValidator;

public abstract class ProjectDaoTest extends ModelDaoTest {
	
	private static final DateFormat DATE_FORMATER = new SimpleDateFormat("yyyy-MM-dd"); 
	
	public static void main(String[] args) throws Exception {
		
		ProjectValidator validator = new ProjectValidator();
		
		List<ProjectDAO> projectDAOs = new ArrayList<ProjectDAO>();
		
		RestProjectDAO localRestProjectDAO = new RestProjectDAO();
		localRestProjectDAO.setRestTemplate(REST_TEMPLATE);
		localRestProjectDAO.setBaseUrl("http://localhost:8080/ssrest/model");
		projectDAOs.add(localRestProjectDAO);
		
		RestProjectDAO remoteRestProjectDAO = new RestProjectDAO();
		remoteRestProjectDAO.setRestTemplate(REST_TEMPLATE);
		remoteRestProjectDAO.setBaseUrl("http://remotehost:8080/ssrest/model");
		projectDAOs.add(remoteRestProjectDAO);
		
		
		DelegatingProjectDAO delProjectDAO = new DelegatingProjectDAO();
		delProjectDAO.setProjectDAOs(projectDAOs);
		
		ProjectDAO projectDAO = (ProjectDAO)delProjectDAO;
		
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
