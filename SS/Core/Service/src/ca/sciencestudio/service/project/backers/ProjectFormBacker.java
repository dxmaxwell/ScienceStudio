/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *     ProjectFormController class.
 *     
 */
package ca.sciencestudio.service.project.backers;

import java.text.ParseException;
import java.util.Date;

import ca.sciencestudio.model.project.Project;
import ca.sciencestudio.model.project.ProjectStatus;
import ca.sciencestudio.model.project.dao.ProjectDAO;
import ca.sciencestudio.util.web.BindAndValidateUtils;

/**
 * @author maxweld
 *
 */
public class ProjectFormBacker { 
	
	private int id;
	private String name;
	private String description;
	private String startDate;
	private String endDate;
	private ProjectStatus status;
	
	public ProjectFormBacker() {
		setId(0);
		setName("");
		setDescription("");
		setStartDate("");
		setEndDate("");
		setStatus(ProjectStatus.UNKNOWN);
	}
	
	public ProjectFormBacker(Project project) {
		setId(project.getId());
		setName(project.getName());
		setDescription(project.getDescription());
		setRawStartDate(project.getStartDate());
		setRawEndDate(project.getEndDate());
		setStatus(project.getStatus());
	}
	
	public Project createProject(ProjectDAO projectDAO) {
		Project project = projectDAO.createProject();
		project.setId(getId());
		project.setName(getName());
		project.setDescription(getDescription());
		project.setStartDate(getRawStartDate());
		project.setEndDate(getRawEndDate());
		project.setStatus(getStatus());
		return project;
	}
	
	public Date getRawStartDate() {
		try {
			return BindAndValidateUtils.DATE_FORMAT_ISO8601_DATE.parse(getStartDate());
		}
		catch (ParseException e) {
			return null;
		}
	}
	public void setRawStartDate(Date date) {
		setStartDate(BindAndValidateUtils.DATE_FORMAT_ISO8601_DATE.format(date));
	}

	public Date getRawEndDate() {
		try {
			return BindAndValidateUtils.DATE_FORMAT_ISO8601_DATE.parse(getEndDate());
		}
		catch (ParseException e) {
			return null;
		}
	}
	public void setRawEndDate(Date date) {
		setEndDate(BindAndValidateUtils.DATE_FORMAT_ISO8601_DATE.format(date));
	}
	
	public int getId() {
		return id;
	}
	private void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}

	public String getStartDate() {
		return startDate;
	}
	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	public String getEndDate() {
		return endDate;
	}
	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	public ProjectStatus getStatus() {
		return status;
	}
	public void setStatus(ProjectStatus status) {
		this.status = status;
	}
}
