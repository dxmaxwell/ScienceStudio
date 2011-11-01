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
import ca.sciencestudio.model.dao.ModelBasicDAO;

/**
 * @author maxweld
 * 
 */
public interface ProjectBasicDAO extends ModelBasicDAO<Project> {
	
	public List<Project> getAllByPersonGid(String personGid);
}
