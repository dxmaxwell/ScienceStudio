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
				onclick: "return loadModelViewTab(ModelPathUtils.getProjectsPath('.html'));",
				html: "Projects"
			}
		},{
			xtype:"box",
			html: "&raquo;"
		},{
			xtype:"box",
			autoEl:{
				tag: "a",
				href:"#project${project.id}",
				onclick: "return loadModelViewTab(ModelPathUtils.getProjectPath(${project.id}, '.html'));",
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
		url: ModelPathUtils.getProjectPersonsPath(${project.id}, "/grid.json"),
		autoDestroy: true,
		autoLoad: false,
		root:"response",
		fields:[{
			name:"id", mapping:"projectPersonGridBacker.id"
		},{
			name:"projectId", mapping:"projectPersonGridBacker.projectId"
		},{
			name:"fullName", mapping:"projectPersonGridBacker.fullName"
		},{
			name:"emailAddress", mapping:"projectPersonGridBacker.emailAddress"
		},{
			name:"projectRole", mapping:"projectPersonGridBacker.projectRole"
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
			header: "Id", width:30, dataIndex:"id", sortable:true
		},{
			header: "Name", width: 180, dataIndex: 'fullName', sortable: true
		},{
			header: "Email", width: 180, dataIndex: 'emailAddress', sortable: true
		},{
			header: "Role", width: 80, dataIndex: 'projectRole', sortable: true
		}],
		border:false,
		height:400
	});

	projectPersonsGridPanel.on('rowclick', function(grid, index, event) {
		var record = grid.getStore().getAt(index);
		if(record) {
			var projectPersonId = record.get("id");
			if(projectPersonId) {
				loadModelViewTab(ModelPathUtils.getProjectPersonPath(projectPersonId, '.html'));
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
	<sec:authorize ifAnyGranted="ROLE_ADMIN_PROJECTS,PROJECT_ROLE_EXPERIMENTER_${project.id}">

	var projectPersonStore = new Ext.data.JsonStore({
		url:ModelPathUtils.getProjectPersonsPath(${project.id}, '/form/query.json'),
		autoDestroy:true,
		root:'response',
		fields: [{
			name:'id', mapping:'projectPersonFormBacker.id'
		},{
			name:'personUid', mapping:'projectPersonFormBacker.personUid'
		},{
			name:'projectId', mapping:'projectPersonFormBacker.projectId'			
		},{
			name:'fullName', mapping:'projectPersonFormBacker.fullName'
		},{
			name:'emailAddress', mapping:'projectPersonFormBacker.emailAddress'
		},{
			name:'phoneNumber', mapping:'projectPersonFormBacker.phoneNumber'
		},{
			name:'mobileNumber', mapping:'projectPersonFormBacker.mobileNumber'
		},{
			name:'projectRole', mapping:'projectPersonFormBacker.projectRole'
		}]
	});
	
	var projectPersonForm = new Ext.ss.core.ProjectPersonFormPanel({
		url:ModelPathUtils.getProjectPersonsPath(${project.id}, '/form/add.json'),
		method: 'POST', 
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
	
	projectPersonForm.ss.fields.personUid.setDisabled(false);

	<sec:authorize ifAnyGranted="ROLE_ADMIN_PROJECTS">
	projectPersonForm.ss.fields.projectRole.setDisabled(false);
	</sec:authorize>

	projectPersonForm.getForm().on('actioncomplete', function(form, action) {
		if(action.type == 'submit' && action.result.success == true) {
			if(action.result.response && action.result.response.viewUrl) {
				loadModelViewTab(action.result.response.viewUrl);
			}
		}
	}, this);

	<c:if test="${not empty projectRoleList}">
	var projectRoleStoreData = { response:
		<jsp:include page="/WEB-INF/jsp/include/marshal-json.jsp" flush="true">  
			<jsp:param name="source" value="projectRoleList"/>
		</jsp:include>	
	};
	
	projectPersonForm.ss.fields.projectRole.getStore().loadData(projectRoleStoreData);
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

	</sec:authorize>
});
</script>
</div>
