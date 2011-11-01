/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *     SessionBasicDAO interface.
 *     
 */
package ca.sciencestudio.model.session.dao;

import ca.sciencestudio.model.dao.ModelBasicDAO;
import ca.sciencestudio.model.session.Session;

/**
 * @author maxweld
 *
 */
public interface SessionBasicDAO extends ModelBasicDAO<Session> {
	
	//public List<Session> getSessionListByProjectId(int projectId);
	//public List<Session> getSessionListByLaboratoryId(int laboratoryId);
	//public List<Session> getSessionListByPersonUid(String personUid);
	//public List<Session> getSessionListByPersonUidAndProjectStatus(String personUid, Project.Status projectStatus);
	//public List<Session> getSessionListByLaboratoryNameAndFacilityName(String laboratoryName, String facilityName);
}
