package ca.sciencestudio.rest.service.test;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;

import org.springframework.validation.Errors;

import ca.sciencestudio.model.Project;
import ca.sciencestudio.model.dao.ProjectDAO;
import ca.sciencestudio.model.dao.rest.RestProjectDAO;
import ca.sciencestudio.model.validators.ProjectValidator;

public abstract class ProjectDaoTest extends ModelDaoTest {
	
	private static final DateFormat DATE_FORMATER = new SimpleDateFormat("yyyy-MM-dd"); 
	
	public static void main(String[] args) throws Exception {
		
		RestProjectDAO restProjectDAO = new RestProjectDAO();
		restProjectDAO.setValidator(new ProjectValidator());
		restProjectDAO.setRestTemplate(REST_TEMPLATE);
		restProjectDAO.setBaseUrl(BASE_URL);
		
		ProjectDAO projectDAO = (ProjectDAO)restProjectDAO;
		
		List<Project> projects = projectDAO.getAll();
		for(Project p : projects) {
			System.out.println(p);
		}
		System.out.println();
		
		Project project = new Project();
		project.setName("Rest Test Project");
		project.setDescription("Rest Test Project Description!!");
		project.setStartDate(DATE_FORMATER.parse("2011-03-25"));
		project.setEndDate(DATE_FORMATER.parse("2011-04-14"));
		project.setStatus(Project.Status.ACTIVE.name());
		Errors errors = projectDAO.add(project);
	
		if(errors.hasErrors()) {
			System.out.println("Errors while adding Project: " + errors);
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
		errors = projectDAO.edit(project);
		
		if(errors.hasErrors()) {
			System.out.println("Errors while editting Project: " + errors);
			return;
		}
		
		System.out.println("Edit Project: " + project);
		
		project2 = projectDAO.get(project.getGid());
		if(!project.equals(project2)) {
			System.out.println("Error while getting Project with GID: " + project.getGid());
			return;
		}
		
		System.out.println("Get Project (again): " + project);
		
		boolean success = projectDAO.remove(project.getGid());
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
