/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *     IbatisSessionDAO class.
 *     
 */
package ca.sciencestudio.model.session.dao.ibatis;

import java.util.List;

import org.springframework.orm.ibatis.support.SqlMapClientDaoSupport;

import ca.sciencestudio.util.sql.SqlMapParameters;
import ca.sciencestudio.model.project.ProjectStatus;
import ca.sciencestudio.model.session.Session;
import ca.sciencestudio.model.session.dao.SessionDAO;
import ca.sciencestudio.model.session.ibatis.IbatisSession;

/**
 * @author medrand
 *
 */
public class IbatisSessionDAO extends SqlMapClientDaoSupport implements SessionDAO {

	public Session createSession() {
		return new IbatisSession();
	}
	
	public int addSession(Session session) {
		int key = (Integer) getSqlMapClientTemplate().insert("addSession", session);
		logger.info("Added new session with key: " + key);
		return key;
	}

	public void editSession(Session session) {
		int count = getSqlMapClientTemplate().update("editSession", session);
		logger.info("Rows affected: " + count);
	}
	
	public void removeSession(int sessionId) {
		int count = getSqlMapClientTemplate().delete("removeSession", sessionId);
		logger.info("Rows affected: " + count);
	}

	public Session getSessionById(int sessionId) {
		Session session = (Session) getSqlMapClientTemplate().queryForObject("getSessionById", sessionId);
		return session;
	}

	@SuppressWarnings("unchecked")
	public List<Session> getSessionList() {
		List<Session> list = getSqlMapClientTemplate().queryForList("getSessionList");
		return list;
	}

	@SuppressWarnings("unchecked")
	public List<Session> getSessionListByProjectId(int projectId) {
		List<Session> list = getSqlMapClientTemplate().queryForList("getSessionListByProjectId", projectId);
		logger.info("Get Session List by Project Id: " + projectId + ", size: " + list.size());
		return list;
	}
	
	@SuppressWarnings("unchecked")
	public List<Session> getSessionListByLaboratoryId(int laboratoryId) {
		List<Session> list = getSqlMapClientTemplate().queryForList("getSessionListByLaboratoryId", laboratoryId);
		logger.info("Get Session List by Laboratory Id: " + laboratoryId + ", size: " + list.size());
		return list;
	}

	@SuppressWarnings("unchecked")
	public List<Session> getSessionListByPersonUid(String personUid) {
		List<Session> list = getSqlMapClientTemplate().queryForList("getSessionListByPersonUid", personUid);
		logger.info("Get Session List by Person uid: " + personUid + ", size: " + list.size());
		return list;
	}
	
	@SuppressWarnings("unchecked")
	public List<Session> getSessionListByPersonUidAndProjectStatus(String personUid, ProjectStatus projectStatus) {
		SqlMapParameters parameters = new SqlMapParameters(personUid, projectStatus.name());
		List<Session> list = getSqlMapClientTemplate().queryForList("getSessionListByPersonUidAndProjectStatus", parameters);
		logger.info("Get Session List by Person uid: " + personUid + ", ProjectStatus: " + projectStatus + ", size: " + list.size());
		return list;
	}
	
	@SuppressWarnings("unchecked")
	public List<Session> getSessionListByLaboratoryNameAndFacilityName(String laboratoryName, String facilityName) {
		SqlMapParameters parameters = new SqlMapParameters(laboratoryName, facilityName);
		List<Session> list = getSqlMapClientTemplate().queryForList("getSessionListByLaboratoryNameAndFacilityName", parameters);
		logger.info("Get Session List by Laboratory name: " + laboratoryName + ", Facility name: " + facilityName + ", size: " + list.size());
		return list;
	}
}
