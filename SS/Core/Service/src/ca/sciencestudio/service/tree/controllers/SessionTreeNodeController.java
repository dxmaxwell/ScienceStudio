/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *  	SessionTreeNodeController class.
 *     
 */
package ca.sciencestudio.service.tree.controllers;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import ca.sciencestudio.model.session.Session;
import ca.sciencestudio.model.session.dao.SessionAuthzDAO;
import ca.sciencestudio.security.util.SecurityUtil;
import ca.sciencestudio.service.utilities.ModelPathUtils;

/**
 * @author maxweld
 *
 */
@Controller
public class SessionTreeNodeController extends AbstractTreeNodeController {
	
	private SessionAuthzDAO sessionAuthzDAO;
	
	@ResponseBody
	@RequestMapping(value = ModelPathUtils.SESSION_PATH + "*", method = RequestMethod.GET)
	public TreeNodeList sessions(@RequestParam("project") String projectGid) {

		String user = SecurityUtil.getPersonGid();
		
		TreeNodeList treeNodes = new TreeNodeList();
		
		List<Session> sessions = sessionAuthzDAO.getAllByProjectGid(user, projectGid).get();
		for(Session session : sessions) {
			TreeNodeMap treeNode = new TreeNodeMap();
			treeNode.put(TREE_NODE_ID, "SESSION_" + session.getGid());
			treeNode.put(TREE_NODE_DATA_URL, ModelPathUtils.getTreeSessionPath("/", session.getGid(), ".json"));
			treeNode.put(TREE_NODE_VIEW_URL, ModelPathUtils.getModelSessionPath("/", session.getGid(), ".html"));
			treeNode.put(TREE_NODE_ICON_CLASS, "ss-session-tree-node-icon");
			treeNode.put(TREE_NODE_TEXT, session.getName());
			treeNodes.add(treeNode);
		}
		
		return treeNodes;
	}
	
	@ResponseBody
	@RequestMapping(value = ModelPathUtils.SESSION_PATH + "/{sessionGid}*", method = RequestMethod.GET)
	public TreeNodeList session(@PathVariable String sessionGid) {
		
		String user = SecurityUtil.getPersonGid();
		
		TreeNodeList treeNodes = new TreeNodeList();
		
		Session session = sessionAuthzDAO.get(user, sessionGid).get();
		if((session != null)) {
		
			// Session Team
			TreeNodeMap treeNode = new TreeNodeMap();
			treeNode.put(TREE_NODE_ID, "SESSION_PERSONS_" + sessionGid);
			treeNode.put(TREE_NODE_DATA_URL, ModelPathUtils.getTreeSessionPersonPath(".json", "?session=", sessionGid));
			treeNode.put(TREE_NODE_VIEW_URL, ModelPathUtils.getModelSessionPersonPath(".html", "?session=", sessionGid));
			treeNode.put(TREE_NODE_ICON_CLASS, "ss-session-persons-tree-node-icon");
			treeNode.put(TREE_NODE_TEXT, "Team");
			treeNodes.add(treeNode);
			
			// Experiments
			treeNode = new TreeNodeMap();
			treeNode.put(TREE_NODE_ID, "SESSION_EXPERIMENTS_" + sessionGid);
			treeNode.put(TREE_NODE_DATA_URL, ModelPathUtils.getTreeExperimentPath(".json", "?session=", sessionGid));
			treeNode.put(TREE_NODE_VIEW_URL, ModelPathUtils.getModelExperimentPath(".html", "?session=", sessionGid));
			treeNode.put(TREE_NODE_ICON_CLASS, "ss-session-experiments-tree-node-icon");
			treeNode.put(TREE_NODE_TEXT, "Experiments");
			treeNodes.add(treeNode);
		}
		
		return treeNodes;
	}

	public SessionAuthzDAO getSessionAuthzDAO() {
		return sessionAuthzDAO;
	}
	public void setSessionAuthzDAO(SessionAuthzDAO sessionAuthzDAO) {
		this.sessionAuthzDAO = sessionAuthzDAO;
	}
}
