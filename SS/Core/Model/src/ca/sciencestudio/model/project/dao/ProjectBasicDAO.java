/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *     ProjectBasicDAO interface.
 *     
 */
package ca.sciencestudio.model.project.dao;

import java.util.List;

import ca.sciencestudio.model.project.Project;
import ca.sciencestudio.model.project.Project.Status;
import ca.sciencestudio.model.dao.ModelBasicDAO;

/**
 * @author maxweld
 * 
 */
public interface ProjectBasicDAO extends ModelBasicDAO<Project> {
	
	//public Project getScanId(int scanId);
	//public Project getSessionId(int sessionId);
	//public Project getExperimentId(int experimentId);
	
	public List<Project> getAllByStatus(Status status);
	public List<Project> getAllByStatus(String status);
	
	public List<Project> getAllByPersonGid(Object personGid);
	
	public List<Project> getAllByPersonGidAndStatus(Object personGid, Status status);
	public List<Project> getAllByPersonGidAndStatus(Object personGid, String status);
}
