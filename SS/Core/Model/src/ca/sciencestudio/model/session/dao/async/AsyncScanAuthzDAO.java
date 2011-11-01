/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *     AsyncScanAuthzDAO class.
 *     
 */
package ca.sciencestudio.model.session.dao.async;

import java.io.InputStream;
import java.util.List;
import java.util.concurrent.Callable;

import ca.sciencestudio.model.dao.Data;
import ca.sciencestudio.model.dao.ModelAuthzDAO;
import ca.sciencestudio.model.dao.async.AbstractAsyncModelAuthzDAO;
import ca.sciencestudio.model.dao.async.DataFutureTask;
import ca.sciencestudio.model.session.Scan;
import ca.sciencestudio.model.session.dao.ScanAuthzDAO;

/**
 * @author maxweld
 * 
 *
 */
public class AsyncScanAuthzDAO extends AbstractAsyncModelAuthzDAO<Scan> implements ScanAuthzDAO {

	private ScanAuthzDAO scanAuthzDAO;
	
	@Override
	public Data<List<Scan>> getAllByExperimentGid(String user, String experimentGid) {
		return execute(new DataFutureTask<List<Scan>>(new GetAllByExperimentGidCallable(user, experimentGid)));
	}

	@Override
	public Data<InputStream> getData(String user, String gid, String path) {
		return execute(new DataFutureTask<InputStream>(new GetDataCallable(user, gid, path)));
	}

	public ScanAuthzDAO getScanAuthzDAO() {
		return scanAuthzDAO;
	}
	
	public void setScanAuthzDAO(ScanAuthzDAO scanAuthzDAO) {
		this.scanAuthzDAO = scanAuthzDAO;
	}

	@Override
	protected ModelAuthzDAO<Scan> getModelAuthzDAO() {
		return scanAuthzDAO;
	}
	
	private class GetAllByExperimentGidCallable implements Callable<List<Scan>> {

		private String user;
		private String experimentGid;
		
		public GetAllByExperimentGidCallable(String user, String experimentGid) {
			this.user = user;
			this.experimentGid = experimentGid;
		}

		@Override
		public List<Scan> call() throws Exception {
			return scanAuthzDAO.getAllByExperimentGid(user, experimentGid).get();
		}
	}
	
	private class GetDataCallable implements Callable<InputStream> {

		private String user;
		private String gid;
		private String path;
		
		public GetDataCallable(String user, String gid, String path) {
			this.user = user;
			this.gid = gid;
			this.path = path;
		}

		@Override
		public InputStream call() throws Exception {
			return scanAuthzDAO.getData(user, gid, path).get();
		}
	}
}
