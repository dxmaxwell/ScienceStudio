/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *   ProjectTreeNodeController class.
 *     
 */
package ca.sciencestudio.service.tree.controllers;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import ca.sciencestudio.model.Project;
//import ca.sciencestudio.model.project.ProjectStatus;
import ca.sciencestudio.model.dao.ProjectDAO;
import ca.sciencestudio.security.util.AuthorityUtil;
import ca.sciencestudio.security.util.SecurityUtil;
import ca.sciencestudio.service.utilities.ModelPathUtils;

/**
 * @author maxweld
 *
 */
@Controller
public class ProjectTreeNodeController extends AbstractTreeNodeController {

	@Autowired
	private ProjectDAO projectDAO;
	
	@RequestMapping(value = "/projects.json", method = RequestMethod.GET)
	public String projects(HttpServletRequest request, ModelMap model) {
		
		List<Project> projects;
		if(SecurityUtil.hasAuthority(AuthorityUtil.ROLE_ADMIN_PROJECTS)) {
			projects = projectDAO.getAllByStatus(Project.Status.ACTIVE.name());
		} else {
			projects = projectDAO.getAllByPersonUidAndStatus(getPersonUid(), Project.Status.ACTIVE.name());
		}
		
		TreeNodeList treeNodes = new TreeNodeList();
		
		for(Project project : projects) {
			TreeNodeMap treeNode = new TreeNodeMap();
			treeNode.put(TREE_NODE_ID, "PROJECT_" +  project.getGid());
			treeNode.put(TREE_NODE_DATA_URL, getTreePath(request) + ModelPathUtils.getProjectPath(project.getGid(), ".json"));
			treeNode.put(TREE_NODE_VIEW_URL, getModelPath(request) + ModelPathUtils.getProjectPath(project.getGid(), ".html"));
			treeNode.put(TREE_NODE_ICON_CLASS, "ss-project-tree-node-icon");
			treeNode.put(TREE_NODE_TEXT, project.getName());
			treeNodes.add(treeNode);
		}
	
		model.put("treeNodes", treeNodes);
		return "treeNodes-json";
	}
	
	@RequestMapping(value = "/project/{projectId}.json", method = RequestMethod.GET)
	public String project(HttpServletRequest request, ModelMap model, @PathVariable int projectId) {
		
		TreeNodeList treeNodes = new TreeNodeList();
		
		Object admin = AuthorityUtil.ROLE_ADMIN_PROJECTS;
		Object group = AuthorityUtil.buildProjectGroupAuthority(projectId);
		
		if(SecurityUtil.hasAnyAuthority(admin, group)) {
			// Project Team
			TreeNodeMap treeNode = new TreeNodeMap();
			treeNode.put(TREE_NODE_ID, "PROJECT_PERSONS_" + projectId);
			treeNode.put(TREE_NODE_DATA_URL, getTreePath(request) + ModelPathUtils.getProjectPersonsPath(projectId, ".json"));
			treeNode.put(TREE_NODE_VIEW_URL, getModelPath(request) + ModelPathUtils.getProjectPersonsPath(projectId, ".html"));
			treeNode.put(TREE_NODE_ICON_CLASS, "ss-project-persons-tree-node-icon");
			treeNode.put(TREE_NODE_TEXT, "Team");
			treeNodes.add(treeNode);
			
			// Project Samples
			treeNode = new TreeNodeMap();
			treeNode.put(TREE_NODE_ID, "PROJECT_SAMPLES_" + projectId);
			treeNode.put(TREE_NODE_DATA_URL, getTreePath(request) + ModelPathUtils.getSamplesPath(projectId, ".json"));
			treeNode.put(TREE_NODE_VIEW_URL, getModelPath(request) + ModelPathUtils.getSamplesPath(projectId, ".html"));
			treeNode.put(TREE_NODE_ICON_CLASS, "ss-project-samples-tree-node-icon");
			treeNode.put(TREE_NODE_TEXT, "Samples");
			treeNodes.add(treeNode);
			
			// Project Sessions
			treeNode = new TreeNodeMap();
			treeNode.put(TREE_NODE_ID, "PROJECT_SESSIONS_" + projectId);
			treeNode.put(TREE_NODE_DATA_URL, getTreePath(request) + ModelPathUtils.getSessionsPath(projectId, ".json"));
			treeNode.put(TREE_NODE_VIEW_URL, getModelPath(request) + ModelPathUtils.getSessionsPath(projectId, ".html"));
			treeNode.put(TREE_NODE_ICON_CLASS, "ss-project-sessions-tree-node-icon");
			treeNode.put(TREE_NODE_TEXT, "Sessions");
			treeNodes.add(treeNode);
		}
		
		model.put("treeNodes", treeNodes);
		return "treeNodes-json";
	}

	public ProjectDAO getProjectDAO() {
		return projectDAO;
	}
	public void setProjectDAO(ProjectDAO projectDAO) {
		this.projectDAO = projectDAO;
	}
}
