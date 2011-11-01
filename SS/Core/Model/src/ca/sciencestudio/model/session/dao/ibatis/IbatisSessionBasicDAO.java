/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *     IbatisSessionBasicDAO class.
 *     
 */
package ca.sciencestudio.model.session.dao.ibatis;

import ca.sciencestudio.model.session.Session;
import ca.sciencestudio.model.facility.Laboratory;
import ca.sciencestudio.model.session.dao.SessionBasicDAO;
import ca.sciencestudio.model.session.dao.ibatis.support.IbatisSession;
import ca.sciencestudio.model.dao.ibatis.AbstractIbatisModelBasicDAO;
import ca.sciencestudio.model.utilities.GID;

/**
 * @author maxweld
 *
 */
public class IbatisSessionBasicDAO extends AbstractIbatisModelBasicDAO<Session, IbatisSession> implements SessionBasicDAO {

	@Override
	public String getGidType() {
		return Session.GID_TYPE;
	}

//	@SuppressWarnings("unchecked")
//	public List<Session> getSessionListByProjectId(int projectId) {
//		List<Session> list = getSqlMapClientTemplate().queryForList("getSessionListByProjectId", projectId);
//		logger.info("Get Session List by Project Id: " + projectId + ", size: " + list.size());
//		return list;
//	}
	
//	@SuppressWarnings("unchecked")
//	public List<Session> getSessionListByLaboratoryId(int laboratoryId) {
//		List<Session> list = getSqlMapClientTemplate().queryForList("getSessionListByLaboratoryId", laboratoryId);
//		logger.info("Get Session List by Laboratory Id: " + laboratoryId + ", size: " + list.size());
//		return list;
//	}

//	@SuppressWarnings("unchecked")
//	public List<Session> getSessionListByPersonUid(String personUid) {
//		List<Session> list = getSqlMapClientTemplate().queryForList("getSessionListByPersonUid", personUid);
//		logger.info("Get Session List by Person uid: " + personUid + ", size: " + list.size());
//		return list;
//	}
	
//	@SuppressWarnings("unchecked")
//	public List<Session> getSessionListByPersonUidAndProjectStatus(String personUid, Project.Status projectStatus) {
//		SqlMapParameters parameters = new SqlMapParameters(personUid, projectStatus.name());
//		List<Session> list = getSqlMapClientTemplate().queryForList("getSessionListByPersonUidAndProjectStatus", parameters);
//		logger.info("Get Session List by Person uid: " + personUid + ", ProjectStatus: " + projectStatus + ", size: " + list.size());
//		return list;
//	}
	
//	@SuppressWarnings("unchecked")
//	public List<Session> getSessionListByLaboratoryNameAndFacilityName(String laboratoryName, String facilityName) {
//		SqlMapParameters parameters = new SqlMapParameters(laboratoryName, facilityName);
//		List<Session> list = getSqlMapClientTemplate().queryForList("getSessionListByLaboratoryNameAndFacilityName", parameters);
//		logger.info("Get Session List by Laboratory name: " + laboratoryName + ", Facility name: " + facilityName + ", size: " + list.size());
//		return list;
//	}
	

	@Override
	protected IbatisSession toIbatisModel(Session session) {
		if(session == null) {
			return null;
		}
		IbatisSession ibatisSession = new IbatisSession();
		GID gid = GID.parse(session.getGid());
		if((gid != null) && gid.isFacilityAndType(getGidFacility(), getGidType(), true, true)) {
			ibatisSession.setId(gid.getId());
		}
		GID laboratoryGid = GID.parse(session.getLaboratoryGid());
		if((laboratoryGid != null) && laboratoryGid.isFacilityAndType(getGidFacility(), Laboratory.GID_TYPE, true, true)) {
			ibatisSession.setLaboratoryId(laboratoryGid.getId());
		}
		ibatisSession.setProjectGid(session.getProjectGid());
		ibatisSession.setName(session.getName());
		ibatisSession.setDescription(session.getDescription());
		ibatisSession.setProposal(session.getProposal());
		ibatisSession.setStartDate(session.getStartDate());
		ibatisSession.setEndDate(session.getEndDate());
		return ibatisSession;
	}
	
	@Override
	protected Session toModel(IbatisSession ibatisSession) {
		if(ibatisSession == null) {
			return null;
		}
		Session session = new Session();
		session.setGid(GID.format(getGidFacility(), ibatisSession.getId(), getGidType()));
		session.setLaboratoryGid(GID.format(getGidFacility(), ibatisSession.getLaboratoryId(), Laboratory.GID_TYPE));		
		session.setProjectGid(ibatisSession.getProjectGid());
		session.setName(ibatisSession.getName());
		session.setDescription(ibatisSession.getDescription());
		session.setProposal(ibatisSession.getProposal());
		session.setStartDate(ibatisSession.getStartDate());
		session.setEndDate(ibatisSession.getEndDate());
		return session;
	}

	@Override
	protected String getStatementName(String prefix, String suffix) {
		return prefix + "Session" + suffix;
	}
}
