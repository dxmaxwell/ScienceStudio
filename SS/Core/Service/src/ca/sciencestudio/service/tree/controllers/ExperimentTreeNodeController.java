/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *   	ExperimentTreeNodeController class.
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
import ca.sciencestudio.model.session.Experiment;
import ca.sciencestudio.model.session.dao.ExperimentDAO;
import ca.sciencestudio.security.util.AuthorityUtil;
import ca.sciencestudio.security.util.SecurityUtil;
import ca.sciencestudio.service.utilities.ModelPathUtils;

/**
 * @author maxweld
 *
 */
@Controller
public class ExperimentTreeNodeController extends AbstractTreeNodeController {

	@Autowired
	private ProjectDAO projectDAO;
	
	@Autowired
	private ExperimentDAO experimentDAO;
	
	@RequestMapping(value = "/session/{sessionId}/experiments.{format}", method = RequestMethod.GET)
	public String experiments(@PathVariable int sessionId, @PathVariable String format, HttpServletRequest request, ModelMap model) {
		
		TreeNodeList treeNodes = new TreeNodeList();
		model.put("treeNodes", treeNodes);
		
		Project project = projectDAO.getProjectBySessionId(sessionId);
		if(project == null) {
			return getResponseView(format);
		}
				
		Object admin = AuthorityUtil.ROLE_ADMIN_PROJECTS;
		Object group = AuthorityUtil.buildProjectGroupAuthority(project.getId());
		
		if(SecurityUtil.hasAnyAuthority(admin, group)) {
			List<Experiment> experimentList = experimentDAO.getExperimentListBySessionId(sessionId);
			for(Experiment experiment : experimentList) {
				TreeNodeMap treeNode = new TreeNodeMap();
				treeNode.put(TREE_NODE_ID, "EXPERIMENT_" + experiment.getId());
				treeNode.put(TREE_NODE_VIEW_URL, getModelPath(request) + ModelPathUtils.getExperimentPath(experiment.getId(), ".html"));
				treeNode.put(TREE_NODE_DATA_URL, getTreePath(request) + ModelPathUtils.getExperimentPath(experiment.getId(), ".json"));
				treeNode.put(TREE_NODE_ICON_CLASS, "ss-experiment-tree-node-icon");
				treeNode.put(TREE_NODE_TEXT, experiment.getName());
				treeNodes.add(treeNode);
			}
		}
		
		return getResponseView(format);
	}
	
	@RequestMapping(value = "/experiment/{experimentId}.{format}", method = RequestMethod.GET)
	public String experiment(@PathVariable int experimentId, @PathVariable String format, HttpServletRequest request, ModelMap model) {
	
		TreeNodeList treeNodes = new TreeNodeList();
		model.put("treeNodes", treeNodes);
		
		Project project = projectDAO.getProjectByExperimentId(experimentId);
		if(project == null) {
			return getResponseView(format);
		}
			
		Object admin = AuthorityUtil.ROLE_ADMIN_PROJECTS;
		Object group = AuthorityUtil.buildProjectGroupAuthority(project.getId());
		
		if(SecurityUtil.hasAnyAuthority(admin, group)) {
			TreeNodeMap treeNode = new TreeNodeMap();
			treeNode.put(TREE_NODE_ID, "EXPERIMENT_SCANS_" + experimentId);
			treeNode.put(TREE_NODE_VIEW_URL, getModelPath(request) + ModelPathUtils.getScansPath(experimentId, ".html"));
			treeNode.put(TREE_NODE_DATA_URL, getTreePath(request) + ModelPathUtils.getScansPath(experimentId, ".json"));
			treeNode.put(TREE_NODE_ICON_CLASS, "ss-experiment-scans-tree-node-icon");
			treeNode.put(TREE_NODE_TEXT, "Scans");
			treeNodes.add(treeNode);
		}
		
		return "treeNodes-json";
	}

	public ProjectDAO getProjectDAO() {
		return projectDAO;
	}
	public void setProjectDAO(ProjectDAO projectDAO) {
		this.projectDAO = projectDAO;
	}

	public ExperimentDAO getExperimentDAO() {
		return experimentDAO;
	}
	public void setExperimentDAO(ExperimentDAO experimentDAO) {
		this.experimentDAO = experimentDAO;
	}
}
