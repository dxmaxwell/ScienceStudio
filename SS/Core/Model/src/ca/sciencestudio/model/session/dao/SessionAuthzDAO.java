/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *     SessionAuthzDAO interface.
 *     
 */
package ca.sciencestudio.model.session.dao;

import java.util.List;

import ca.sciencestudio.model.dao.Data;
import ca.sciencestudio.model.dao.ModelAuthzDAO;
import ca.sciencestudio.model.session.Session;

/**
 * @author maxweld
 *
 */
public interface SessionAuthzDAO extends ModelAuthzDAO<Session> {
	
	public Data<List<Session>> getAll(String user);
	public Data<List<Session>> getAllByProjectGid(String user, String projectGid);
	//public List<Session> getSessionListByLaboratoryId(int laboratoryId);
	//public List<Session> getSessionListByPersonUid(String personUid);
	//public List<Session> getSessionListByPersonUidAndProjectStatus(String personUid, Project.Status projectStatus);
	//public List<Session> getSessionListByLaboratoryNameAndFacilityName(String laboratoryName, String facilityName);
}
