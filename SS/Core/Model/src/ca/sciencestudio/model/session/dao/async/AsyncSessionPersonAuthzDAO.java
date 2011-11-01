/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *     AsyncSessionPersonAuthzDAO class.
 *     
 */
package ca.sciencestudio.model.session.dao.async;

import java.util.List;
import java.util.concurrent.Callable;

import ca.sciencestudio.model.dao.Data;
import ca.sciencestudio.model.dao.ModelAuthzDAO;
import ca.sciencestudio.model.dao.async.AbstractAsyncModelAuthzDAO;
import ca.sciencestudio.model.dao.async.DataFutureTask;
import ca.sciencestudio.model.session.SessionPerson;
import ca.sciencestudio.model.session.dao.SessionPersonAuthzDAO;

/**
 * @author maxweld
 * 
 *
 */
public class AsyncSessionPersonAuthzDAO extends AbstractAsyncModelAuthzDAO<SessionPerson> implements SessionPersonAuthzDAO {

	private SessionPersonAuthzDAO sessionPersonAuthzDAO;
	
	@Override
	public Data<List<SessionPerson>> getAllBySessionGid(String user, String sessionGid) {
		return execute(new DataFutureTask<List<SessionPerson>>(new GetAllBySessionGidCallable(user, sessionGid)));
	}

	public SessionPersonAuthzDAO getSessionPersonAuthzDAO() {
		return sessionPersonAuthzDAO;
	}

	public void setSessionPersonAuthzDAO(SessionPersonAuthzDAO sessionPersonAuthzDAO) {
		this.sessionPersonAuthzDAO = sessionPersonAuthzDAO;
	}

	@Override
	protected ModelAuthzDAO<SessionPerson> getModelAuthzDAO() {
		return sessionPersonAuthzDAO;
	}

	private class GetAllBySessionGidCallable implements Callable<List<SessionPerson>> {

		private String user;
		private String sessionGid;
		
		public GetAllBySessionGidCallable(String user, String sessionGid) {
			this.user = user;
			this.sessionGid = sessionGid;
		}

		@Override
		public List<SessionPerson> call() throws Exception {
			return sessionPersonAuthzDAO.getAllBySessionGid(user, sessionGid).get();
		}
	}
}
