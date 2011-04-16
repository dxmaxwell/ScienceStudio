/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *   SessionTreeNodeController class.
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

import ca.sciencestudio.model.session.Session;
import ca.sciencestudio.model.session.dao.SessionDAO;
import ca.sciencestudio.security.util.AuthorityUtil;
import ca.sciencestudio.security.util.SecurityUtil;
import ca.sciencestudio.service.utilities.ModelPathUtils;

/**
 * @author maxweld
 *
 */
@Controller
public class SessionTreeNodeController extends AbstractTreeNodeController {
	
	@Autowired
	private SessionDAO sessionDAO;
	
	@RequestMapping(value = "project/{projectId}/sessions.{format}", method = RequestMethod.GET)
	public String sessions(@PathVariable int projectId, @PathVariable String format, HttpServletRequest request, ModelMap model) {

		TreeNodeList treeNodes = new TreeNodeList();
		
		Object admin = AuthorityUtil.ROLE_ADMIN_PROJECTS;
		Object group = AuthorityUtil.buildProjectGroupAuthority(projectId);
		
		if(SecurityUtil.hasAnyAuthority(admin, group)) {
					
			List<Session> sessions = sessionDAO.getSessionListByProjectId(projectId);
			for(Session session : sessions) {
				TreeNodeMap treeNode = new TreeNodeMap();
				treeNode.put(TREE_NODE_ID, "SESSION_" + session.getId());
				treeNode.put(TREE_NODE_VIEW_URL, getModelPath(request) + ModelPathUtils.getSessionPath(session.getId(), ".html"));
				treeNode.put(TREE_NODE_DATA_URL, getTreePath(request) + ModelPathUtils.getSessionPath(session.getId(), ".json"));
				treeNode.put(TREE_NODE_ICON_CLASS, "ss-session-tree-node-icon");
				treeNode.put(TREE_NODE_TEXT, session.getName());
				treeNodes.add(treeNode);
			}
		}
		
		model.put("treeNodes", treeNodes);
		return getResponseView(format);
	}
	
	@RequestMapping(value = "/session/{sessionId}.{format}", method = RequestMethod.GET)
	public String session(@PathVariable int sessionId, @PathVariable String format, HttpServletRequest request, ModelMap model) {
		
		TreeNodeList treeNodes = new TreeNodeList();
		
		Session session = sessionDAO.getSessionById(sessionId);
		if((session != null)) {
		
			Object admin = AuthorityUtil.ROLE_ADMIN_PROJECTS;
			Object group = AuthorityUtil.buildProjectGroupAuthority(session.getProjectId());
			
			if(SecurityUtil.hasAnyAuthority(admin, group)) {
				TreeNodeMap treeNode = new TreeNodeMap();
				treeNode.put(TREE_NODE_ID, "SESSION_EXPERIMENTS_" + sessionId);
				treeNode.put(TREE_NODE_VIEW_URL, getModelPath(request) + ModelPathUtils.getExperimentsPath(sessionId, ".html"));
				treeNode.put(TREE_NODE_DATA_URL, getTreePath(request) + ModelPathUtils.getExperimentsPath(sessionId, ".json"));
				treeNode.put(TREE_NODE_ICON_CLASS, "ss-session-experiments-tree-node-icon");
				treeNode.put(TREE_NODE_TEXT, "Experiments");
				treeNodes.add(treeNode);
			}
		}
		
		model.put("treeNodes", treeNodes);
		return getResponseView(format);
	}

	public SessionDAO getSessionDAO() {
		return sessionDAO;
	}
	public void setSessionDAO(SessionDAO sessionDAO) {
		this.sessionDAO = sessionDAO;
	}
}
