/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *   	ScanTreeNodeController class.
 *     
 */
package ca.sciencestudio.service.tree.controllers;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import ca.sciencestudio.model.session.Scan;
import ca.sciencestudio.model.session.dao.ScanAuthzDAO;
import ca.sciencestudio.security.util.SecurityUtil;
import ca.sciencestudio.service.utilities.ModelPathUtils;

/**
 * @author maxweld
 *
 */
@Controller
public class ScanTreeNodeController extends AbstractTreeNodeController {
	
	private ScanAuthzDAO scanAuthzDAO;
	
	@ResponseBody
	@RequestMapping(value = ModelPathUtils.SCAN_PATH + "*", method = RequestMethod.GET, params = "experiment")
	public TreeNodeList scans(@RequestParam("experiment") String experimentGid) {
		
		String user = SecurityUtil.getPersonGid();
		
		TreeNodeList treeNodes = new TreeNodeList();
		
		List<Scan> scans = scanAuthzDAO.getAllByExperimentGid(user, experimentGid).get();
		
		for(Scan scan : scans) {
			TreeNodeMap treeNode = new TreeNodeMap();
			treeNode.put(TREE_NODE_ID, "SCAN_" + scan.getGid());
			treeNode.put(TREE_NODE_VIEW_URL, ModelPathUtils.getModelScanPath("/", scan.getGid(), ".html"));
			treeNode.put(TREE_NODE_ICON_CLASS, "ss-scan-tree-node-icon");
			treeNode.put(TREE_NODE_TEXT, scan.getName());
			treeNode.put(TREE_NODE_LEAF, true);
			treeNodes.add(treeNode);
		}
		
		return treeNodes;
	}

	public ScanAuthzDAO getScanAuthzDAO() {
		return scanAuthzDAO;
	}
	public void setScanAuthzDAO(ScanAuthzDAO scanAuthzDAO) {
		this.scanAuthzDAO = scanAuthzDAO;
	}
}
