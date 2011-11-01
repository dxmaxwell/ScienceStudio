<%-- 
	Copyright (c) Canadian Light Source, Inc. All rights reserved.
	- see license.txt for details.
	
	Description:
		Scans html fragment.
--%>
<%@ page language="java"  contentType="text/xml; charset=UTF-8" pageEncoding="UTF-8" trimDirectiveWhitespaces="true"%>
<%@ include file="/WEB-INF/jsp/include/taglibs.jsp"%>
<div>
<script type="text/javascript">
Ext.onReady(function() {
<%--

	Navigation Trail
--%>
	var navigationPanel = new Ext.Panel({
		layout:{
			type:'hbox',
			align:'middle',
			defaultMargins:'2px'
		},
		items: [{
			xtype:"box",
			autoEl:{
				tag:"img",
				height:"20px",
				src:"/ssstatic/img/nav/home.png",
				onclick:"return activateHomeTab();"
			}
		},{
			xtype:"box",
			html: "&raquo;"
		},{
			xtype:"box",
			autoEl:{
				tag: "a",
				href: "#projects",
				onclick: "return loadModelViewTab(ModelPathUtils.getModelProjectPath('.html'));",
				html: "Projects"
			}
		},{
			xtype:"box",
			html: "&raquo;"
		},{
			xtype:"box",
			autoEl:{
				tag: "a",
				href:"#project${project.gid}",
				onclick: "return loadModelViewTab(ModelPathUtils.getProjectPath('/${project.gid}.html'));",
				html:"${project.name}"
			}
		},{
			xtype:"box",
			html: "&raquo;"
		},{
			xtype:"box",
			autoEl:{
				tag: "a",
				href:"#sessions${project.gid}",
				onclick: "return loadModelViewTab(ModelPathUtils.getModelSessionPath('.html?proejct=${project.gid}'));",
				html:"Sessions"
			}
		},{
			xtype:"box",
			html: "&raquo;"
		},{
			xtype:"box",
			autoEl:{
				tag: "a",
				href:"#session${session.gid}",
				onclick: "return loadModelViewTab(ModelPathUtils.getModelSessionPath('/${session.gid}.html'));",
				html:"${session.name}"
			}
		},{
			xtype:"box",
			html: "&raquo;"
		},{
			xtype:"box",
			autoEl:{
				tag: "a",
				href:"#experiments${session.gid}",
				onclick: "return loadModelViewTab(ModelPathUtils.getModelExperimentPath('.html?session=${session.gid}'));",
				html:"Experiments"
			}
		},{
			xtype:"box",
			html: "&raquo;"
		},{
			xtype:"box",
			autoEl:{
				tag: "a",
				href:"#experiment${experiment.gid}",
				onclick: "return loadModelViewTab(ModelPathUtils.getModelExperimentPath('/${experiment.gid}.html'));",
				html:"${experiment.name}"
			}
		},{
			xtype:"box",
			html: "&raquo;"
		},{
			xtype:"box",
			html: "Scans"
		}],
		bodyCssClass:"ss-navigation-trail"
	});
	
	addItemModelViewTab(navigationPanel);
<%--

	Scans Grid
--%>
	var scansGridStore = new Ext.data.JsonStore({
		url: ModelPathUtils.getModelScanPath('.json?experiment=${experiment.gid}'),
		autoDestroy: true,
		autoLoad: false,
		fields:[{
			name:"gid"
		},{
			name:"name"
		},{
			name:"startDate", type:"date", dateFormat:"c"
		},{
			name:"endDate", type:"date", dateFormat:"c"
		}]
	});

	var scansGridStoreReload = function() {
		if(scansGridStore) {
			scansGridStore.reload();
		}
	};
	
	var scansGridPanel = new Ext.grid.GridPanel({
		store:scansGridStore,
		deferRowRender: false,
		viewConfig:{
			forceFit:true
		},
		columns: [{
			header: "GID", width:50, dataIndex:"gid", sortable:true
		},{
			header: "Name", width: 180, dataIndex: 'name', sortable: true
		},{
			header: "Start", width: 80, dataIndex: 'startDate', sortable: true,
			xtype:'datecolumn', format:Date.patterns.ISO8601Shrt
		},{
			header: "End", width: 80, dataIndex: 'endDate', sortable: true,
			xtype:'datecolumn', format:Date.patterns.ISO8601Shrt
		}],
		border:false,
		height:400
	});

	scansGridPanel.on('rowclick', function(grid, index, event) {
		var record = grid.getStore().getAt(index);
		if(record) {
			var gid = record.get("gid");
			if(gid) {
				loadModelViewTab(ModelPathUtils.getModelScanPath(['/', gid, '.html']));
			}
		}
	}, this);

	var scansGridStoreReloadTask = {
		run:scansGridStoreReload,
		interval:60000,
		scope:this
	}

	scansGridPanel.on("afterrender", function() {
		Ext.TaskMgr.start(scansGridStoreReloadTask);
	}, this);

	scansGridPanel.on("desstroy", function() {
		Ext.TaskMgr.stop(scansGridStoreReloadTask);
	}, this);

	var scansPanel = new Ext.Panel({
		title:"Scans",
		tools:[{
			id:'refresh',
			handler:scansGridStoreReload,
			scope:this
		}],
		items:[
			scansGridPanel
		],
		style:{
			"margin":"10px"
		},
		width:600
	});

	addItemModelViewTab(scansPanel, true);
});
</script>
</div>
