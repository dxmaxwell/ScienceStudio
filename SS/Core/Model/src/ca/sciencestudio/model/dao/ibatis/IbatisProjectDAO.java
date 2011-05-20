/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *     IbatisProjectDAO class.
 *     
 */
package ca.sciencestudio.model.dao.ibatis;

import java.util.Collections;
import java.util.List;

import org.springframework.dao.DataAccessException;

import ca.sciencestudio.model.dao.ibatis.support.AbstractIbatisModelDAO;
import ca.sciencestudio.model.dao.ibatis.support.IbatisProject;
import ca.sciencestudio.model.dao.support.ModelAccessException;
import ca.sciencestudio.model.Project;
import ca.sciencestudio.model.dao.ProjectDAO;
import ca.sciencestudio.model.utilities.GID;
import ca.sciencestudio.util.sql.SqlMapParameters;

/**
 * @author maxweld
 * 
 */
public class IbatisProjectDAO extends AbstractIbatisModelDAO<Project, IbatisProject> implements ProjectDAO {
	
	@Override
	public String getType() {
		return Project.GID_TYPE;
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public List<Project> getAllByStatus(String status) {
		List<Project> projects;
		try {
			projects = toModelList(getSqlMapClientTemplate().queryForList(getStatementName("get", "ListByStatus"), status));
		}
		catch(DataAccessException e) {
			logger.warn("Data Access exception while getting Model list: " + e.getMessage());
			throw new ModelAccessException(e);
		}
		
		if(logger.isDebugEnabled()) {
			logger.debug("Get all Projects by status: " + status + ", size: " + projects.size());
		}
		return Collections.unmodifiableList(projects);
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<Project> getAllByPersonUidAndStatus(String personUid, String status) {
		List<Project> projects;
		try {
			projects = toModelList(getSqlMapClientTemplate().queryForList(getStatementName("get", "ListByPersonUidAndStatus"), new SqlMapParameters(personUid, status)));
		}
		catch(DataAccessException e) {
			logger.warn("Data Access exception while getting Model list: " + e.getMessage());
			throw new ModelAccessException(e);
		}
		
		if(logger.isDebugEnabled()) {
			logger.debug("Get all Projects by status: " + status + ", size: " + projects.size());
		}
		return Collections.unmodifiableList(projects);
	}

	@Override
	protected IbatisProject toIbatisModel(Project project) {
		IbatisProject ibatisProject = new IbatisProject();
		ibatisProject.setId(GID.parse(project.getGid()).getId());
		ibatisProject.setName(project.getName());
		ibatisProject.setDescription(project.getDescription());
		ibatisProject.setStartDate(project.getStartDate());
		ibatisProject.setEndDate(project.getEndDate());
		ibatisProject.setStatus(project.getStatus());
		return ibatisProject;
	}
	
	@Override
	protected Project toModel(IbatisProject ibatisProject) {
		if(ibatisProject == null) {
			return null;
		}
		Project project = new Project();
		project.setGid(GID.format(getFacility(), ibatisProject.getId(), getType()));
		project.setName(ibatisProject.getName());
		project.setDescription(ibatisProject.getDescription());
		project.setStartDate(ibatisProject.getStartDate());
		project.setEndDate(ibatisProject.getEndDate());
		project.setStatus(ibatisProject.getStatus());
		return project;
	}

	@Override
	protected String getStatementName(String prefix, String suffix) {
		return prefix + "Project" + suffix;
	}	
}
