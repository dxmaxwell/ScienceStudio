/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *     IbatisSessionPersonBasicDAO interface.
 *     
 */
package ca.sciencestudio.model.session.dao.ibatis;

import ca.sciencestudio.model.dao.ibatis.AbstractIbatisModelBasicDAO;
import ca.sciencestudio.model.session.Session;
import ca.sciencestudio.model.session.SessionPerson;
import ca.sciencestudio.model.session.dao.SessionPersonBasicDAO;
import ca.sciencestudio.model.session.dao.ibatis.support.IbatisSessionPerson;
import ca.sciencestudio.model.utilities.GID;

/**
 * @author maxweld
 * 
 *
 */
public class IbatisSessionPersonBasicDAO extends AbstractIbatisModelBasicDAO<SessionPerson, IbatisSessionPerson> implements SessionPersonBasicDAO {

	@Override
	public String getGidType() {
		return SessionPerson.GID_TYPE;
	}

	@Override
	protected IbatisSessionPerson toIbatisModel(SessionPerson t) {
		if(t == null) {
			return null;
		}
		IbatisSessionPerson s = new IbatisSessionPerson();
		GID gid = GID.parse(t.getGid());
		if((gid != null) && gid.isFacilityAndType(getGidFacility(), getGidType(), true, true)) {
			s.setId(gid.getId());
		}
		GID sessionGid = GID.parse(t.getSessionGid());
		if((sessionGid != null) && sessionGid.isFacilityAndType(getGidFacility(), Session.GID_TYPE, true, true)) {
			s.setSessionId(sessionGid.getId());
		}
		s.setPersonGid(t.getPersonGid());
		return s;
	}
	
	@Override
	protected SessionPerson toModel(IbatisSessionPerson t) {
		if(t == null) {
			return null;
		}
		SessionPerson s = new SessionPerson();
		s.setGid(GID.format(getGidFacility(), t.getId(), getGidType()));
		s.setSessionGid(GID.format(getGidFacility(), t.getSessionId(), Session.GID_TYPE));
		s.setPersonGid(t.getPersonGid());
		return s;
	}

	@Override
	protected String getStatementName(String prefix, String suffix) {
		return prefix + "SessionPerson" + suffix;
	}
}
