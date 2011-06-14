/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *     viewport-west.js
 *     
 */

function westPanel() {
	
	var treeRoot =  new Ext.tree.AsyncTreeNode({
		dataUrl:'/ss/tree/root'
	});
	
	var treeLoader = new Ext.tree.TreeLoader({
		dataUrl:'/will/be/replaced',
		requestMethod: 'GET',
		nodeParameter: ''
	});
	
	treeLoader.on('beforeload', function(loader, node) {		
		loader.dataUrl = node.attributes.dataUrl||loader.url;
    });
	
	var treePanel = new Ext.tree.TreePanel({
		region:'west',
		title:'Project Navigator',
		loader: treeLoader,
    	root: treeRoot,
		rootVisible:false,
		containerScroll:true,
		collapsible:true,
    	autoScroll:true,
    	enableDD:false,
    	animate:true,
    	split:true,
    	lines:false,
    	tools:[{
    		id:'refresh',
    		handler:treeReload,
    		scope:this
    	}],
		buttonAlign: 'left',
		margins:'0 0 4 4',
    	minWidth: 200,
    	width:200
    });
	
	treePanel.on('click', function(node, event) {
		node.select();
		if(node.attributes.viewUrl) {
			loadModelViewTab(node.attributes.viewUrl);
		}
	});
	
	function treeReload() {
		try {
			treeNodeReload(treePanel.getRootNode());
		}
		catch(ex) {
			/* Ignore */
		}
	};

	function treeNodeReload(node) {
		if(node && node.attributes.dataUrl && node.isExpanded()) {	
			Ext.Ajax.request({
				url: node.attributes.dataUrl,
				success: treeNodeReloadRequestSuccess,
				scope: this,
				node: node
			});
		}
	} 
	
	function treeNodeReloadRequestSuccess(response, options) {
		var node = options.node;
		
		var treeNodes = response.responseJson || Ext.decode(response.responseText);
		for(var idx = 0; idx < treeNodes.length; idx++) {
			var treeNode = treeNodes[idx];
			if(treeNode && treeNode.id) {
				var child = node.findChild('id', treeNode.id);
				if(child && treeNode.text) {
					if(child.text != treeNode.text) {
						child.setText(treeNode.text);
					}
				}
				else {					
					var loader = node.loader||node.getOwnerTree().getLoader();
					if(loader) {
						node.appendChild(loader.createNode(treeNode));
					}
				}
			}
		}
		
		node.eachChild(treeNodeReload, this);
	}
	
	Ext.TaskMgr.start({
		run: treeReload,
		interval: 30000,
		scope: this
	});
	
	return treePanel;
}
