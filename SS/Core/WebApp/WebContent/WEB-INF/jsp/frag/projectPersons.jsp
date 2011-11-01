<%-- 
	Copyright (c) Canadian Light Source, Inc. All rights reserved.
	- see license.txt for details.
	
	Description:
		ProjectPersons html fragment.
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
				onclick: "return loadModelViewTab(ModelPathUtils.getModelProjectPath('/${project.gid}.html'));",
				html:"${project.name}"
			}
		},{
			xtype:"box",
			html: "&raquo;"
		},{
			xtype:"box",
			html: "Team"
		}],
		bodyCssClass:"ss-navigation-trail"
	});

	addItemModelViewTab(navigationPanel);
<%--

	Project Person Grid
--%>
	var projectPersonsGridStore = new Ext.data.JsonStore({
		url: ModelPathUtils.getModelProjectPersonPath('/grid.json?project=${project.gid}'),
		autoDestroy: true,
		autoLoad: false,
		fields:[{
			name:"gid",
		},{
			name:"projectGid",
		},{
			name:"fullName",
		},{
			name:"emailAddress",
		},{
			name:"role",
		}]
	});

	var projectPersonsGridStoreReload = function() {
		if(projectPersonsGridStore) {
			projectPersonsGridStore.reload();
		}
	};

	var projectPersonsGridPanel = new Ext.grid.GridPanel({
		store: projectPersonsGridStore,
		deferRowRender: false,
		viewConfig:{
			forceFit:true
		},
		columns: [{
			header: "GID", width:50, dataIndex:"gid", sortable:true
		},{
			header: "Name", width: 180, dataIndex: 'fullName', sortable: true
		},{
			header: "Email", width: 180, dataIndex: 'emailAddress', sortable: true
		},{
			header: "Role", width: 80, dataIndex: 'role', sortable: true
		}],
		border:false,
		height:400
	});

	projectPersonsGridPanel.on('rowclick', function(grid, index, event) {
		var record = grid.getStore().getAt(index);
		if(record) {
			var gid = record.get("gid");
			if(gid) {
				loadModelViewTab(ModelPathUtils.getModelProjectPersonPath(['/', gid, '.html']));
			}
		}
	}, this);

	var projectPersonsGridStoreReloadTask = {
		run:projectPersonsGridStoreReload,
		scope:this,
		interval:60000
	}

	Ext.TaskMgr.start(projectPersonsGridStoreReloadTask);

	projectPersonsGridPanel.on("destroy", function() {
		Ext.TaskMgr.stop(projectPersonsGridStoreReloadTask);
	});

	var projectPersonsPanel = new Ext.Panel({
		title:"Team",
		tools:[{
			id:'refresh',
			handler:projectPersonsGridStoreReload,
			scope:this
		}],
		items: [
			projectPersonsGridPanel
		],
		style:{
			"margin":"10px"
		},
		width: 600
	});
	
	addItemModelViewTab(projectPersonsPanel, true);
<%--

	Add Project Person
--%>
	<c:if test="${permissions.add}">

	var projectPersonStore = new Ext.data.JsonStore({
		url:ModelPathUtils.getModelProjectPersonPath('/form/search.json'),
		baseParams:{ project:'${project.gid}' },
		autoDestroy:true,
		fields: [{
			name:'gid'
		},{
			name:'personGid'
		},{
			name:'projectGid'			
		},{
			name:'fullName'
		},{
			name:'emailAddress'
		},{
			name:'phoneNumber'
		},{
			name:'mobileNumber'
		},{
			name:'role'
		}]
	});
	
	var projectPersonForm = new Ext.ss.core.ProjectPersonFormPanel({
		url:ModelPathUtils.getModelProjectPersonPath('/form/add.json'),
		defaults: {
			disabled:true
		},
		submitText: 'Add',
		labelAlign: 'right',
		buttonAlign: 'center',		
		border: false,
		projectPersonStore:projectPersonStore,
		waitMsg:'Adding Person...',
		waitMsgTarget: true,
		padding: '5 5 5 5'
	});
	
	projectPersonForm.ss.fields.personGid.setDisabled(false);
	projectPersonForm.ss.fields.role.setDisabled(false);

	projectPersonForm.getForm().on('actioncomplete', function(form, action) {
		if(action.type == 'submit' && action.result.success == true) {
			if(action.result.viewUrl) {
				loadModelViewTab(action.result.viewUrl);
			}
		}
	}, this);

	<c:if test="${not empty projectRoleOptions}">
	projectPersonForm.ss.fields.role.getStore().loadData(<hmc:write source="${projectRoleOptions}"/>);
	</c:if>	

	var panel = new Ext.Panel({
		title: 'Add Person',
		items: [ projectPersonForm ],
		style: {
			"margin":"10px"
		},
		width: 400
	});

	addItemModelViewTab(panel, true);

	</c:if>
});
</script>
</div>
