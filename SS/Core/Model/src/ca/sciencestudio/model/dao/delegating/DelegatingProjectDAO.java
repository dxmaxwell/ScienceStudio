/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *     DelegatingProjectDAO class.
 *     
 */
package ca.sciencestudio.model.dao.delegating;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import ca.sciencestudio.model.Project;
import ca.sciencestudio.model.Project.Status;
import ca.sciencestudio.model.dao.ProjectDAO;
import ca.sciencestudio.model.dao.support.ModelAccessException;

/**
 * @author maxweld
 *
 *
 */
public class DelegatingProjectDAO extends AbstractDelegratingModelDAO<Project, ProjectDAO> implements ProjectDAO {

	@Override
	public List<Project> getAllByStatus(Status status) {
		return getAllByStatus(status.name());
	}

	@Override
	public List<Project> getAllByStatus(String status) {
		boolean modelAccessException = true;
		List<Project> models = new ArrayList<Project>();
		for(ProjectDAO projectDAO : getProjectDAOs()) {
			try {
				models.addAll(projectDAO.getAllByStatus(status));
				modelAccessException = false;
			}
			catch(ModelAccessException e) {
				logger.warn("Delegate threw Model Access exception: " + e.getMessage());
			}
		}
		if(modelAccessException) {
			throw new ModelAccessException("Model Access exception while delegates getting all Models.");
		}
		return models;
	}	
	
	@Override
	public List<Project> getAllByPersonGid(Object personGid) {
		boolean modelAccessException = true;
		List<Project> models = new ArrayList<Project>();
		for(ProjectDAO projectDAO : getProjectDAOs()) {
			try {
				models.addAll(projectDAO.getAllByPersonGid(personGid));
				modelAccessException = false;
			}
			catch(ModelAccessException e) {
				logger.warn("Delegate threw Model Access exception: " + e.getMessage());
			}
		}
		if(modelAccessException) {
			throw new ModelAccessException("Model Access exception while delegates getting all Models.");
		}
		return models;
	}

	@Override
	public List<Project> getAllByPersonGidAndStatus(Object personGid, Status status) {
		return getAllByPersonGidAndStatus(personGid, status.name());
	}

	@Override
	public List<Project> getAllByPersonGidAndStatus(Object personGid, String status) {
		boolean modelAccessException = true;
		List<Project> models = new ArrayList<Project>();
		for(ProjectDAO projectDAO : getProjectDAOs()) {
			try {
				models.addAll(projectDAO.getAllByPersonGidAndStatus(personGid, status));
				modelAccessException = false;
			}
			catch(ModelAccessException e) {
				logger.warn("Delegate threw Model Access exception: " + e.getMessage());
			}
		}
		if(modelAccessException) {
			throw new ModelAccessException("Model Access exception while delegates getting all Models.");
		}
		return models;
	}
	
	public Collection<ProjectDAO> getProjectDAOs() {
		return getModelDAOs();
	}
	public void setProjectDAOs(Collection<ProjectDAO> projectDAOs) {
		setModelDAOs(projectDAOs);
	}
}
