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

import ca.sciencestudio.model.dao.Data;
import ca.sciencestudio.model.dao.ModelAuthzDAO;
import ca.sciencestudio.model.dao.async.AbstractAsyncModelAuthzDAO;
import ca.sciencestudio.model.dao.async.DataFutureTask;
import ca.sciencestudio.model.session.Experiment;
import ca.sciencestudio.model.session.dao.ExperimentAuthzDAO;

/**
 * @author maxweld
 * 
 *
 */
public class AsyncExperimentAuthzDAO extends AbstractAsyncModelAuthzDAO<Experiment> implements ExperimentAuthzDAO {

	private ExperimentAuthzDAO experimentAuthzDAO;
	
	@Override
	public Data<List<Experiment>> getAllBySessionGid(String user, String sessionGid) {
		return execute(new DataFutureTask<List<Experiment>>(new GetAllBySessionGidCallable(user, sessionGid)));
	}

	@Override
	public Data<List<Experiment>> getAllBySourceGid(String user, String sourceGid) {
		return execute(new DataFutureTask<List<Experiment>>(new GetAllBySourceGidCallable(user, sourceGid)));
	}

	public ExperimentAuthzDAO getExperimentAuthzDAO() {
		return experimentAuthzDAO;
	}

	public void setExperimentAuthzDAO(ExperimentAuthzDAO experimentAuthzDAO) {
		this.experimentAuthzDAO = experimentAuthzDAO;
	}

	@Override
	protected ModelAuthzDAO<Experiment> getModelAuthzDAO() {
		return experimentAuthzDAO;
	}

	private class GetAllBySessionGidCallable implements Callable<List<Experiment>> {

		private String user;
		private String sessionGid;
		
		public GetAllBySessionGidCallable(String user, String sessionGid) {
			this.user = user;
			this.sessionGid = sessionGid;
		}

		@Override
		public List<Experiment> call() throws Exception {
			return experimentAuthzDAO.getAllBySessionGid(user, sessionGid).get();
		}
	}
	
	private class GetAllBySourceGidCallable implements Callable<List<Experiment>> {

		private String user;
		private String sourceGid;
		
		public GetAllBySourceGidCallable(String user, String sourceGid) {
			this.user = user;
			this.sourceGid = sourceGid;
		}

		@Override
		public List<Experiment> call() throws Exception {
			return experimentAuthzDAO.getAllBySourceGid(user, sourceGid).get();
		}
	}
}
