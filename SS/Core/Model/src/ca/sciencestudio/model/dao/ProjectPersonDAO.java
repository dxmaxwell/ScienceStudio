/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *     ProjectPersonDAO class.
 *     
 */
package ca.sciencestudio.model.dao;

import java.util.List;

import ca.sciencestudio.model.ProjectPerson;

/**
 * @author maxweld
 *
 */
public interface ProjectPersonDAO extends ModelDAO<ProjectPerson> {

	public List<ProjectPerson> getAllByPersonGid(Object personGid);
	public List<ProjectPerson> getAllByProjectGid(Object projectGid);
	public List<ProjectPerson> getAllByProjectGidAndPersonGid(Object projectGid, Object personGid);
}
