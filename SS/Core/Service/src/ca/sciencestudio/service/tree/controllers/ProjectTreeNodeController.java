/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *  	ProjectTreeNodeController class.
 *     
 */
package ca.sciencestudio.service.tree.controllers;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import ca.sciencestudio.model.project.Project;
import ca.sciencestudio.model.project.dao.ProjectAuthzDAO;
import ca.sciencestudio.security.util.SecurityUtil;
import ca.sciencestudio.service.utilities.ModelPathUtils;

/**
 * @author maxweld
 *
 */
@Controller
public class ProjectTreeNodeController extends AbstractTreeNodeController {
	
	private ProjectAuthzDAO projectAuthzDAO;
	
	@ResponseBody
	@RequestMapping(value = ModelPathUtils.PROJECT_PATH + "*", method = RequestMethod.GET)
	public TreeNodeList projects() {
		
		List<Project> projects = projectAuthzDAO.getAll(SecurityUtil.getPersonGid()).get();
		
		TreeNodeList treeNodes = new TreeNodeList();
		
		for(Project project : projects) {
			TreeNodeMap treeNode = new TreeNodeMap();
			treeNode.put(TREE_NODE_ID, "PROJECT_" +  project.getGid());
			treeNode.put(TREE_NODE_DATA_URL, ModelPathUtils.getTreeProjectPath("/", project.getGid()));
			treeNode.put(TREE_NODE_VIEW_URL, ModelPathUtils.getModelProjectPath("/", project.getGid(), ".html"));
			treeNode.put(TREE_NODE_ICON_CLASS, "ss-project-tree-node-icon");
			treeNode.put(TREE_NODE_TEXT, project.getName());
			treeNodes.add(treeNode);
		}

		return treeNodes;
	}
	
	@ResponseBody
	@RequestMapping(value = ModelPathUtils.PROJECT_PATH + "/{projectGid}*", method = RequestMethod.GET)
	public TreeNodeList project(@PathVariable String projectGid) {
		
		TreeNodeList treeNodes = new TreeNodeList();
		
		// No restricted content ahead, so no security check required.
		
		// Project Team
		TreeNodeMap treeNode = new TreeNodeMap();
		treeNode.put(TREE_NODE_ID, "PROJECT_PERSONS_" + projectGid);
		treeNode.put(TREE_NODE_DATA_URL, ModelPathUtils.getTreeProjectPersonPath(".json", "?project=", projectGid));
		treeNode.put(TREE_NODE_VIEW_URL, ModelPathUtils.getModelProjectPersonPath(".html", "?project=", projectGid));
		treeNode.put(TREE_NODE_ICON_CLASS, "ss-project-persons-tree-node-icon");
		treeNode.put(TREE_NODE_TEXT, "Team");
		treeNodes.add(treeNode);
		
		// Project Samples
		treeNode = new TreeNodeMap();
		treeNode.put(TREE_NODE_ID, "PROJECT_SAMPLES_" + projectGid);
		treeNode.put(TREE_NODE_DATA_URL, ModelPathUtils.getTreeSamplePath(".json", "?project=", projectGid));
		treeNode.put(TREE_NODE_VIEW_URL, ModelPathUtils.getModelSamplePath(".html", "?project=", projectGid));
		treeNode.put(TREE_NODE_ICON_CLASS, "ss-project-samples-tree-node-icon");
		treeNode.put(TREE_NODE_TEXT, "Samples");
		treeNodes.add(treeNode);
		
		// Project Sessions
		treeNode = new TreeNodeMap();
		treeNode.put(TREE_NODE_ID, "PROJECT_SESSIONS_" + projectGid);
		treeNode.put(TREE_NODE_DATA_URL, ModelPathUtils.getTreeSessionPath(".json", "?project=", projectGid));
		treeNode.put(TREE_NODE_VIEW_URL, ModelPathUtils.getModelSessionPath(".html", "?project=", projectGid));
		treeNode.put(TREE_NODE_ICON_CLASS, "ss-project-sessions-tree-node-icon");
		treeNode.put(TREE_NODE_TEXT, "Sessions");
		treeNodes.add(treeNode);

		return treeNodes;
	}

	public ProjectAuthzDAO getProjectAuthzDAO() {
		return projectAuthzDAO;
	}
	public void setProjectAuthzDAO(ProjectAuthzDAO projectAuthzDAO) {
		this.projectAuthzDAO = projectAuthzDAO;
	}
}
