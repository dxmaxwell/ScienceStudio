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

import ca.sciencestudio.model.Project;
import ca.sciencestudio.util.web.BindAndValidateUtils;

/**
 * @author maxweld
 *
 */
public class ProjectFormBacker { 
	
	private String gid;
	private String name;
	private String description;
	private String startDate;
	private String endDate;
	private String status;
	
	public ProjectFormBacker() {
		setGid(Project.DEFAULT_GID);
		setName(Project.DEFAULT_NAME);
		setDescription(Project.DEFAULT_DESCRIPTION);
		setRawStartDate(Project.DEFAULT_START_DATE);
		setRawEndDate(Project.DEFAULT_END_DATE);
		setStatus(Project.DEFAULT_STATUS);
	}
	
	public ProjectFormBacker(Project project) {
		setGid(project.getGid());
		setName(project.getName());
		setDescription(project.getDescription());
		setRawStartDate(project.getStartDate());
		setRawEndDate(project.getEndDate());
		setStatus(project.getStatus());
	}
	
	public Project toProject() {
		Project project = new Project();
		project.setGid(getGid());
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
			return Project.DEFAULT_START_DATE;
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
			return Project.DEFAULT_END_DATE;
		}
	}
	public void setRawEndDate(Date date) {
		setEndDate(BindAndValidateUtils.DATE_FORMAT_ISO8601_DATE.format(date));
	}

	public String getGid() {
		return gid;
	}
	public void setGid(String gid) {
		this.gid = gid;
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

	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
}
