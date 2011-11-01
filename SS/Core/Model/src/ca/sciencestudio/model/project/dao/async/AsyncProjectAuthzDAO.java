/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *     AsyncProjectAuthzDAO class.
 *     
 */
package ca.sciencestudio.model.project.dao.async;

import java.util.List;
import java.util.concurrent.Callable;

import ca.sciencestudio.model.project.Project;
import ca.sciencestudio.model.project.dao.ProjectAuthzDAO;
import ca.sciencestudio.model.dao.AuthoritiesDAO;
import ca.sciencestudio.model.dao.Data;
import ca.sciencestudio.model.dao.ModelAuthzDAO;
import ca.sciencestudio.model.dao.async.AsyncAuthoritiesModelAuthzDAO;
import ca.sciencestudio.model.dao.async.DataFutureTask;

/**
 * @author maxweld
 * 
 *
 */
public class AsyncProjectAuthzDAO extends AsyncAuthoritiesModelAuthzDAO<Project> implements ProjectAuthzDAO {

	private ProjectAuthzDAO projectAuthzDAO;
	
	public Data<List<Project>> getAll(String user) {
		return execute(new DataFutureTask<List<Project>>(new GetAllCallable(user)));
	}
	
	public ProjectAuthzDAO getProjectAuthzDAO() {
		return projectAuthzDAO;
	}
	
	public void setProjectAuthzDAO(ProjectAuthzDAO projectAuthzDAO) {
		this.projectAuthzDAO = projectAuthzDAO;
	}

	@Override
	protected ModelAuthzDAO<Project> getModelAuthzDAO() {
		return projectAuthzDAO;
	}
	
	@Override
	protected AuthoritiesDAO getAuthoritiesDAO() {
		return projectAuthzDAO;
	}
	
	private class GetAllCallable implements Callable<List<Project>> {

		private String user;
		
		public GetAllCallable(String user) {
			this.user = user;
		}

		@Override
		public List<Project> call() throws Exception {
			return getProjectAuthzDAO().getAll(user).get();
		}
	}
}
