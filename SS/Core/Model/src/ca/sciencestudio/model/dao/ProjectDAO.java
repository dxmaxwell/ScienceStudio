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

/**
 * @author maxweld
 * 
 */
public interface ProjectDAO extends ModelDAO<Project> {
	
	public List<Project> getAllByStatus(String status);
	public List<Project> getAllByPersonUidAndStatus(String personUid, String status);
}
