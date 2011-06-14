/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *     IbatisProjectPersonDAO class.
 *     
 */
package ca.sciencestudio.model.dao.ibatis;

import java.util.Collections;
import java.util.List;

import org.springframework.dao.DataAccessException;

import ca.sciencestudio.model.Project;
import ca.sciencestudio.model.ProjectPerson;
import ca.sciencestudio.model.dao.ProjectPersonDAO;
import ca.sciencestudio.model.dao.ibatis.support.AbstractIbatisModelDAO;
import ca.sciencestudio.model.dao.ibatis.support.IbatisProjectPerson;
import ca.sciencestudio.model.dao.support.ModelAccessException;
import ca.sciencestudio.model.utilities.GID;
import ca.sciencestudio.util.sql.SqlMapParameters;

/**
 * @author maxweld
 *
 *
 */
public class IbatisProjectPersonDAO extends AbstractIbatisModelDAO<ProjectPerson, IbatisProjectPerson> implements ProjectPersonDAO {

	@Override
	public String getGidType() {
		return ProjectPerson.GID_TYPE;
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<ProjectPerson> getAllByPersonGid(Object personGid) {
						
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
	public List<ProjectPerson> getAllByProjectGid(Object projectGid) {		
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
	public List<ProjectPerson> getAllByProjectGidAndPersonGid(Object projectGid, Object personGid) {
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
		IbatisProjectPerson ibatisProjectPerson = new IbatisProjectPerson();
		GID gid = GID.parse(projectPerson.getGid());
		if(gid != null) { ibatisProjectPerson.setId(gid.getId()); }
		ibatisProjectPerson.setPersonGid(projectPerson.getPersonGid());
		ibatisProjectPerson.setProjectId(projectPerson.getProjectId());
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
		projectPerson.setPersonGid(ibatisProjectPerson.getPersonGid());
		projectPerson.setProjectId(ibatisProjectPerson.getProjectId());
		projectPerson.setRole(ibatisProjectPerson.getRole());
		return projectPerson;
	}

	@Override
	protected String getStatementName(String prefix, String suffix) {
		return prefix + "ProjectPerson" + suffix;
	}
}
