/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *     ProjectPersonBasicDAO class.
 *     
 */
package ca.sciencestudio.model.project.dao;

import java.util.List;

import ca.sciencestudio.model.project.ProjectPerson;
import ca.sciencestudio.model.dao.ModelBasicDAO;

/**
 * @author maxweld
 *
 */
public interface ProjectPersonBasicDAO extends ModelBasicDAO<ProjectPerson> {

	public List<ProjectPerson> getAllByPersonGid(String personGid);
	public List<ProjectPerson> getAllByProjectGid(String projectGid);
	public List<ProjectPerson> getAllByProjectGidAndPersonGid(String projectGid, Object personGid);
}
