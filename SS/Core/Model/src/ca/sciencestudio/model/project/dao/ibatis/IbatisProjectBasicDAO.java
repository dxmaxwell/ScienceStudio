/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *     IbatisProjectBasicDAO class.
 *     
 */
package ca.sciencestudio.model.project.dao.ibatis;

import java.util.Collections;
import java.util.List;

import org.springframework.dao.DataAccessException;

import ca.sciencestudio.model.dao.ibatis.AbstractIbatisModelBasicDAO;
import ca.sciencestudio.util.exceptions.ModelAccessException;
import ca.sciencestudio.model.project.Project;
import ca.sciencestudio.model.project.Project.Status;
import ca.sciencestudio.model.project.dao.ProjectBasicDAO;
import ca.sciencestudio.model.project.dao.ibatis.support.IbatisProject;
import ca.sciencestudio.model.utilities.GID;
import ca.sciencestudio.util.sql.SqlMapParameters;

/**
 * @author maxweld
 * 
 */
public class IbatisProjectBasicDAO extends AbstractIbatisModelBasicDAO<Project, IbatisProject> implements ProjectBasicDAO {
	
	@Override
	public String getGidType() {
		return Project.GID_TYPE;
	}
	
	@Override
	public List<Project> getAllByStatus(Status status) {
		return getAllByStatus(status.name());
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
	public List<Project> getAllByPersonGid(Object personGid) {
		List<Project> projects;
		try {
			projects = toModelList(getSqlMapClientTemplate().queryForList(getStatementName("get", "ListByPersonUid"), personGid));
		}
		catch(DataAccessException e) {
			logger.warn("Data Access exception while getting Model list: " + e.getMessage());
			throw new ModelAccessException(e);
		}
		
		if(logger.isDebugEnabled()) {
			logger.debug("Get all Projects by Person GID: " + personGid + ", size: " + projects.size());
		}
		return Collections.unmodifiableList(projects);
	}

	@Override
	public List<Project> getAllByPersonGidAndStatus(Object personGid, Status status) {
		return getAllByPersonGidAndStatus(personGid, status.name());
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<Project> getAllByPersonGidAndStatus(Object personGid, String status) {
		List<Project> projects;
		try {
			projects = toModelList(getSqlMapClientTemplate().queryForList(getStatementName("get", "ListByPersonUidAndStatus"), new SqlMapParameters(personGid, status)));
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
		if(project == null) {
			return null;
		}
		IbatisProject ibatisProject = new IbatisProject();
		GID gid = GID.parse(project.getGid());
		if((gid != null) && gid.isFacilityAndType(getGidFacility(), getGidType(), true, true)) {
			ibatisProject.setId(gid.getId());
		}
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
		project.setGid(GID.format(getGidFacility(), ibatisProject.getId(), getGidType()));
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
