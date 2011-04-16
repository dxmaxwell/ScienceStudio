/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *     Project interface.
 *     
 */
package ca.sciencestudio.model.project;

import java.io.Serializable;
import java.util.Date;

import ca.sciencestudio.model.project.ProjectStatus;

/**
 * @author maxweld
 *
 */
public interface Project extends Cloneable, Serializable {
	
	public int getId();
	public void setId(int id);
	
	public String getName();
	public void setName(String name);
	
	public String getDescription();
	public void setDescription(String description);
	
	public Date getStartDate();
	public void setStartDate(Date startDate);
	
	public Date getEndDate();
	public void setEndDate(Date endDate);
	
	public ProjectStatus getStatus();
	public void setStatus(ProjectStatus status);
	
	public Project clone();
}
