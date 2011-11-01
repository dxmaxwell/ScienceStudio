/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *     AsyncProjectPersonAuthzDAO class.
 *     
 */
package ca.sciencestudio.model.project.dao.async;

import java.util.List;
import java.util.concurrent.Callable;

import ca.sciencestudio.model.dao.Data;
import ca.sciencestudio.model.dao.ModelAuthzDAO;
import ca.sciencestudio.model.dao.async.AbstractAsyncModelAuthzDAO;
import ca.sciencestudio.model.dao.async.DataFutureTask;
import ca.sciencestudio.model.project.ProjectPerson;
import ca.sciencestudio.model.project.dao.ProjectPersonAuthzDAO;

/**
 * @author maxweld
 * 
 *
 */
public class AsyncProjectPersonAuthzDAO extends AbstractAsyncModelAuthzDAO<ProjectPerson> implements ProjectPersonAuthzDAO {

	private ProjectPersonAuthzDAO projectPersonAuthzDAO;
	
	@Override
	public Data<List<ProjectPerson>> getAllByPersonGid(String user, String personGid) {
		return execute(new DataFutureTask<List<ProjectPerson>>(new GetAllByPersonGidCallable(user, personGid)));
	}

	@Override
	public Data<List<ProjectPerson>> getAllByProjectGid(String user, String projectGid) {
		return execute(new DataFutureTask<List<ProjectPerson>>(new GetAllByProjectGidCallable(user, projectGid)));
	}

	public ProjectPersonAuthzDAO getProjectPersonAuthzDAO() {
		return projectPersonAuthzDAO;
	}
	
	public void setProjectPersonAuthzDAO(ProjectPersonAuthzDAO projectPersonAuthzDAO) {
		this.projectPersonAuthzDAO = projectPersonAuthzDAO;
	}
	
	@Override
	protected ModelAuthzDAO<ProjectPerson> getModelAuthzDAO() {
		return projectPersonAuthzDAO;
	}
	
	private class GetAllByPersonGidCallable implements Callable<List<ProjectPerson>> {

		private String user;
		private String personGid;
		
		public GetAllByPersonGidCallable(String user, String personGid) {
			this.user = user;
			this.personGid = personGid;
		}

		@Override
		public List<ProjectPerson> call() throws Exception {
			return projectPersonAuthzDAO.getAllByPersonGid(user, personGid).get();
		}
	}
	
	private class GetAllByProjectGidCallable implements Callable<List<ProjectPerson>> {

		private String user;
		private String projectGid;
		
		public GetAllByProjectGidCallable(String user, String projectGid) {
			this.user = user;
			this.projectGid = projectGid;
		}

		@Override
		public List<ProjectPerson> call() throws Exception {
			return projectPersonAuthzDAO.getAllByProjectGid(user, projectGid).get();
		}
	}
}
