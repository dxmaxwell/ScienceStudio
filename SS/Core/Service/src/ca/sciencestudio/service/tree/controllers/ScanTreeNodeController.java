/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *   	ScanTreeNodeController class.
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

import ca.sciencestudio.model.project.Project;
import ca.sciencestudio.model.project.dao.ProjectDAO;
import ca.sciencestudio.model.session.Scan;
import ca.sciencestudio.model.session.dao.ScanDAO;
import ca.sciencestudio.security.util.AuthorityUtil;
import ca.sciencestudio.security.util.SecurityUtil;
import ca.sciencestudio.service.utilities.ModelPathUtils;

/**
 * @author maxweld
 *
 */
@Controller
public class ScanTreeNodeController extends AbstractTreeNodeController {
	
	@Autowired
	private ScanDAO scanDAO;
	
	@Autowired
	private ProjectDAO projectDAO;
	
	@RequestMapping(value = "/experiment/{experimentId}/scans.{format}", method = RequestMethod.GET)
	public String scans(@PathVariable int experimentId, @PathVariable String format, HttpServletRequest request, ModelMap model) {
		
		TreeNodeList treeNodes = new TreeNodeList();
		model.put("treeNodes", treeNodes);
		
		Project project = projectDAO.getProjectByExperimentId(experimentId);
		if(project == null) {
			return getResponseView(format);
		}
				
		Object admin = AuthorityUtil.ROLE_ADMIN_PROJECTS;
		Object team = AuthorityUtil.buildProjectGroupAuthority(project.getId());
		
		if(SecurityUtil.hasAnyAuthority(admin, team)) {
			List<Scan> scans = scanDAO.getScanListByExperimentId(experimentId);
			for(Scan scan : scans) {
				TreeNodeMap treeNode = new TreeNodeMap();
				treeNode.put(TREE_NODE_ID, "SCAN_" + scan.getId());
				treeNode.put(TREE_NODE_VIEW_URL, getModelPath(request) + ModelPathUtils.getScanPath(scan.getId(), ".html"));
				treeNode.put(TREE_NODE_ICON_CLASS, "ss-scan-tree-node-icon");
				treeNode.put(TREE_NODE_TEXT, scan.getName());
				treeNode.put(TREE_NODE_LEAF, true);
				treeNodes.add(treeNode);
			}
		}

		return getResponseView(format);
	}

	public ScanDAO getScanDAO() {
		return scanDAO;
	}
	public void setScanDAO(ScanDAO scanDAO) {
		this.scanDAO = scanDAO;
	}

	public ProjectDAO getProjectDAO() {
		return projectDAO;
	}
	public void setProjectDAO(ProjectDAO projectDAO) {
		this.projectDAO = projectDAO;
	}
}
