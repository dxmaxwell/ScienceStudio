/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *     AsyncSessionAuthzDAO class.
 *     
 */
package ca.sciencestudio.model.session.dao.async;

import java.util.List;
import java.util.concurrent.Callable;

import ca.sciencestudio.model.dao.AuthoritiesDAO;
import ca.sciencestudio.model.dao.Data;
import ca.sciencestudio.model.dao.ModelAuthzDAO;
import ca.sciencestudio.model.dao.async.AsyncAuthoritiesModelAuthzDAO;
import ca.sciencestudio.model.dao.async.DataFutureTask;
import ca.sciencestudio.model.session.Session;
import ca.sciencestudio.model.session.dao.SessionAuthzDAO;

/**
 * @author maxweld
 * 
 *
 */
public class AsyncSessionAuthzDAO extends AsyncAuthoritiesModelAuthzDAO<Session> implements SessionAuthzDAO {

	private SessionAuthzDAO sessionAuthzDAO;
	
	@Override
	public Data<Session> getByScanGid(String user, String scanGid) {
		return get(user, scanGid);
	}

	@Override
	public Data<Session> getByExperimentGid(String user, String experimentGid) {
		return get(user, experimentGid);
	}

	@Override
	public Data<List<Session>> getAll(String user) {
		return execute(new DataFutureTask<List<Session>>(new GetAllCallable(user)));
	}

	@Override
	public Data<List<Session>> getAllByProjectGid(String user, String projectGid) {
		return execute(new DataFutureTask<List<Session>>(new GetAllByProjectGidCallable(user, projectGid)));
	}

	public SessionAuthzDAO getSessionAuthzDAO() {
		return sessionAuthzDAO;
	}

	public void setSessionAuthzDAO(SessionAuthzDAO sessionAuthzDAO) {
		this.sessionAuthzDAO = sessionAuthzDAO;
	}
	
	@Override
	protected AuthoritiesDAO getAuthoritiesDAO() {
		return sessionAuthzDAO;
	}

	@Override
	protected ModelAuthzDAO<Session> getModelAuthzDAO() {
		return sessionAuthzDAO;
	}

	private class GetAllCallable implements Callable<List<Session>> {

		private String user;
		
		public GetAllCallable(String user) {
			this.user = user;
		}

		@Override
		public List<Session> call() throws Exception {
			return sessionAuthzDAO.getAll(user).get();
		}
	}
	
	private class GetAllByProjectGidCallable implements Callable<List<Session>> {

		private String user;
		private String projectGid;
		
		public GetAllByProjectGidCallable(String user, String projectGid) {
			this.user = user;
			this.projectGid = projectGid;
		}

		@Override
		public List<Session> call() throws Exception {
			return sessionAuthzDAO.getAllByProjectGid(user, projectGid).get();
		}
	}
}
