/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *     AsyncSampleAuthzDAO class.
 *     
 */
package ca.sciencestudio.model.sample.dao.async;

import java.util.List;
import java.util.concurrent.Callable;

import ca.sciencestudio.model.dao.Data;
import ca.sciencestudio.model.dao.ModelAuthzDAO;
import ca.sciencestudio.model.dao.async.AbstractAsyncModelAuthzDAO;
import ca.sciencestudio.model.dao.async.DataFutureTask;
import ca.sciencestudio.model.sample.Sample;
import ca.sciencestudio.model.sample.dao.SampleAuthzDAO;

/**
 * @author maxweld
 * 
 *
 */
public class AsyncSampleAuthzDAO extends AbstractAsyncModelAuthzDAO<Sample> implements SampleAuthzDAO {

	private SampleAuthzDAO sampleAuthzDAO;
	
	@Override
	public Data<List<Sample>> getAllByProjectGid(String user, String projectGid) {
		return execute(new DataFutureTask<List<Sample>>(new GetAllByProjectGidCallable(user, projectGid)));
	}
	
	public SampleAuthzDAO getSampleAuthzDAO() {
		return sampleAuthzDAO;
	}

	public void setSampleAuthzDAO(SampleAuthzDAO sampleAuthzDAO) {
		this.sampleAuthzDAO = sampleAuthzDAO;
	}

	@Override
	protected ModelAuthzDAO<Sample> getModelAuthzDAO() {
		return sampleAuthzDAO;
	}

	private class GetAllByProjectGidCallable implements Callable<List<Sample>> {

		private String user;
		private String projectGid;
		
		public GetAllByProjectGidCallable(String user, String projectGid) {
			this.user = user;
			this.projectGid = projectGid;
		}

		@Override
		public List<Sample> call() throws Exception {
			return sampleAuthzDAO.getAllByProjectGid(user, projectGid).get();
		}
	}
}
