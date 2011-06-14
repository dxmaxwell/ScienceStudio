/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *   	AbstractTreeNodeController class.
 *     
 */
package ca.sciencestudio.service.tree.controllers;

import java.util.HashMap;
import java.util.LinkedList;

import javax.servlet.http.HttpServletRequest;

import ca.sciencestudio.security.util.SecurityUtil;

/**
 * @author maxweld
 *
 */
public abstract class AbstractTreeNodeController {

	protected static final String TREE_NODE_ID = "id";
	protected static final String TREE_NODE_DATA_URL = "dataUrl";
	protected static final String TREE_NODE_TEXT = "text";
	protected static final String TREE_NODE_CLASS = "cls";
	protected static final String TREE_NODE_ICON_CLASS = "iconCls";
	protected static final String TREE_NODE_LEAF = "leaf";
	protected static final String TREE_NODE_VIEW_URL = "viewUrl";
	protected static final String TREE_NODE_VIEW_TARGET = "viewTarget";
	
	protected static final String RESPONSE_VIEW = "treeNodes-";
	
	private String modelServletPath;
	
	protected String getPersonUid() {
		return SecurityUtil.getPerson().getGid();
	}
	
	protected String getResponseView(String format) {
		return RESPONSE_VIEW + format;
	}
	
	protected String getTreePath(HttpServletRequest request) {
		return request.getContextPath() + request.getServletPath();
	}
	
	protected String getModelPath(HttpServletRequest request) {
		return request.getContextPath() + getModelServletPath();
	}
	
	public String getModelServletPath() {
		return modelServletPath;
	}
	public void setModelServletPath(String modelServletPath) {
		this.modelServletPath = modelServletPath;
	}
	
	protected static class TreeNodeMap extends HashMap<String,Object> {
		
		private static final long serialVersionUID = 1L;

		public TreeNodeMap() {
			super();
		}
	}
	
	protected static class TreeNodeList extends LinkedList<TreeNodeMap> {
		
		private static final long serialVersionUID = 1L;

		public TreeNodeList() {
			super();
		}
	}
}
