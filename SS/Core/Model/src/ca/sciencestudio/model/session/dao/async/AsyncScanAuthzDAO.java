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
import ca.sciencestudio.util.rest.FileProps;

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
	public Data<InputStream> getFileData(String user, String gid, String path) {
		return execute(new DataFutureTask<InputStream>(new GetFileDataCallable(user, gid, path)));
	}

	@Override
	public Data<List<FileProps>> getFileList(String user, String gid, String path) {
		return execute(new DataFutureTask<List<FileProps>>(new GetFileListCallable(user, gid, path)));
	}

	@Override
	public Data<List<FileProps>> getFileList(String user, String gid, String path, String type) {
		return execute(new DataFutureTask<List<FileProps>>(new GetFileListByTypeCallable(user, gid, path, type)));
	}

	@Override
	public Data<List<FileProps>> getFileList(String user, String gid, String path, String type, int depth) {
		return execute(new DataFutureTask<List<FileProps>>(new GetFileListByTypeAndDepthCallable(user, gid, path, type, depth)));
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
	
	private class GetFileDataCallable implements Callable<InputStream> {

		private String user;
		private String gid;
		private String path;
		
		public GetFileDataCallable(String user, String gid, String path) {
			this.user = user;
			this.gid = gid;
			this.path = path;
		}

		@Override
		public InputStream call() throws Exception {
			return scanAuthzDAO.getFileData(user, gid, path).get();
		}
	}
	
	private class GetFileListCallable implements Callable<List<FileProps>> {

		protected String user;
		protected String gid;
		protected String path;
		
		public GetFileListCallable(String user, String gid, String path) {
			this.user = user;
			this.gid = gid;
			this.path = path;
		}

		@Override
		public List<FileProps> call() throws Exception {
			return scanAuthzDAO.getFileList(user, gid, path).get();
		}
	}
	
	private class GetFileListByTypeCallable extends GetFileListCallable {
		
		protected String type;
		
		public GetFileListByTypeCallable(String user, String gid, String path, String type) {
			super(user, gid, path);
			this.type = type;
		}

		@Override
		public List<FileProps> call() throws Exception {
			return scanAuthzDAO.getFileList(user, gid, path, type).get();
		}
	}
	
	private class GetFileListByTypeAndDepthCallable extends GetFileListByTypeCallable {

		protected int depth;
		
		public GetFileListByTypeAndDepthCallable(String user, String gid, String path, String type, int depth) {
			super(user, gid, path, type);
			this.depth = depth;
		}

		@Override
		public List<FileProps> call() throws Exception {
			return scanAuthzDAO.getFileList(user, gid, path, type, depth).get();
		}
	}
}
