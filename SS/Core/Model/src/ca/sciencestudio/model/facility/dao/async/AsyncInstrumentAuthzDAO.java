/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *    AsyncInstrumentAuthzDAO class.
 *     
 */
package ca.sciencestudio.model.facility.dao.async;

import java.util.List;
import java.util.concurrent.Callable;

import ca.sciencestudio.model.dao.Data;
import ca.sciencestudio.model.dao.ModelAuthzDAO;
import ca.sciencestudio.model.dao.async.AbstractAsyncModelAuthzDAO;
import ca.sciencestudio.model.dao.async.DataFutureTask;
import ca.sciencestudio.model.facility.Instrument;
import ca.sciencestudio.model.facility.dao.InstrumentAuthzDAO;

/**
 * @author maxweld
 * 
 *
 */
public class AsyncInstrumentAuthzDAO extends AbstractAsyncModelAuthzDAO<Instrument> implements InstrumentAuthzDAO {

	public InstrumentAuthzDAO instrumentAuthzDAO;
	
	@Override
	public Data<Instrument> get(String gid) {
		return execute(new DataFutureTask<Instrument>(new GetCallable(gid)));
	}

	@Override
	public Data<List<Instrument>> getAll() {
		return execute(new DataFutureTask<List<Instrument>>(new GetAllCallable()));
	}

	@Override
	public Data<List<Instrument>> getAllByLaboratoryGid(String laboratoryGid) {
		return execute(new DataFutureTask<List<Instrument>>(new GetAllByLaboratoryGidCallable(laboratoryGid)));
	}
	
	public InstrumentAuthzDAO getInstrumentAuthzDAO() {
		return instrumentAuthzDAO;
	}

	public void setInstrumentAuthzDAO(InstrumentAuthzDAO instrumentAuthzDAO) {
		this.instrumentAuthzDAO = instrumentAuthzDAO;
	}

	@Override
	protected ModelAuthzDAO<Instrument> getModelAuthzDAO() {
		return instrumentAuthzDAO;
	}
	
	private class GetCallable implements Callable<Instrument> {
		
		private String gid;
		
		public GetCallable(String gid) {
			this.gid = gid;
		}

		@Override
		public Instrument call() throws Exception {
			return instrumentAuthzDAO.get(gid).get();
		}	
	}

	private class GetAllCallable implements Callable<List<Instrument>> {

		@Override
		public List<Instrument> call() throws Exception {
			return instrumentAuthzDAO.getAll().get();
		}	
	}
	
	private class GetAllByLaboratoryGidCallable implements Callable<List<Instrument>> {
		
		private String laboratoryGid;
		
		public GetAllByLaboratoryGidCallable(String laboratoryGid) {
			this.laboratoryGid = laboratoryGid;
		}

		@Override
		public List<Instrument> call() throws Exception {
			return instrumentAuthzDAO.getAllByLaboratoryGid(laboratoryGid).get();
		}	
	}
}
