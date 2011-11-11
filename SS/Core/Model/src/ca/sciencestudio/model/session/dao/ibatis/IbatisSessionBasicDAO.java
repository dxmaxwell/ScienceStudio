/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *     IbatisSessionBasicDAO class.
 *     
 */
package ca.sciencestudio.model.session.dao.ibatis;

import java.util.Collections;
import java.util.List;

import org.springframework.dao.DataAccessException;

import ca.sciencestudio.model.session.Experiment;
import ca.sciencestudio.model.session.Scan;
import ca.sciencestudio.model.session.Session;
import ca.sciencestudio.model.facility.Laboratory;
import ca.sciencestudio.model.session.dao.SessionBasicDAO;
import ca.sciencestudio.model.session.dao.ibatis.support.IbatisSession;
import ca.sciencestudio.model.dao.ibatis.AbstractIbatisModelBasicDAO;
import ca.sciencestudio.model.utilities.GID;
import ca.sciencestudio.util.exceptions.ModelAccessException;

/**
 * @author maxweld
 *
 */
public class IbatisSessionBasicDAO extends AbstractIbatisModelBasicDAO<Session> implements SessionBasicDAO {

	@Override
	public String getGidType() {
		return Session.GID_TYPE;
	}
	
	@Override
	public Session getByScanGid(String scanGid) {
		GID gid = parseAndCheckGid(scanGid, getGidFacility(), Scan.GID_TYPE);
		if(gid == null) {
			return null;
		}
		
		Session session;
		try {
			session = toModel(getSqlMapClientTemplate().queryForObject(getStatementName("get", "ByScanId"), gid.getId()));
		}
		catch(DataAccessException e) {
			logger.warn("Data Access exception while getting Session: " + e.getMessage());
			throw new ModelAccessException(e);
		}
		
		if(logger.isDebugEnabled()) {
			logger.debug("Get Session by Scan GID: " + scanGid);
		}
		return session;
	}

	@Override
	public Session getByExperimentGid(String experimentGid) {
		GID gid = parseAndCheckGid(experimentGid, getGidFacility(), Experiment.GID_TYPE);
		if(gid == null) {
			return null;
		}
		
		Session session;
		try {
			session = toModel(getSqlMapClientTemplate().queryForObject(getStatementName("get", "ByExperimentId"), gid.getId()));
		}
		catch(DataAccessException e) {
			logger.warn("Data Access exception while getting Session: " + e.getMessage());
			throw new ModelAccessException(e);
		}
		
		if(logger.isDebugEnabled()) {
			logger.debug("Get Session by Experiment GID: " + experimentGid);
		}
		return session;
	}

	@Override
	public List<Session> getAllByPersonGid(String personGid) {		
		List<Session> sessions;
		try {
			sessions = toModelList(getSqlMapClientTemplate().queryForList(getStatementName("get", "ListByPersonGid"), personGid));
		}
		catch(DataAccessException e) {
			logger.warn("Data Access exception while getting Model list: " + e.getMessage());
			throw new ModelAccessException(e);
		}
		
		if(logger.isDebugEnabled()) {
			logger.debug("Get all Sessions by Person GID: " + personGid + ", size: " + sessions.size());
		}
		return Collections.unmodifiableList(sessions);
	}
	
	@Override
	public List<Session> getAllByProjectGid(String projectGid) {
		List<Session> sessions;
		try {
			sessions = toModelList(getSqlMapClientTemplate().queryForList(getStatementName("get", "ListByProjectGid"), projectGid));
		}
		catch(DataAccessException e) {
			logger.warn("Data Access exception while getting Model list: " + e.getMessage());
			throw new ModelAccessException(e);
		}
		
		if(logger.isDebugEnabled()) {
			logger.debug("Get all Sessions by Project GID: " + projectGid + ", size: " + sessions.size());
		}
		return Collections.unmodifiableList(sessions);
	}

//	@Override
//	public List<Session> getAllByLaboratoryGid(String laboratoryGid) {
//		GID gid = parseAndCheckGid(laboratoryGid, getGidFacility(), Laboratory.GID_TYPE);
//		if(gid == null) {
//			return Collections.emptyList();
//		}
//		
//		List<Session> sessions;
//		try {
//			sessions = toModelList(getSqlMapClientTemplate().queryForList(getStatementName("get", "ListByLaboratoryId"), gid.getId()));
//		}
//		catch(DataAccessException e) {
//			logger.warn("Data Access exception while getting Model list: " + e.getMessage());
//			throw new ModelAccessException(e);
//		}
//		
//		if(logger.isDebugEnabled()) {
//			logger.debug("Get all Sessions by Laboratory GID: " + laboratoryGid + ", size: " + sessions.size());
//		}
//		return Collections.unmodifiableList(sessions);
//	}

//	@Override
//	public List<Session> getAllByLaboratoryNameAndFacilityName(String laboratoryName, String facilityName) {
//		List<Session> sessions;
//		try {
//			SqlMapParameters parameters = new SqlMapParameters(laboratoryName, facilityName);
//			sessions = toModelList(getSqlMapClientTemplate().queryForList(getStatementName("get", "ListByLaboratoryNameAndFacilityName"), parameters));
//		}
//		catch(DataAccessException e) {
//			logger.warn("Data Access exception while getting Model list: " + e.getMessage());
//			throw new ModelAccessException(e);
//		}
//		
//		if(logger.isDebugEnabled()) {
//			logger.debug("Get all Sessions by Laboratory Name: " + laboratoryName + "and Facility Name: " + facilityName + ", size: " + sessions.size());
//		}
//		return Collections.unmodifiableList(sessions);	
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
	protected Session toModel(Object obj) {
		if(!(obj instanceof IbatisSession)) {
			return null;
		}
		IbatisSession ibatisSession = (IbatisSession)obj;
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
