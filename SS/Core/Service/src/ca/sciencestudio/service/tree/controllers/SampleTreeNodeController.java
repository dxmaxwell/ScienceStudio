/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *   SampleTreeNodeController class.
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

import ca.sciencestudio.model.sample.Sample;
import ca.sciencestudio.model.sample.dao.SampleDAO;
import ca.sciencestudio.security.util.AuthorityUtil;
import ca.sciencestudio.security.util.SecurityUtil;
import ca.sciencestudio.service.utilities.ModelPathUtils;

/**
 * @author maxweld
 *
 */
@Controller
public class SampleTreeNodeController extends AbstractTreeNodeController {

	@Autowired
	private SampleDAO sampleDAO;
	
	@RequestMapping(value = "project/{projectId}/samples.json", method = RequestMethod.GET)
	public String samples(@PathVariable int projectId, HttpServletRequest request, ModelMap model) {
		
		TreeNodeList treeNodes = new TreeNodeList();
		
		Object admin = AuthorityUtil.ROLE_ADMIN_PROJECTS;
		Object group = AuthorityUtil.buildProjectGroupAuthority(projectId);
		
		if(SecurityUtil.hasAnyAuthority(admin, group)) {
			
			List<Sample> samples = sampleDAO.getSampleListByProjectId(projectId);
			
			for(Sample sample : samples) {
				TreeNodeMap treeNode = new TreeNodeMap();
				treeNode.put(TREE_NODE_ID, "SAMPLE_" + sample.getId());
				treeNode.put(TREE_NODE_VIEW_URL, getModelPath(request) + ModelPathUtils.getSamplePath(sample.getId(), ".html"));
				treeNode.put(TREE_NODE_ICON_CLASS, "ss-sample-tree-node-icon");
				treeNode.put(TREE_NODE_TEXT, sample.getName());
				treeNode.put(TREE_NODE_LEAF, true);
				treeNodes.add(treeNode);
			}	
		}
		
		model.put("treeNodes", treeNodes);
		return "treeNodes-json";
	}

	public SampleDAO getSampleDAO() {
		return sampleDAO;
	}
	public void setSampleDAO(SampleDAO sampleDAO) {
		this.sampleDAO = sampleDAO;
	}
}
