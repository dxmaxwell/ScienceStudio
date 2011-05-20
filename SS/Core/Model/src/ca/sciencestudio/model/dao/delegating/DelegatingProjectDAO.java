/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *     DelegatingProjectDAO abstract class.
 *     
 */
package ca.sciencestudio.model.dao.delegating;

import java.util.Collection;

import ca.sciencestudio.model.Project;
import ca.sciencestudio.model.dao.ProjectDAO;
import ca.sciencestudio.model.dao.delegating.support.AbstractDelegratingModelDAO;

/**
 * @author maxweld
 *
 *
 */
public class DelegatingProjectDAO extends AbstractDelegratingModelDAO<Project, ProjectDAO> implements ProjectDAO {

	public Collection<ProjectDAO> getProjectDAOs() {
		return getModelDAOs();
	}
	public void setProjectDAOs(Collection<ProjectDAO> projectDAOs) {
		setModelDAOs(projectDAOs);
	}
}
