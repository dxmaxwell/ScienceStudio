/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *     ProjectPersonGridBacker class.
 *     
 */
package ca.sciencestudio.service.project.backers;

import ca.sciencestudio.model.person.Person;
import ca.sciencestudio.model.project.ProjectPerson;

/**
 * @author maxweld
 *
 */
public class ProjectPersonGridBacker extends ProjectPersonFormBacker {

	private static final long serialVersionUID = 1L;

//	public ProjectPersonGridBacker(int projectId, ProjectRole projectRole, Person person) {
//		super(projectId, projectRole, person);
//	}

//	public ProjectPersonGridBacker(int projectId, ProjectRole projectRole) {
//		super(projectId, projectRole);
//	}

	public ProjectPersonGridBacker(ProjectPerson projectPerson, Person person) {
		super(projectPerson, person);
	}
}
