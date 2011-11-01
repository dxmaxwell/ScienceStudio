/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *     IbatisSessionPersonBasicDAO interface.
 *     
 */
package ca.sciencestudio.model.session.dao.ibatis;

import java.util.Collections;
import java.util.List;

import org.springframework.dao.DataAccessException;

import ca.sciencestudio.model.dao.ibatis.AbstractIbatisModelBasicDAO;
import ca.sciencestudio.model.session.Session;
import ca.sciencestudio.model.session.SessionPerson;
import ca.sciencestudio.model.session.SessionPerson.Role;
import ca.sciencestudio.model.session.dao.SessionPersonBasicDAO;
import ca.sciencestudio.model.session.dao.ibatis.support.IbatisSessionPerson;
import ca.sciencestudio.model.utilities.GID;
import ca.sciencestudio.util.exceptions.ModelAccessException;

/**
 * @author maxweld
 * 
 *
 */
public class IbatisSessionPersonBasicDAO extends AbstractIbatisModelBasicDAO<SessionPerson> implements SessionPersonBasicDAO {

	@Override
	public String getGidType() {
		return SessionPerson.GID_TYPE;
	}

	@Override
	public List<SessionPerson> getAllByPersonGid(String personGid) {
		List<SessionPerson> sessionPersons;
		try {
			sessionPersons = toModelList(getSqlMapClientTemplate().queryForList(getStatementName("get", "ListByPersonGid"), personGid));
		}
		catch(DataAccessException e) {
			logger.warn("Data Access exception while getting Model list: " + e.getMessage());
			throw new ModelAccessException(e);
		}
		
		if(logger.isDebugEnabled()) {
			logger.debug("Get all SessionPersons by Person GID: " + personGid + ", size: " + sessionPersons.size());
		}
		return Collections.unmodifiableList(sessionPersons);
	}

	@Override
	public List<SessionPerson> getAllBySessionGid(String sessionGid) {
		GID gid = parseAndCheckGid(sessionGid, getGidFacility(), Session.GID_TYPE);
		if(gid == null) {
			return Collections.emptyList();
		}
		
		List<SessionPerson> sessionPersons;
		try {
			sessionPersons = toModelList(getSqlMapClientTemplate().queryForList(getStatementName("get", "ListBySessionId"), gid.getId()));
		}
		catch(DataAccessException e) {
			logger.warn("Data Access exception while getting Model list: " + e.getMessage());
			throw new ModelAccessException(e);
		}
		
		if(logger.isDebugEnabled()) {
			logger.debug("Get all SessionPersons by Session GID: " + sessionGid + ", size: " + sessionPersons.size());
		}
		return Collections.unmodifiableList(sessionPersons);
	}

//	@Override
//	public List<SessionPerson> getAllBySessionMember(String personGid) {
//		List<SessionPerson> sessionPersons;
//		try {			
//			sessionPersons = toModelList(getSqlMapClientTemplate().queryForList(getStatementName("get", "ListBySessionMember"), personGid));
//		}
//		catch(DataAccessException e) {
//			logger.warn("Data Access exception while getting Model list: " + e.getMessage());
//			throw new ModelAccessException(e);
//		}
//		
//		if(logger.isDebugEnabled()) {
//			logger.debug("Get all SessionPersons by Session Member: " + personGid + ", size: " + sessionPersons.size());
//		}
//		return Collections.unmodifiableList(sessionPersons);
//	}
	
	@Override
	protected IbatisSessionPerson toIbatisModel(SessionPerson sessionPerson) {
		if(sessionPerson == null) {
			return null;
		}
		IbatisSessionPerson ibatisSessionPerson = new IbatisSessionPerson();
		GID gid = GID.parse(sessionPerson.getGid());
		if((gid != null) && gid.isFacilityAndType(getGidFacility(), getGidType(), true, true)) {
			ibatisSessionPerson.setId(gid.getId());
		}
		GID sessionGid = GID.parse(sessionPerson.getSessionGid());
		if((sessionGid != null) && sessionGid.isFacilityAndType(getGidFacility(), Session.GID_TYPE, true, true)) {
			ibatisSessionPerson.setSessionId(sessionGid.getId());
		}
		ibatisSessionPerson.setPersonGid(sessionPerson.getPersonGid());
		ibatisSessionPerson.setRole(sessionPerson.getRole().name());
		return ibatisSessionPerson;
	}
	
	@Override
	protected SessionPerson toModel(Object obj) {
		if(!(obj instanceof IbatisSessionPerson)) {
			return null;
		}
		IbatisSessionPerson ibatisSessionPerson = (IbatisSessionPerson)obj;
		SessionPerson sessionPerson = new SessionPerson();
		sessionPerson.setGid(GID.format(getGidFacility(), ibatisSessionPerson.getId(), getGidType()));
		sessionPerson.setSessionGid(GID.format(getGidFacility(), ibatisSessionPerson.getSessionId(), Session.GID_TYPE));
		sessionPerson.setPersonGid(ibatisSessionPerson.getPersonGid());
		sessionPerson.setRole(Role.valueOf(ibatisSessionPerson.getRole()));
		return sessionPerson;
	}

	@Override
	protected String getStatementName(String prefix, String suffix) {
		return prefix + "SessionPerson" + suffix;
	}
}
