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

import ca.sciencestudio.model.project.Project;
import ca.sciencestudio.model.project.dao.ProjectBasicDAO;
import ca.sciencestudio.model.project.dao.ibatis.support.IbatisProject;
import ca.sciencestudio.model.dao.ibatis.AbstractIbatisModelBasicDAO;
import ca.sciencestudio.model.utilities.GID;
import ca.sciencestudio.util.exceptions.ModelAccessException;

/**
 * @author maxweld
 * 
 */
public class IbatisProjectBasicDAO extends AbstractIbatisModelBasicDAO<Project> implements ProjectBasicDAO {
	
	@Override
	public String getGidType() {
		return Project.GID_TYPE;
	}

	@Override
	public List<Project> getAllByPersonGid(String personGid) {
		List<Project> projects;
		try {
			projects = toModelList(getSqlMapClientTemplate().queryForList(getStatementName("get", "ListByPersonGid"), personGid));
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
		ibatisProject.setStatus(project.getStatus().name());
		return ibatisProject;
	}
	
	@Override
	protected Project toModel(Object obj) {
		if(!(obj instanceof IbatisProject)) {
			return null;
		}
		IbatisProject ibatisProject = (IbatisProject)obj;
		Project project = new Project();
		project.setGid(GID.format(getGidFacility(), ibatisProject.getId(), getGidType()));
		project.setName(ibatisProject.getName());
		project.setDescription(ibatisProject.getDescription());
		project.setStartDate(ibatisProject.getStartDate());
		project.setEndDate(ibatisProject.getEndDate());
		project.setStatus(Project.Status.valueOf(ibatisProject.getStatus()));
		return project;
	}

	@Override
	protected String getStatementName(String prefix, String suffix) {
		return prefix + "Project" + suffix;
	}	
}
