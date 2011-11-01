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

import ca.sciencestudio.model.project.Project;
import ca.sciencestudio.model.project.ProjectPerson;
import ca.sciencestudio.model.dao.ibatis.AbstractIbatisModelBasicDAO;
import ca.sciencestudio.util.exceptions.ModelAccessException;
import ca.sciencestudio.model.project.dao.ProjectPersonBasicDAO;
import ca.sciencestudio.model.project.dao.ibatis.support.IbatisProjectPerson;
import ca.sciencestudio.model.utilities.GID;
import ca.sciencestudio.util.sql.SqlMapParameters;

/**
 * @author maxweld
 *
 *
 */
public class IbatisProjectPersonBasicDAO extends AbstractIbatisModelBasicDAO<ProjectPerson, IbatisProjectPerson> implements ProjectPersonBasicDAO {

	@Override
	public String getGidType() {
		return ProjectPerson.GID_TYPE;
	}

	@Override
	@SuppressWarnings("unchecked")
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
			logger.debug("Get All Project Persons with Person GID: " + personGid);
		}
		return Collections.unmodifiableList(projectPersons);
	}
	
	@Override
	@SuppressWarnings("unchecked")
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
	
	@Override
	@SuppressWarnings("unchecked")
	public List<ProjectPerson> getAllByProjectGidAndPersonGid(String projectGid, Object personGid) {
		GID gid = parseAndCheckGid(projectGid, getGidFacility(), Project.GID_TYPE);
		if(gid == null) {
			return Collections.emptyList();
		}
				
		List<ProjectPerson> projectPersons;
		try {
			projectPersons = toModelList(getSqlMapClientTemplate().queryForList(getStatementName("get", "ListByProjectIdAndPersonGid"), new SqlMapParameters(gid.getId(), personGid)));
		}
		catch(DataAccessException e) {
			logger.warn("Data Access exception while getting Model: " + e.getMessage());
			throw new ModelAccessException(e);
		}
		
		if(logger.isDebugEnabled()) {
			logger.debug("Get All Project Persons with Project GID: " + projectGid + ", and Person GID: " + personGid);
		}
		return Collections.unmodifiableList(projectPersons);
	}

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
		if((projectGid != null) && projectGid.isFacilityAndType(getGidFacility(), getGidType(), true, true)) {
			ibatisProjectPerson.setProjectId(projectGid.getId());	
		}
		ibatisProjectPerson.setPersonGid(projectPerson.getPersonGid());
		ibatisProjectPerson.setRole(projectPerson.getRole());
		return ibatisProjectPerson;
	}
	
	@Override
	protected ProjectPerson toModel(IbatisProjectPerson ibatisProjectPerson) {
		if(ibatisProjectPerson == null) {
			return null;
		}
		ProjectPerson projectPerson = new ProjectPerson();
		projectPerson.setGid(GID.format(getGidFacility(), ibatisProjectPerson.getId(), getGidType()));
		projectPerson.setProjectGid(GID.format(getGidFacility(), ibatisProjectPerson.getProjectId(), Project.GID_TYPE));
		projectPerson.setPersonGid(ibatisProjectPerson.getPersonGid());
		projectPerson.setRole(ibatisProjectPerson.getRole());
		return projectPerson;
	}

	@Override
	protected String getStatementName(String prefix, String suffix) {
		return prefix + "ProjectPerson" + suffix;
	}
}
