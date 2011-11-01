/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *  	SampleTreeNodeController class.
 *     
 */
package ca.sciencestudio.service.tree.controllers;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import ca.sciencestudio.model.sample.Sample;
import ca.sciencestudio.model.sample.dao.SampleAuthzDAO;
import ca.sciencestudio.security.util.SecurityUtil;
import ca.sciencestudio.service.utilities.ModelPathUtils;

/**
 * @author maxweld
 *
 */
@Controller
public class SampleTreeNodeController extends AbstractTreeNodeController {

	private SampleAuthzDAO sampleAuthzDAO;
	
	@ResponseBody
	@RequestMapping(value = ModelPathUtils.SAMPLE_PATH + "*", method = RequestMethod.GET)
	public TreeNodeList samples(@RequestParam("project") String projectGid) {
		
		String user = SecurityUtil.getPersonGid();
		
		TreeNodeList treeNodes = new TreeNodeList();
		
		List<Sample> samples = sampleAuthzDAO.getAllByProjectGid(user, projectGid).get();
			
		for(Sample sample : samples) {
			TreeNodeMap treeNode = new TreeNodeMap();
			treeNode.put(TREE_NODE_ID, "SAMPLE_" + sample.getGid());
			treeNode.put(TREE_NODE_VIEW_URL, ModelPathUtils.getModelSamplePath("/", sample.getGid(), ".html"));
			treeNode.put(TREE_NODE_ICON_CLASS, "ss-sample-tree-node-icon");
			treeNode.put(TREE_NODE_TEXT, sample.getName());
			treeNode.put(TREE_NODE_LEAF, true);
			treeNodes.add(treeNode);
		}
		
		return treeNodes;
	}

	public SampleAuthzDAO getSampleAuthzDAO() {
		return sampleAuthzDAO;
	}
	public void setSampleAuthzDAO(SampleAuthzDAO sampleAuthzDAO) {
		this.sampleAuthzDAO = sampleAuthzDAO;
	}
}
