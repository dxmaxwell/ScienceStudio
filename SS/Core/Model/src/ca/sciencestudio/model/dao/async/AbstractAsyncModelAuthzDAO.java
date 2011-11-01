/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *    AbstractAsyncModelAuthzDAO class.
 *     
 */
package ca.sciencestudio.model.dao.async;

import java.util.concurrent.Callable;
import java.util.concurrent.Executor;

import ca.sciencestudio.util.rest.AddResult;
import ca.sciencestudio.util.rest.EditResult;
import ca.sciencestudio.util.rest.RemoveResult;
import ca.sciencestudio.model.Model;
import ca.sciencestudio.model.dao.Data;
import ca.sciencestudio.model.dao.ModelAuthzDAO;

/**
 * @author maxweld
 *
 *
 */
public abstract class AbstractAsyncModelAuthzDAO<T extends Model> implements ModelAuthzDAO<T> {
	
	private Executor executor = new DefaultExecutor();
	
	@Override
	public Data<AddResult> add(String personGid, T t) {
		return execute(new DataFutureTask<AddResult>(new AddCallable(personGid, t)));
	}

	@Override
	public Data<EditResult> edit(String personGid, T t) {
		return execute(new DataFutureTask<EditResult>(new EditCallable(personGid, t)));
	}

	@Override
	public Data<RemoveResult> remove(String personGid, String gid) {
		return execute(new DataFutureTask<RemoveResult>(new RemoveCallable(personGid, gid)));
	}

	@Override
	public Data<T> get(String user, String gid) {
		return execute(new DataFutureTask<T>(new GetCallable(user, gid)));
	}
	
	public Executor getExecutor() {
		return executor;
	}

	public void setExecutor(Executor executor) {
		this.executor = executor;
	}

	protected <D> Data<D> execute(DataFutureTask<D> dataFutureTask) {
		executor.execute(dataFutureTask);
		return dataFutureTask;
	}
	
	protected abstract ModelAuthzDAO<T> getModelAuthzDAO();
	
	private class AddCallable implements Callable<AddResult> {
		
		private String user;
		private T t;
		
		public AddCallable(String user, T t) {
			this.user = user;
			this.t = t;
		}

		@Override
		public AddResult call() throws Exception {
			return getModelAuthzDAO().add(user, t).get();
		}
	}
	
	private class EditCallable implements Callable<EditResult> {
		
		private String user;
		private T t;
		
		public EditCallable(String user, T t) {
			this.user = user;
			this.t = t;
		}

		@Override
		public EditResult call() {
			return getModelAuthzDAO().edit(user, t).get();
		}
	}
	
	private class RemoveCallable implements Callable<RemoveResult> {
		
		private String user;
		private String gid;
		
		public RemoveCallable(String user, String gid) {
			this.user = user;
			this.gid = gid;
		}

		@Override
		public RemoveResult call() {
			return getModelAuthzDAO().remove(user, gid).get();
		}
	}
	
	private class GetCallable implements Callable<T> {

		private String user;
		private String gid;
		
		public GetCallable(String user, String gid) {
			this.user = user;
			this.gid = gid;
		}

		@Override
		public T call() throws Exception {
			return getModelAuthzDAO().get(user, gid).get();
		}
	}
	
	private class DefaultExecutor implements Executor {

		@Override
		public void execute(Runnable command) {
			Thread thread = new Thread(command);
			thread.setDaemon(true);
			thread.start();
		}
	}
}
