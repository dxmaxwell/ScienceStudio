/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *     ProjectDAO interface.
 *     
 */
package ca.sciencestudio.model.dao;

import java.util.List;

import ca.sciencestudio.model.Project;
import ca.sciencestudio.model.Project.Status;


/**
 * @author maxweld
 * 
 */
public interface ProjectDAO extends ModelDAO<Project> {
	
	public List<Project> getAllByStatus(Status status);
	public List<Project> getAllByStatus(String status);
	
	public List<Project> getAllByPersonGid(Object personGid);
	
	public List<Project> getAllByPersonGidAndStatus(Object personGid, Status status);
	public List<Project> getAllByPersonGidAndStatus(Object personGid, String status);
}
