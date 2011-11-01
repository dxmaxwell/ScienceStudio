/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *     ProjectPersonAuthzDAO class.
 *     
 */
package ca.sciencestudio.model.project.dao;

import java.util.List;

import ca.sciencestudio.model.project.ProjectPerson;
import ca.sciencestudio.model.dao.Data;
import ca.sciencestudio.model.dao.ModelAuthzDAO;

/**
 * @author maxweld
 *
 */
public interface ProjectPersonAuthzDAO extends ModelAuthzDAO<ProjectPerson> {

	public Data<List<ProjectPerson>> getAllByPersonGid(String user, String personGid);
	public Data<List<ProjectPerson>> getAllByProjectGid(String user, String projectGid);
}
