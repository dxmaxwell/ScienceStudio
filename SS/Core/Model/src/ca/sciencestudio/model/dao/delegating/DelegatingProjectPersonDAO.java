/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *     DelegatingProjectPersonDAO abstract class.
 *     
 */
package ca.sciencestudio.model.dao.delegating;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import ca.sciencestudio.model.ProjectPerson;
import ca.sciencestudio.model.dao.ProjectPersonDAO;
import ca.sciencestudio.model.dao.delegating.support.AbstractDelegratingModelDAO;
import ca.sciencestudio.model.dao.support.ModelAccessException;

/**
 * @author maxweld
 * 
 *
 */
public class DelegatingProjectPersonDAO extends AbstractDelegratingModelDAO<ProjectPerson, ProjectPersonDAO> implements ProjectPersonDAO {

	@Override
	public List<ProjectPerson> getAllByPersonGid(Object personGid) {
		
		boolean modelAccessException = true;
		List<ProjectPerson> models = new ArrayList<ProjectPerson>();
		for(ProjectPersonDAO projectPersonDAO : getProjectPersonDAOs()) {
			try {
				models.addAll(projectPersonDAO.getAllByPersonGid(personGid));
				modelAccessException = false;
			}
			catch(ModelAccessException e) {
				logger.warn("Delegate threw Model Access exception: " + e.getMessage());
			}
		}
		if(modelAccessException) {
			throw new ModelAccessException("Model Access exception while delegates getting all Project Persons.");
		}
		return models;
	}
	
	@Override
	public List<ProjectPerson> getAllByProjectGid(Object projectGid) {
		
		boolean modelAccessException = true;
		List<ProjectPerson> models = new ArrayList<ProjectPerson>();
		for(ProjectPersonDAO projectPersonDAO : getProjectPersonDAOs()) {
			try {
				models.addAll(projectPersonDAO.getAllByProjectGid(projectGid));
				modelAccessException = false;
			}
			catch(ModelAccessException e) {
				logger.warn("Delegate threw Model Access exception: " + e.getMessage());
			}
		}
		if(modelAccessException) {
			throw new ModelAccessException("Model Access exception while delegates getting all Project Persons.");
		}
		return models;
	}
	
	@Override
	public List<ProjectPerson> getAllByProjectGidAndPersonGid(Object projectGid, Object personGid) {
		
		boolean modelAccessException = true;
		List<ProjectPerson> models = new ArrayList<ProjectPerson>();
		for(ProjectPersonDAO projectPersonDAO : getProjectPersonDAOs()) {
			try {
				models.addAll(projectPersonDAO.getAllByProjectGidAndPersonGid(projectGid, personGid));
				modelAccessException = false;
			}
			catch(ModelAccessException e) {
				logger.warn("Delegate threw Model Access exception: " + e.getMessage());
			}
		}
		if(modelAccessException) {
			throw new ModelAccessException("Model Access exception while delegates getting all Project Persons.");
		}
		return models;
	}
	

	public Collection<ProjectPersonDAO> getProjectPersonDAOs() {
		return getModelDAOs();
	}
	public void setProjectPersonDAOs(Collection<ProjectPersonDAO> projectPersonDAOs) {
		setModelDAOs(projectPersonDAOs);
	}
}
