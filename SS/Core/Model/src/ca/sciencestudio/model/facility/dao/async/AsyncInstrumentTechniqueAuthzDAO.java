/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *    AsyncInstrumentTechniqueAuthzDAO class.
 *     
 */
package ca.sciencestudio.model.facility.dao.async;

import java.util.List;
import java.util.concurrent.Callable;

import ca.sciencestudio.model.dao.Data;
import ca.sciencestudio.model.dao.ModelAuthzDAO;
import ca.sciencestudio.model.dao.async.AbstractAsyncModelAuthzDAO;
import ca.sciencestudio.model.dao.async.DataFutureTask;
import ca.sciencestudio.model.facility.InstrumentTechnique;
import ca.sciencestudio.model.facility.dao.InstrumentTechniqueAuthzDAO;

/**
 * @author maxweld
 * 
 *
 */
public class AsyncInstrumentTechniqueAuthzDAO extends AbstractAsyncModelAuthzDAO<InstrumentTechnique> implements InstrumentTechniqueAuthzDAO {

	private InstrumentTechniqueAuthzDAO instrumentTechniqueAuthzDAO;
	
	@Override
	public Data<InstrumentTechnique> get(String gid) {
		return execute(new DataFutureTask<InstrumentTechnique>(new GetCallable(gid)));
	}

	@Override
	public Data<List<InstrumentTechnique>> getAll() {
		return execute(new DataFutureTask<List<InstrumentTechnique>>(new GetAllCallable()));
	}

	@Override
	public Data<List<InstrumentTechnique>> getAllByLaboratoryGid(String laboratoryGid) {
		return execute(new DataFutureTask<List<InstrumentTechnique>>(new GetAllByLaboratoryGidCallable(laboratoryGid)));
	}

	public InstrumentTechniqueAuthzDAO getInstrumentTechniqueAuthzDAO() {
		return instrumentTechniqueAuthzDAO;
	}
	
	public void setInstrumentTechniqueAuthzDAO(InstrumentTechniqueAuthzDAO instrumentTechniqueAuthzDAO) {
		this.instrumentTechniqueAuthzDAO = instrumentTechniqueAuthzDAO;
	}

	@Override
	protected ModelAuthzDAO<InstrumentTechnique> getModelAuthzDAO() {
		return instrumentTechniqueAuthzDAO;
	}

	private class GetCallable implements Callable<InstrumentTechnique> {
		
		private String gid;
		
		public GetCallable(String gid) {
			this.gid = gid;
		}

		@Override
		public InstrumentTechnique call() throws Exception {
			return instrumentTechniqueAuthzDAO.get(gid).get();
		}	
	}
	
	private class GetAllCallable implements Callable<List<InstrumentTechnique>> {

		@Override
		public List<InstrumentTechnique> call() throws Exception {
			return instrumentTechniqueAuthzDAO.getAll().get();
		}	
	}

	private class GetAllByLaboratoryGidCallable implements Callable<List<InstrumentTechnique>> {
		
		private String laboratoryGid;
		
		public GetAllByLaboratoryGidCallable(String laboratoryGid) {
			this.laboratoryGid = laboratoryGid;
		}

		@Override
		public List<InstrumentTechnique> call() throws Exception {
			return instrumentTechniqueAuthzDAO.getAllByLaboratoryGid(laboratoryGid).get();
		}	
	}
}
