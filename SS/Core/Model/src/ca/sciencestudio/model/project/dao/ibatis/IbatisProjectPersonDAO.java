/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *     IbatisProjectPersonDAO class.
 *     
 */
package ca.sciencestudio.model.project.dao.ibatis;

import java.util.List;

import org.springframework.orm.ibatis.support.SqlMapClientDaoSupport;

import ca.sciencestudio.model.project.ProjectPerson;
import ca.sciencestudio.model.project.dao.ProjectPersonDAO;
import ca.sciencestudio.model.project.ibatis.IbatisProjectPerson;
import ca.sciencestudio.util.sql.SqlMapParameters;

/**
 * @author maxweld
 *
 */
public class IbatisProjectPersonDAO extends SqlMapClientDaoSupport implements ProjectPersonDAO {
	
	@Override
	public ProjectPerson createProjectPerson() {
		return new IbatisProjectPerson();
	}

	@Override
	public int addProjectPerson(ProjectPerson projectPerson) {
		int key = (Integer) getSqlMapClientTemplate().insert("addProjectPerson", projectPerson);
		logger.info("Add project person with id: " + key);
		return key;
	}

	@Override
	public void editProjectPerson(ProjectPerson projectPerson) {
		int count = getSqlMapClientTemplate().update("editProjectPerson", projectPerson);
		logger.info("Edit project person with id: " + projectPerson.getId() + ", rows affected: " + count);
	}
	
	@Override
	public void removeProjectPerson(int projectPersonId) {
		int count = getSqlMapClientTemplate().delete("removeProjectPerson", projectPersonId);
		logger.info("Remove project person with id: " + projectPersonId + ", rows affected: " + count);
	}

	@Override
	public void removeProjectPerson(ProjectPerson projectPerson) {
		int count = getSqlMapClientTemplate().delete("removeProjectPerson", projectPerson.getId());
		logger.info("Remove project person with id: " + projectPerson.getId() + ", rows affected: " + count);
	}
	
	@Override
	public ProjectPerson getProjectPersonById(int projectPersonId) {
		ProjectPerson projectPerson = (ProjectPerson) getSqlMapClientTemplate().queryForObject("getProjectPersonById", projectPersonId);
		logger.info("Get project person with id: " + projectPersonId);
		return projectPerson;
	}

	@Override
	public ProjectPerson getProjectPersonByProjectIdAndPersonUid(int projectId, String personUid) {
		SqlMapParameters params = new SqlMapParameters(projectId, personUid);
		ProjectPerson projectPerson = (ProjectPerson) getSqlMapClientTemplate().queryForObject("getProjectPersonByProjectIdAndPersonUid", params);
		logger.info("Get project person with project id: " + projectId + ", and person uid: " + personUid);
		return projectPerson;
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public List<ProjectPerson> getProjectPersonList() {
		List<ProjectPerson> list = getSqlMapClientTemplate().queryForList("getProjectPersonList");
		logger.info("Get project person list, size: " + list.size());
		return list;
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public List<ProjectPerson> getProjectPersonListByProjectId(int projectId) {
		List<ProjectPerson> list = getSqlMapClientTemplate().queryForList("getProjectPersonListByProjectId", projectId);
		logger.info("Get project person with project id: " + projectId);
		return list;
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<ProjectPerson> getProjectPersonListByPersonUid(String personUid) {
		List<ProjectPerson> list = getSqlMapClientTemplate().queryForList("getProjectPersonListByPersonUid", personUid);
		logger.info("Get project person with person uid: " + personUid);
		return list;
	}
}
