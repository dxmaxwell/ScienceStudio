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
