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
import ca.sciencestudio.model.dao.ModelAuthzDAO;

/**
 * @author maxweld
 *
 */
public interface ProjectPersonAuthzDAO extends ModelAuthzDAO<ProjectPerson> {

	//public List<ProjectPerson> getAllByPersonGid(String personGid, Object personGid);
	//public List<ProjectPerson> getAllByProjectGid(String personGid, Object projectGid);
	//public List<ProjectPerson> getAllByProjectGidAndPersonGid(String personGid, Object projectGid, Object personGid);
}
