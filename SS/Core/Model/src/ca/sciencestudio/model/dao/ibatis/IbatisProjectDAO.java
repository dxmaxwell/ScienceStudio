/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *     IbatisProjectDAO class.
 *     
 */
package ca.sciencestudio.model.dao.ibatis;

import ca.sciencestudio.model.dao.ibatis.support.AbstractIbatisModelDAO;
import ca.sciencestudio.model.dao.ibatis.support.IbatisProject;
import ca.sciencestudio.model.Project;
import ca.sciencestudio.model.dao.ProjectDAO;
import ca.sciencestudio.model.utilities.GID;

/**
 * @author maxweld
 * 
 */
public class IbatisProjectDAO extends AbstractIbatisModelDAO<Project, IbatisProject> implements ProjectDAO {
	
	@Override
	public String getGidType() {
		return Project.GID_TYPE;
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
