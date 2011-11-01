/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *     LaboratoryAuthzDAO interface.
 *     
 */
package ca.sciencestudio.model.facility.dao.async;

import java.util.List;
import java.util.concurrent.Callable;

import ca.sciencestudio.model.dao.Data;
import ca.sciencestudio.model.dao.ModelAuthzDAO;
import ca.sciencestudio.model.dao.async.AbstractAsyncModelAuthzDAO;
import ca.sciencestudio.model.dao.async.DataFutureTask;
import ca.sciencestudio.model.facility.Laboratory;
import ca.sciencestudio.model.facility.dao.LaboratoryAuthzDAO;

/**
 * @author maxweld
 * 
 *
 */
public class AsyncLaboratoryAuthzDAO extends AbstractAsyncModelAuthzDAO<Laboratory> implements LaboratoryAuthzDAO {

	private LaboratoryAuthzDAO laboratoryAuthzDAO;
	
	@Override
	public Data<Laboratory> get(String gid) {
		return execute(new DataFutureTask<Laboratory>(new GetCallable(gid)));
	}

	@Override
	public Data<List<Laboratory>> getAll() {
		return execute(new DataFutureTask<List<Laboratory>>(new GetAllCallable()));
	}

	public LaboratoryAuthzDAO getLaboratoryAuthzDAO() {
		return laboratoryAuthzDAO;
	}

	public void setLaboratoryAuthzDAO(LaboratoryAuthzDAO laboratoryAuthzDAO) {
		this.laboratoryAuthzDAO = laboratoryAuthzDAO;
	}

	@Override
	protected ModelAuthzDAO<Laboratory> getModelAuthzDAO() {
		return laboratoryAuthzDAO;
	}

	private class GetCallable implements Callable<Laboratory> {
		
		private String gid;
		
		public GetCallable(String gid) {
			this.gid = gid;
		}

		@Override
		public Laboratory call() throws Exception {
			return laboratoryAuthzDAO.get(gid).get();
		}	
	}
	
	private class GetAllCallable implements Callable<List<Laboratory>> {

		@Override
		public List<Laboratory> call() throws Exception {
			return laboratoryAuthzDAO.getAll().get();
		}	
	}
}
