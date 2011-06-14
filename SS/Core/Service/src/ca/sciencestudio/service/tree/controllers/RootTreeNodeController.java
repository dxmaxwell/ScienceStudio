/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *   	RootTreeNodeController class.
 *     
 */
package ca.sciencestudio.service.tree.controllers;

import javax.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import ca.sciencestudio.service.utilities.ModelPathUtils;

/**
 * @author maxweld
 *
 */
@Controller
public class RootTreeNodeController extends AbstractTreeNodeController {
	
	@ResponseBody
	@RequestMapping(value = "/root*", method = RequestMethod.GET)
	public TreeNodeList root(HttpServletRequest request) throws Exception {
		
		TreeNodeList treeNodes = new TreeNodeList();
		
		TreeNodeMap treeNode = new TreeNodeMap();
		treeNode.put(TREE_NODE_ID, "PROJECTS");
		treeNode.put(TREE_NODE_DATA_URL, getTreePath(request) + ModelPathUtils.getProjectsPath(""));
		treeNode.put(TREE_NODE_VIEW_URL, getModelPath(request) + ModelPathUtils.getProjectsPath(".html"));
		treeNode.put(TREE_NODE_ICON_CLASS, "ss-projects-tree-node-icon");
		treeNode.put(TREE_NODE_TEXT, "Projects");
		treeNodes.add(treeNode);
	
		return treeNodes;
	}
}
