/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *    AsyncTechniqueAuthzDAO class.
 *     
 */
package ca.sciencestudio.model.facility.dao.async;

import java.util.List;
import java.util.concurrent.Callable;

import ca.sciencestudio.model.dao.Data;
import ca.sciencestudio.model.dao.ModelAuthzDAO;
import ca.sciencestudio.model.dao.async.AbstractAsyncModelAuthzDAO;
import ca.sciencestudio.model.dao.async.DataFutureTask;
import ca.sciencestudio.model.facility.Technique;
import ca.sciencestudio.model.facility.dao.TechniqueAuthzDAO;

/**
 * @author maxweld
 * 
 *
 */
public class AsyncTechniqueAuthzDAO extends AbstractAsyncModelAuthzDAO<Technique> implements TechniqueAuthzDAO {

	private TechniqueAuthzDAO techniqueAuthzDAO;
	
	@Override
	public Data<Technique> get(String gid) {
		return execute(new DataFutureTask<Technique>(new GetCallable(gid)));
	}

	@Override
	public Data<List<Technique>> getAll() {
		return execute(new DataFutureTask<List<Technique>>(new GetAllCallable()));
	}

	@Override
	public Data<List<Technique>> getAllByLaboratoryGid(String laboratoryGid) {
		return execute(new DataFutureTask<List<Technique>>(new GetAllByLaboratoryGidCallable(laboratoryGid)));
	}
	
	@Override
	public Data<List<Technique>> getAllByInstrumentGid(String instrumentGid) {
		return execute(new DataFutureTask<List<Technique>>(new GetAllByInstrumentGidCallable(instrumentGid)));
	}

	public TechniqueAuthzDAO getTechniqueAuthzDAO() {
		return techniqueAuthzDAO;
	}

	public void setTechniqueAuthzDAO(TechniqueAuthzDAO techniqueAuthzDAO) {
		this.techniqueAuthzDAO = techniqueAuthzDAO;
	}

	@Override
	protected ModelAuthzDAO<Technique> getModelAuthzDAO() {
		return techniqueAuthzDAO;
	}

	private class GetCallable implements Callable<Technique> {
		
		private String gid;
		
		public GetCallable(String gid) {
			this.gid = gid;
		}

		@Override
		public Technique call() throws Exception {
			return techniqueAuthzDAO.get(gid).get();
		}	
	}

	private class GetAllCallable implements Callable<List<Technique>> {

		@Override
		public List<Technique> call() throws Exception {
			return techniqueAuthzDAO.getAll().get();
		}	
	}

	private class GetAllByLaboratoryGidCallable implements Callable<List<Technique>> {
		
		private String laboratoryGid;
		
		public GetAllByLaboratoryGidCallable(String laboratoryGid) {
			this.laboratoryGid = laboratoryGid;
		}

		@Override
		public List<Technique> call() throws Exception {
			return techniqueAuthzDAO.getAllByLaboratoryGid(laboratoryGid).get();
		}	
	}
	
	private class GetAllByInstrumentGidCallable implements Callable<List<Technique>> {
		
		private String instrumentGid;
		
		public GetAllByInstrumentGidCallable(String instrumentGid) {
			this.instrumentGid = instrumentGid;
		}

		@Override
		public List<Technique> call() throws Exception {
			return techniqueAuthzDAO.getAllByInstrumentGid(instrumentGid).get();
		}	
	}
}
