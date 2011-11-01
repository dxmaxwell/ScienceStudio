/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *     IbatisProjectPersonBasicDAO class.
 *     
 */
package ca.sciencestudio.model.project.dao.ibatis;

import java.util.Collections;
import java.util.List;

import org.springframework.dao.DataAccessException;

import ca.sciencestudio.model.dao.ibatis.AbstractIbatisModelBasicDAO;
import ca.sciencestudio.model.project.Project;
import ca.sciencestudio.model.project.ProjectPerson;
import ca.sciencestudio.model.project.ProjectPerson.Role;
import ca.sciencestudio.model.project.dao.ProjectPersonBasicDAO;
import ca.sciencestudio.model.project.dao.ibatis.support.IbatisProjectPerson;
import ca.sciencestudio.util.exceptions.ModelAccessException;
import ca.sciencestudio.model.utilities.GID;

/**
 * @author maxweld
 *
 *
 */
public class IbatisProjectPersonBasicDAO extends AbstractIbatisModelBasicDAO<ProjectPerson> implements ProjectPersonBasicDAO {

	@Override
	public String getGidType() {
		return ProjectPerson.GID_TYPE;
	}

	@Override
	public List<ProjectPerson> getAllByPersonGid(String personGid) {
		List<ProjectPerson> projectPersons;
		try {
			projectPersons = toModelList(getSqlMapClientTemplate().queryForList(getStatementName("get", "ListByPersonGid"), personGid));
		}
		catch(DataAccessException e) {
			logger.warn("Data Access exception while getting Model: " + e.getMessage());
			throw new ModelAccessException(e);
		}
		
		if(logger.isDebugEnabled()) {
			logger.debug("Get All ProjectPersons with Person GID: " + personGid);
		}
		return Collections.unmodifiableList(projectPersons);
	}
	
	@Override
	public List<ProjectPerson> getAllByProjectGid(String projectGid) {
		GID gid = parseAndCheckGid(projectGid, getGidFacility(), Project.GID_TYPE);
		if(gid == null) {
			return Collections.emptyList();
		}
				
		List<ProjectPerson> projectPersons;
		try {
			projectPersons = toModelList(getSqlMapClientTemplate().queryForList(getStatementName("get", "ListByProjectId"), gid.getId()));
		}
		catch(DataAccessException e) {
			logger.warn("Data Access exception while getting Model: " + e.getMessage());
			throw new ModelAccessException(e);
		}
		
		if(logger.isDebugEnabled()) {
			logger.debug("Get All Project Persons with Project GID: " + projectGid);
		}
		return Collections.unmodifiableList(projectPersons);
	}
	
//	@Override
//	public List<ProjectPerson> getAllByProjectMember(String personGid) {
//		List<ProjectPerson> projectPersons;
//		try {
//			projectPersons = toModelList(getSqlMapClientTemplate().queryForList(getStatementName("get", "ListByProjectMember"), personGid));
//		}
//		catch(DataAccessException e) {
//			logger.warn("Data Access exception while getting Model: " + e.getMessage());
//			throw new ModelAccessException(e);
//		}
//		
//		if(logger.isDebugEnabled()) {
//			logger.debug("Get All ProjectPersons by Project Member: " + personGid + ", size: " + projectPersons.size());
//		}
//		return Collections.unmodifiableList(projectPersons);
//	}

	@Override
	protected IbatisProjectPerson toIbatisModel(ProjectPerson projectPerson) {
		if(projectPerson == null) {
			return null;
		}
		IbatisProjectPerson ibatisProjectPerson = new IbatisProjectPerson();
		GID gid = GID.parse(projectPerson.getGid());
		if((gid != null) && gid.isFacilityAndType(getGidFacility(), getGidType(), true, true)) {
			ibatisProjectPerson.setId(gid.getId());
		}
		GID projectGid = GID.parse(projectPerson.getProjectGid());
		if((projectGid != null) && projectGid.isFacilityAndType(getGidFacility(), Project.GID_TYPE, true, true)) {
			ibatisProjectPerson.setProjectId(projectGid.getId());	
		}
		ibatisProjectPerson.setPersonGid(projectPerson.getPersonGid());
		ibatisProjectPerson.setRole(projectPerson.getRole().name());
		return ibatisProjectPerson;
	}
	
	@Override
	protected ProjectPerson toModel(Object obj) {
		if(!(obj instanceof IbatisProjectPerson)) {
			return null;
		}
		IbatisProjectPerson ibatisProjectPerson = (IbatisProjectPerson)obj;
		ProjectPerson projectPerson = new ProjectPerson();
		projectPerson.setGid(GID.format(getGidFacility(), ibatisProjectPerson.getId(), getGidType()));
		projectPerson.setProjectGid(GID.format(getGidFacility(), ibatisProjectPerson.getProjectId(), Project.GID_TYPE));
		projectPerson.setPersonGid(ibatisProjectPerson.getPersonGid());
		projectPerson.setRole(Role.valueOf(ibatisProjectPerson.getRole()));
		return projectPerson;
	}

	@Override
	protected String getStatementName(String prefix, String suffix) {
		return prefix + "ProjectPerson" + suffix;
	}
}
