/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *  	LaboratorySessionBacker class.
 *
 */
package ca.sciencestudio.vespers.service.admin.backers;

import ca.sciencestudio.model.project.Project;
import ca.sciencestudio.model.session.Session;
import ca.sciencestudio.service.session.backers.SessionFormBacker;

/**
 * @author maxweld
 *
 */
public class LaboratorySessionBacker extends SessionFormBacker {

	private static final long serialVersionUID = 1L;
	
	private String projectName;
	private String status;
	
	public LaboratorySessionBacker() {
		super();
	}
	
	public LaboratorySessionBacker(Project project, Session session) {
		super(session);
		
		if(!project.getGid().equalsIgnoreCase(getProjectGid())) {
			throw new IllegalArgumentException();
		}
		setProjectName(project.getName());
		setStatus("UNKNOWN");
	}
		
	public String getProjectName() {
		return projectName;
	}
	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}

	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
}
