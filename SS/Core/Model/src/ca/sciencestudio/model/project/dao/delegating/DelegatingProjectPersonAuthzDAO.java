/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *     DelegatingProjectPersonAuthzDAO abstract class.
 *     
 */
package ca.sciencestudio.model.project.dao.delegating;

import java.util.List;
import java.util.ArrayList;
import java.util.Collection;

import ca.sciencestudio.model.project.ProjectPerson;
import ca.sciencestudio.model.project.dao.ProjectPersonAuthzDAO;
import ca.sciencestudio.model.dao.delegating.AbstractDelegratingModelAuthzDAO;
import ca.sciencestudio.util.exceptions.ModelAccessException;

/**
 * @author maxweld
 * 
 *
 */
public class DelegatingProjectPersonAuthzDAO extends AbstractDelegratingModelAuthzDAO<ProjectPerson, ProjectPersonAuthzDAO> implements ProjectPersonAuthzDAO {

//	@Override
//	public List<ProjectPerson> getAllByPersonGid(Object personGid) {
//		
//		boolean modelAccessException = true;
//		List<ProjectPerson> models = new ArrayList<ProjectPerson>();
//		for(ProjectPersonBasicDAO projectPersonDAO : getProjectPersonDAOs()) {
//			try {
//				models.addAll(projectPersonDAO.getAllByPersonGid(personGid));
//				modelAccessException = false;
//			}
//			catch(ModelAccessException e) {
//				logger.warn("Delegate threw Model Access exception: " + e.getMessage());
//			}
//		}
//		if(modelAccessException) {
//			throw new ModelAccessException("Model Access exception while delegates getting all Project Persons.");
//		}
//		return models;
//	}
	
//	@Override
//	public List<ProjectPerson> getAllByProjectGid(Object projectGid) {
//		
//		boolean modelAccessException = true;
//		List<ProjectPerson> models = new ArrayList<ProjectPerson>();
//		for(ProjectPersonBasicDAO projectPersonDAO : getProjectPersonDAOs()) {
//			try {
//				models.addAll(projectPersonDAO.getAllByProjectGid(projectGid));
//				modelAccessException = false;
//			}
//			catch(ModelAccessException e) {
//				logger.warn("Delegate threw Model Access exception: " + e.getMessage());
//			}
//		}
//		if(modelAccessException) {
//			throw new ModelAccessException("Model Access exception while delegates getting all Project Persons.");
//		}
//		return models;
//	}
	
//	@Override
//	public List<ProjectPerson> getAllByProjectGidAndPersonGid(Object projectGid, Object personGid) {
//		
//		boolean modelAccessException = true;
//		List<ProjectPerson> models = new ArrayList<ProjectPerson>();
//		for(ProjectPersonBasicDAO projectPersonDAO : getProjectPersonDAOs()) {
//			try {
//				models.addAll(projectPersonDAO.getAllByProjectGidAndPersonGid(projectGid, personGid));
//				modelAccessException = false;
//			}
//			catch(ModelAccessException e) {
//				logger.warn("Delegate threw Model Access exception: " + e.getMessage());
//			}
//		}
//		if(modelAccessException) {
//			throw new ModelAccessException("Model Access exception while delegates getting all Project Persons.");
//		}
//		return models;
//	}
	

	public Collection<ProjectPersonAuthzDAO> getProjectPersonDAOs() {
		return getModelDAOs();
	}
	public void setProjectPersonDAOs(Collection<ProjectPersonAuthzDAO> projectPersonDAOs) {
		setModelDAOs(projectPersonDAOs);
	}
}
