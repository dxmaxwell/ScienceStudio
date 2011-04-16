/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *     IbatisProjectDAO class.
 *     
 */

package ca.sciencestudio.model.project.dao.ibatis;

import java.util.List;

import org.springframework.orm.ibatis.support.SqlMapClientDaoSupport;

import ca.sciencestudio.model.project.Project;
import ca.sciencestudio.model.project.dao.ProjectDAO;
import ca.sciencestudio.model.project.ibatis.IbatisProject;
import ca.sciencestudio.util.sql.SqlMapParameters;

import ca.sciencestudio.model.project.ProjectStatus;

/**
 * @author medrand
 * 
 */
public class IbatisProjectDAO extends SqlMapClientDaoSupport implements ProjectDAO {

	@Override
	public Project createProject() {
		return new IbatisProject();
	}	

	@Override
	public int addProject(Project project) {
		int key = (Integer) getSqlMapClientTemplate().insert("addProject", project);
		logger.debug("Added new project with key: " + key);
		return key;
	}

	@Override
	public void editProject(Project project) {
		int count = getSqlMapClientTemplate().update("editProject", project);
		logger.debug("Edit project with id: " + project.getId() + ", rows affected: " + count);
	}

	@Override
	public void removeProject(int projectId){
		int count = getSqlMapClientTemplate().delete("removeProject", projectId);
		logger.debug("Remove project with id: " + projectId + ", rows affected: " + count);
	}

	@Override
	public void removeProject(Project project) {
		int count = getSqlMapClientTemplate().delete("removeProject", project.getId());
		logger.debug("Remove project with id: " + project.getId() + ", rows affected: " + count);
	}
		
	@Override
	public Project getProjectById(int projectId) {
		Project project = (Project) getSqlMapClientTemplate().queryForObject("getProjectById", projectId);
		logger.debug("Get project with id: " + projectId);
		return project;
	}

	@Override
	public Project getProjectBySessionId(int sessionId) {
		Project project = (Project) getSqlMapClientTemplate().queryForObject("getProjectBySessionId", sessionId);
		logger.debug("Get project with session id: " + sessionId);
		return project;
	}
	
	@Override
	public Project getProjectByExperimentId(int experimentId) {
		Project project = (Project) getSqlMapClientTemplate().queryForObject("getProjectByExperimentId", experimentId);
		logger.debug("Get project with experiment Id: " + experimentId);
		return project;
	}

	@Override
	public Project getProjectByScanId(int scanId) {
		Project project = (Project) getSqlMapClientTemplate().queryForObject("getProjectByScanId", scanId);
		logger.debug("Get project with scan Id: " + scanId);
		return project;
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public List<Project> getProjectList() {
		List<Project> projects = getSqlMapClientTemplate().queryForList("getProjectList");
		logger.debug("Get project list, size: " + projects.size());
		return projects;
	} 
	
	@Override
	@SuppressWarnings("unchecked")
	public List<Project> getProjectListByPersonUid(String personUid) {
		List<Project> projects = getSqlMapClientTemplate().queryForList("getProjectListByPersonUid", personUid);
		logger.debug("Get project list with person uid: " + personUid + ", size: " + projects.size());
		return projects;
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public List<Project> getProjectListByStatus(ProjectStatus status) {
		List<Project> projects = getSqlMapClientTemplate().queryForList("getProjectListByStatus", status.name());
		logger.debug("Get project list with status: " + status.name() + ", size: " + projects.size());
		return projects;
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public List<Project> getProjectListByPersonUidAndStatus(String personUid, ProjectStatus status) {
		SqlMapParameters params = new SqlMapParameters(personUid, status.name());
		List<Project> projects = getSqlMapClientTemplate().queryForList("getProjectListByPersonUidAndStatus", params);
		logger.debug("Get project list with person uid: " + personUid + ", size: " + projects.size());
		return projects;
	}
}
