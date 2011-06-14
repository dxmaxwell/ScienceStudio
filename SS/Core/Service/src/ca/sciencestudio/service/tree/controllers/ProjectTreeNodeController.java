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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import ca.sciencestudio.model.Project;
import ca.sciencestudio.model.dao.ProjectDAO;
import ca.sciencestudio.security.util.SecurityUtil;
import ca.sciencestudio.security.util.SecurityUtil.ROLE;
import ca.sciencestudio.service.utilities.ModelPathUtils;

/**
 * @author maxweld
 *
 */
@Controller
public class ProjectTreeNodeController extends AbstractTreeNodeController {

	@Autowired
	private ProjectDAO projectDAO;
	
	@ResponseBody
	@RequestMapping(value = "/projects*", method = RequestMethod.GET)
	public TreeNodeList projects(HttpServletRequest request) {
		
		List<Project> projects;
		if(SecurityUtil.hasAuthority(ROLE.ADMIN_PROJECTS)) {
			projects = projectDAO.getAllByStatus(Project.Status.ACTIVE);
		} else {
			projects = projectDAO.getAllByPersonGidAndStatus(getPersonUid(), Project.Status.ACTIVE);
		}
		
		TreeNodeList treeNodes = new TreeNodeList();
		
		for(Project project : projects) {
			TreeNodeMap treeNode = new TreeNodeMap();
			treeNode.put(TREE_NODE_ID, "PROJECT_" +  project.getGid());
			treeNode.put(TREE_NODE_DATA_URL, getTreePath(request) + ModelPathUtils.getProjectsPath(project.getGid(), ""));
			treeNode.put(TREE_NODE_VIEW_URL, getModelPath(request) + ModelPathUtils.getProjectsPath(project.getGid(), ".html"));
			treeNode.put(TREE_NODE_ICON_CLASS, "ss-project-tree-node-icon");
			treeNode.put(TREE_NODE_TEXT, project.getName());
			treeNodes.add(treeNode);
		}

		return treeNodes;
	}
	
	@ResponseBody
	@RequestMapping(value = "/projects/{projectGid}*", method = RequestMethod.GET)
	public TreeNodeList project(@PathVariable String projectGid, HttpServletRequest request) {
		
		TreeNodeList treeNodes = new TreeNodeList();
		
		boolean group = SecurityUtil.hasAnyProjectRole(projectGid);
		boolean admin = SecurityUtil.hasAuthority(ROLE.ADMIN_PROJECTS);
		
		if(group || admin) {
			// Project Team
			TreeNodeMap treeNode = new TreeNodeMap();
			treeNode.put(TREE_NODE_ID, "PROJECT_PERSONS_" + projectGid);
			treeNode.put(TREE_NODE_DATA_URL, getTreePath(request) + ModelPathUtils.getProjectPersonsPath(projectGid, ".json"));
			treeNode.put(TREE_NODE_VIEW_URL, getModelPath(request) + ModelPathUtils.getProjectPersonsPath(projectGid, ".html"));
			treeNode.put(TREE_NODE_ICON_CLASS, "ss-project-persons-tree-node-icon");
			treeNode.put(TREE_NODE_TEXT, "Team");
			treeNodes.add(treeNode);
			
			// Project Samples
			treeNode = new TreeNodeMap();
			treeNode.put(TREE_NODE_ID, "PROJECT_SAMPLES_" + projectGid);
			treeNode.put(TREE_NODE_DATA_URL, getTreePath(request) + ModelPathUtils.getSamplesPath(projectGid, ".json"));
			treeNode.put(TREE_NODE_VIEW_URL, getModelPath(request) + ModelPathUtils.getSamplesPath(projectGid, ".html"));
			treeNode.put(TREE_NODE_ICON_CLASS, "ss-project-samples-tree-node-icon");
			treeNode.put(TREE_NODE_TEXT, "Samples");
			treeNodes.add(treeNode);
			
			// Project Sessions
			treeNode = new TreeNodeMap();
			treeNode.put(TREE_NODE_ID, "PROJECT_SESSIONS_" + projectGid);
			treeNode.put(TREE_NODE_DATA_URL, getTreePath(request) + ModelPathUtils.getSessionsPath(projectGid, ".json"));
			treeNode.put(TREE_NODE_VIEW_URL, getModelPath(request) + ModelPathUtils.getSessionsPath(projectGid, ".html"));
			treeNode.put(TREE_NODE_ICON_CLASS, "ss-project-sessions-tree-node-icon");
			treeNode.put(TREE_NODE_TEXT, "Sessions");
			treeNodes.add(treeNode);
		}

		return treeNodes;
	}

	public ProjectDAO getProjectDAO() {
		return projectDAO;
	}
	public void setProjectDAO(ProjectDAO projectDAO) {
		this.projectDAO = projectDAO;
	}
}
