/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *   	ExperimentTreeNodeController class.
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

import ca.sciencestudio.model.session.Experiment;
import ca.sciencestudio.model.session.dao.ExperimentAuthzDAO;
import ca.sciencestudio.security.util.SecurityUtil;
import ca.sciencestudio.service.utilities.ModelPathUtils;

/**
 * @author maxweld
 *
 */
@Controller
public class ExperimentTreeNodeController extends AbstractTreeNodeController {


	private ExperimentAuthzDAO experimentAuthzDAO;
	
	@ResponseBody
	@RequestMapping(value = ModelPathUtils.EXPERIMENT_PATH + "*", method = RequestMethod.GET, params = "session")
	public TreeNodeList experiments(@RequestParam("session") String sessionGid) {
		
		String user = SecurityUtil.getPersonGid();
		
		TreeNodeList treeNodes = new TreeNodeList();
		
		List<Experiment> experimentList = experimentAuthzDAO.getAllBySessionGid(user, sessionGid).get();
		
		for(Experiment experiment : experimentList) {
			TreeNodeMap treeNode = new TreeNodeMap();
			treeNode.put(TREE_NODE_ID, "EXPERIMENT_" + experiment.getGid());

			treeNode.put(TREE_NODE_DATA_URL, ModelPathUtils.getTreeExperimentPath("/", experiment.getGid(), ".json"));
			treeNode.put(TREE_NODE_VIEW_URL, ModelPathUtils.getModelExperimentPath("/", experiment.getGid(), ".html"));
			treeNode.put(TREE_NODE_ICON_CLASS, "ss-experiment-tree-node-icon");
			treeNode.put(TREE_NODE_TEXT, experiment.getName());
			treeNodes.add(treeNode);
		}

		return treeNodes;
	}
	
	@ResponseBody
	@RequestMapping(value = ModelPathUtils.EXPERIMENT_PATH + "/{experimentGid}*", method = RequestMethod.GET)
	public TreeNodeList experiment(@PathVariable String experimentGid) {
			
		TreeNodeList treeNodes = new TreeNodeList();
		
		// No restricted content ahead, so no security check required.
		
		TreeNodeMap treeNode = new TreeNodeMap();
		treeNode.put(TREE_NODE_ID, "EXPERIMENT_SCANS_" + experimentGid);
		treeNode.put(TREE_NODE_DATA_URL, ModelPathUtils.getTreeScanPath(".json", "?experiment=", experimentGid));
		treeNode.put(TREE_NODE_VIEW_URL, ModelPathUtils.getModelScanPath(".html", "?experiment=", experimentGid));
		treeNode.put(TREE_NODE_ICON_CLASS, "ss-experiment-scans-tree-node-icon");
		treeNode.put(TREE_NODE_TEXT, "Scans");
		treeNodes.add(treeNode);
			
		return treeNodes;
	}

	public ExperimentAuthzDAO getExperimentAuthzDAO() {
		return experimentAuthzDAO;
	}
	public void setExperimentAuthzDAO(ExperimentAuthzDAO experimentAuthzDAO) {
		this.experimentAuthzDAO = experimentAuthzDAO;
	}
}
