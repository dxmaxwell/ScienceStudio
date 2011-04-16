/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *     SessionDAO interface.
 *     
 */
package ca.sciencestudio.model.session.dao;

import java.util.List;

import ca.sciencestudio.model.project.ProjectStatus;
import ca.sciencestudio.model.session.Session;

/**
 * @author maxweld
 *
 */
public interface SessionDAO {
	public Session createSession();
	
	public int addSession(Session session);
	public void editSession(Session session);
	public void removeSession(int sessionId);
	
	public Session getSessionById(int sessionId);
	
	public List<Session> getSessionList();
	public List<Session> getSessionListByProjectId(int projectId);
	public List<Session> getSessionListByLaboratoryId(int laboratoryId);
	public List<Session> getSessionListByPersonUid(String personUid);
	public List<Session> getSessionListByPersonUidAndProjectStatus(String personUid, ProjectStatus projectStatus);
	public List<Session> getSessionListByLaboratoryNameAndFacilityName(String laboratoryName, String facilityName);
}
