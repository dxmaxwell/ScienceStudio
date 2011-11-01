/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *     ProjectAuthzDAO interface.
 *     
 */
package ca.sciencestudio.model.project.dao;

import java.util.List;

import ca.sciencestudio.model.dao.Data;
import ca.sciencestudio.model.dao.ModelAuthzDAO;
import ca.sciencestudio.model.project.Project;

/**
 * @author maxweld
 * 
 */
public interface ProjectAuthzDAO extends ModelAuthzDAO<Project> {
	
	public Data<List<Project>> getAll(String user); 
}
