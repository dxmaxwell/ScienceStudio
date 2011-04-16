<%-- 
	Copyright (c) Canadian Light Source, Inc. All rights reserved.
	- see license.txt for details.
	
	Description:
		Home HTML fragment.
--%>
<%@ page language="java" contentType="text/xml; charset=UTF-8" pageEncoding="UTF-8" trimDirectiveWhitespaces="true"%>
<%@ include file="/WEB-INF/jsp/include/taglibs.jsp"%>
<div>
<script type="text/javascript">
Ext.onReady(function() {
	
	var projectsGridStore = new Ext.data.JsonStore({
		url: '/ss/model/projects.json',
		autoDestroy:true,
		autoLoad:false,
		root:"response",
		success: "success",
		fields:[{
			name:"id", mapping:"project.id"
		},{
			name:"name", mapping:"project.name"
		},{
			name:"startDate", mapping:"project.startDate",
			type:"date", dateFormat:Date.patterns.ISO8601Full
		},{
			name:"endDate", mapping:"project.endDate",
			type:"date", dateFormat:Date.patterns.ISO8601Full
		},{
			name:"status", mapping:"project.status"
		}]
	});

	function projectsGridStoreReload() {
		if(projectsGridStore) {
			projectsGridStore.reload();
		}
	}
	
	var projectsGridPanel = new Ext.grid.GridPanel({
		store:projectsGridStore,
		deferRowRender: false,
		viewConfig:{
			forceFit:true
		},
		columns: [{
			header: "Id", width:30, dataIndex:"id", sortable:true
		},{
			header: "Name", width: 180, dataIndex: 'name', sortable: true
		},{
			header: "Start", width: 85, dataIndex: 'startDate', sortable: true,
			xtype:'datecolumn', format:Date.patterns.ISO8601Date 
		},{
			header: "End", width: 85, dataIndex: 'endDate', sortable: true,
			xtype:'datecolumn', format:Date.patterns.ISO8601Date
		},{
			header: "Status", width: 50, dataIndex: 'status', sortable: false
		}],
		border:false,
		flex:1
	});

	projectsGridPanel.on('rowclick', function(grid, index, event) {
		var sm = grid.getSelectionModel();
		if(sm && sm.deselectRow) {
			sm.deselectRow(index);
		}

		var record = grid.getStore().getAt(index);
		if(record) {
			var projectId = record.get("id");
			if(projectId) {
				loadModelViewTab(ModelPathUtils.getProjectPath(projectId, '.html'));
			}
		}
	}, this);

	var projectsGridReloadTask = {
	    run: projectsGridStoreReload,
	    scope: this,
		interval:60000
	};

	Ext.TaskMgr.start(projectsGridReloadTask);
	
	var projectsPanel = new Ext.Panel({
		layout:{
			type:'vbox',
			align:'stretch'
		},
		items:[
			projectsGridPanel
		],
		height:200
	});

	var sessionsGridStore = new Ext.data.JsonStore({
		url: '/ss/model/sessions/grid.json',
		autoDestroy:true,
		autoLoad:false,
		root:"response",
		success: "success",
		fields:[{
			name:"sessionId", mapping:"sessionGridBacker.sessionId"
		},{
			name:"sessionName", mapping:"sessionGridBacker.sessionName"
		},{
			name:"projectName", mapping:"sessionGridBacker.projectName"
		},{
			name:"proposal", mapping:"sessionGridBacker.proposal"
		},{
			name:"startDate", mapping:"sessionGridBacker.startDate",
			type:"date", dateFormat:Date.patterns.ISO8601Shrt
		},{
			name:"endDate", mapping:"sessionGridBacker.endDate",
			type:"date", dateFormat:Date.patterns.ISO8601Shrt
		}]
	});

	function sessionsGridStoreReload() {
		if(sessionsGridStore) {
			sessionsGridStore.reload();
		}
	}

	var sessionsGridPanel = new Ext.grid.GridPanel({
		store:sessionsGridStore,
		deferRowRender: false,
		viewConfig:{
			forceFit:true
		},
		columns: [{
			header: "Id", width:30, dataIndex:"sessionId", sortable:true
		},{
			header: "Name", width: 180, dataIndex: 'sessionName', sortable: true
		},{
			header: "Project", width: 180, dataIndex: 'projectName', sortable: true
		},{
			header: "Proposal", width: 180, dataIndex: 'proposal', sortable: true
		},{
			header: "Start", width: 85, dataIndex: 'startDate', sortable: true,
			xtype:'datecolumn', format:Date.patterns.ISO8601Shrt 
		},{
			header: "End", width: 85, dataIndex: 'endDate', sortable: true,
			xtype:'datecolumn', format:Date.patterns.ISO8601Shrt
		}],
		border:false,
		flex:1
	});

	sessionsGridPanel.on('rowclick', function(grid, index, event) {
		var sm = grid.getSelectionModel();
		if(sm && sm.deselectRow) {
			sm.deselectRow(index);
		}

		var record = grid.getStore().getAt(index);
		if(record) {
			var sessionId = record.get("sessionId");
			if(sessionId) {
				loadModelViewTab(ModelPathUtils.getSessionPath(sessionId, '.html'));
			}
		}
	}, this);

	var sessionsGridReloadTask = {
	    run: sessionsGridStoreReload,
	    scope: this,
		interval:60000
	};

	Ext.TaskMgr.start(sessionsGridReloadTask);

	var sessionsPanel = new Ext.Panel({
		layout:{
			type:'vbox',
			align:'stretch'
		},
		items:[
			sessionsGridPanel
		],
		height:300
	});

	var datePickerPanel = new Ext.DatePicker({
		// May need some configuraiton in the future? //
	});
	
	var skypePanel = new Ext.BoxComponent({
		autoEl: {
			tag: 'a',
			href: 'skype:echo123?call',
			cn: [{
				tag: 'img',
				alt: 'Skype Me!',
				src: 'http://download.skype.com/share/skypebuttons/buttons/call_blue_white_124x52.png',
				style: 'border:none;width:124px;height:52px;'
			}]
		}
	});
	
	var gTalkPanel = new Ext.BoxComponent({
		autoEl: {
			tag: 'a',
			href: "javascript:void(0);",
			cn:[{
				tag: 'img',
				src: 'http://www.google.com/talk/images/gadget_popout.gif',
				onclick: "open('http://talkgadget.google.com/talkgadget/popout','gtalk','width=300,height=445,toolbar=0,status=0,menubar=0,location=0,resizable=1,scrollbars=0');"
			}]
		}
	});
	
	<sec:authorize ifAnyGranted="ROLE_ADMIN_PROJECTS">
	var projectAdminPanel = new Ext.Panel({
		layout:{
			type:'hbox'
		},
		items:[{
			xtype:'box',
			autoEl:{
				tag:'div',
				cn:[{
					tag: 'a',
					href:'#projects',
					html:'Show Projects',
					onclick:"return loadModelViewTab(ModelPathUtils.getProjectsPath('.html'));"
				}]
			},
			margins:'0px 5px'
		},{
			xtype:'box',
			autoEl:{
				tag:'div',
				cn:[{
					tag:'a',
					href:'#projects',
					html:'Add Project',
					onclick:"return loadModelViewTab(ModelPathUtils.getProjectsPath('.html'));"
				}]
			},
			margins:'0px 5px'
		}]
	});
	</sec:authorize>

	var instrumentAdminPanel = new Ext.Panel({
		items: [{ xtype:'box' }
	<sec:authorize ifAnyGranted="ROLE_ADMIN_VESPERS">
	<%-- Vespers Simulation --%>
	<%--	,{
				xtype: 'box',
				autoEl: {
					tag:'span',
					html:'&nbsp;&nbsp;'
				}
			},{
				xtype: 'box',
				autoEl: {
					tag: 'a',
					target: '_BLANK',
					href: '/ssvespers/simadmin/main.html',
					html: 'VESPERS Simulation'
				}
			} 				--%>
	<%-- ================== --%>
	</sec:authorize>
		]
	});
	
	var portalPanel = new Ext.ux.Portal({
		items:[{
			columnWidth: 0.8,
			defaults: {
				style: 'padding: 5px 0px'
			},
			items:[{
				title:'Projects',
				tools:[{
					id:'refresh',
					handler:projectsGridStoreReload,
					scope:this
				}],	
				items:[
					projectsPanel
				]
			},{
				title:'Sessions',
				tools:[{
					id:'refresh',
					handler:sessionsGridStoreReload,
					scope:this
				}],
				items:[
					sessionsPanel
				]
			}],
			style:'padding:5px;'
		},{
			columnWidth: 0.2,
			defaults: {
				style: 'padding: 5px 0px'
			},
			items: [{
				title: 'Calendar',
				items:[ datePickerPanel ]
			},{
				title:'Skype Gadget',
				items:[ skypePanel ]
			},{
				title:'GTalk Gadget',
				items:[ gTalkPanel ]
			}
	<sec:authorize ifAnyGranted="ROLE_ADMIN_PROJECTS">
			,{
				title:'Project Administration',
				items:[ projectAdminPanel ]
			}
	</sec:authorize>
	<sec:authorize ifAnyGranted="ROLE_ADMIN_VESPERS">
			,{
				title:'Instrument Administration',
				items:[ instrumentAdminPanel ]
			}
	</sec:authorize>
			],
			style:'padding:5px;'
		}],
		border:false
	});
	
	addItemHomeTab(portalPanel, true);	
});
</script>
</div>
