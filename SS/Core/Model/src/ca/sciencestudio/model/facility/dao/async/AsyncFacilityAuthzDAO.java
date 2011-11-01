/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *    AsyncFacilityAuthzDAO class.
 *     
 */
package ca.sciencestudio.model.facility.dao.async;

import java.util.List;
import java.util.concurrent.Callable;

import ca.sciencestudio.model.dao.AuthoritiesDAO;
import ca.sciencestudio.model.dao.Data;
import ca.sciencestudio.model.dao.ModelAuthzDAO;
import ca.sciencestudio.model.dao.async.AsyncAuthoritiesModelAuthzDAO;
import ca.sciencestudio.model.dao.async.DataFutureTask;
import ca.sciencestudio.model.facility.Facility;
import ca.sciencestudio.model.facility.dao.FacilityAuthzDAO;

/**
 * @author maxweld
 * 
 *
 */
public class AsyncFacilityAuthzDAO extends AsyncAuthoritiesModelAuthzDAO<Facility> implements FacilityAuthzDAO {

	private FacilityAuthzDAO facilityAuthzDAO;
	
	@Override
	public Data<Facility> get(String gid) {
		return execute(new DataFutureTask<Facility>(new GetCallable(gid)));
	}

	@Override
	public Data<List<Facility>> getAll() {
		return execute(new DataFutureTask<List<Facility>>(new GetAllCallable()));
	}

	public FacilityAuthzDAO getFacilityAuthzDAO() {
		return facilityAuthzDAO;
	}

	public void setFacilityAuthzDAO(FacilityAuthzDAO facilityAuthzDAO) {
		this.facilityAuthzDAO = facilityAuthzDAO;
	}

	@Override
	protected ModelAuthzDAO<Facility> getModelAuthzDAO() {
		return facilityAuthzDAO;
	}

	@Override
	protected AuthoritiesDAO getAuthoritiesDAO() {
		return facilityAuthzDAO;
	}

	private class GetCallable implements Callable<Facility> {
		
		private String gid;
		
		public GetCallable(String gid) {
			this.gid = gid;
		}

		@Override
		public Facility call() throws Exception {
			return facilityAuthzDAO.get(gid).get();
		}	
	}
	
	private class GetAllCallable implements Callable<List<Facility>> {
		
		@Override
		public List<Facility> call() throws Exception {
			return facilityAuthzDAO.getAll().get();
		}	
	}
}
