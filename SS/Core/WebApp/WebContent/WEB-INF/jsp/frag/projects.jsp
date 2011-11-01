<%-- 
	Copyright (c) Canadian Light Source, Inc. All rights reserved.
	- see license.txt for details.
	
	Description:
		Projects html fragment.
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
			html: "Projects"
		}],
		bodyCssClass:"ss-navigation-trail"
	});

	addItemModelViewTab(navigationPanel);
<%--

	Projects Grid
--%>
	var projectsGridStore = new Ext.data.JsonStore({
		url: ModelPathUtils.getModelProjectPath(".json"),
		//baseParams:{ 'role':'admin' },
		autoDestroy: true,
		autoLoad: false,
		fields:[{
			name:"gid"
		},{
			name:"name"
		},{
			name:"startDate", type:"date", dateFormat:"c"
		},{
			name:"endDate",	type:"date", dateFormat:"c"
		},{
			name:"status"
		}]
	});

	var projectsGridStoreReload = function() {
		if(projectsGridStore) {
			projectsGridStore.reload();
		}
	};

	var projectsGridPanel = new Ext.grid.GridPanel({
		store: projectsGridStore,
		deferRowRender: false,
		viewConfig:{
			forceFit:true
		},
		columns: [{
			header: "GID", width:50, dataIndex:"gid", sortable:true
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
		var record = grid.getStore().getAt(index);
		if(record) {
			var projectGid = record.get("gid");
			if(projectGid) {
				loadModelViewTab(ModelPathUtils.getModelProjectPath(['/', projectGid, '.html']));
			}
		}
	}, this);

	var projectsGridStoreReloadTask = {
		run:projectsGridStoreReload,
		scope:this,
		interval:30000
	}

	Ext.TaskMgr.start(projectsGridStoreReloadTask);

	projectsGridPanel.on("destroy", function() {
		Ext.TaskMgr.stop(projectsGridStoreReloadTask);
	});

	var projectsPanel = new Ext.Panel({
		title:"Projects",
		tools:[{
			id:'refresh',
			handler:projectsGridStoreReload,
			scope:this
		}],
		layout:{
			type:'vbox'
		},
		items: [
			projectsGridPanel
		],
		style:{
			"margin":"10px"
		},
		height:400,
		width:600
	});
	
	addItemModelViewTab(projectsPanel, true);
<%--

	Add Project Form
--%>
	<c:if test="${permissions.add}">

	var projectFormPanel = new Ext.ss.core.ProjectFormPanel({
		url: ModelPathUtils.getModelProjectPath('/form/add.json'),
		method: 'POST', 
		submitText: 'Add',
		labelAlign: 'right',
		buttonAlign: 'center',
		waitMsg:'Adding Project...',
		waitMsgTarget: true,
		border: false,
		padding: '5 5 5 5'
	});
	
	projectFormPanel.getForm().on('actioncomplete', function(form, action) {
		if(action.type == 'submit' && action.result.success == true) {
			if(action.result.viewUrl) {
				loadModelViewTab(action.result.viewUrl);
			}
		}
	}, this);

	<c:if test="${not empty projectStatusOptions}">
	projectFormPanel.ss.fields.status.getStore().loadData(<hmc:write source="${projectStatusOptions}"/>);
	</c:if>
	
	var panel = new Ext.Panel({
		title: 'Add Project',
		items: [ projectFormPanel ],
		style: { "margin":"10px" },
		width: 325
	});

	addItemModelViewTab(panel, true);

	</c:if>
});
</script>
</div>