/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *  	SessionGridBacker class.
 *
 */
package ca.sciencestudio.service.session.backers;

import ca.sciencestudio.model.project.Project;
import ca.sciencestudio.model.session.Session;

/**
 * @author maxweld
 *
 */
public class SessionGridBacker extends Session {

	private static final long serialVersionUID = 1L;
	
	private String projectName;	
		
	public SessionGridBacker(Project project, Session session) {
		super(session);
		
		if(!getProjectGid().equals(project.getGid())) { 
			throw new IllegalArgumentException();
		}
		
		setProjectName(project.getName());
	}
	
	public String getProjectName() {
		return projectName;
	}
	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}
}
