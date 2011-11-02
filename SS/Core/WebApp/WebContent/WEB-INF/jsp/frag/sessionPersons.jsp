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
			autoEl:{
				tag: "a",
				href:"#sessions${project.gid}",
				onclick: "return loadModelViewTab(ModelPathUtils.getModelSessionPath('.html?project=${project.gid}'));",
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
			html: "Team"
		}],
		bodyCssClass:"ss-navigation-trail"
	});

	addItemModelViewTab(navigationPanel);
<%--

	Session Person Grid
--%>
	var sessionPersonsGridStore = new Ext.data.JsonStore({
		url: ModelPathUtils.getModelSessionPersonPath('/grid.json?session=${session.gid}'),
		autoDestroy: true,
		autoLoad: false,
		fields:[{
			name:"gid",
		},{
			name:"sessionGid",
		},{
			name:"fullName",
		},{
			name:"emailAddress",
		},{
			name:"role",
		}]
	});

	var sessionPersonsGridStoreReload = function() {
		if(sessionPersonsGridStore) {
			sessionPersonsGridStore.reload();
		}
	};

	var sessionPersonsGridPanel = new Ext.grid.GridPanel({
		store: sessionPersonsGridStore,
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

	sessionPersonsGridPanel.on('rowclick', function(grid, index, event) {
		var record = grid.getStore().getAt(index);
		if(record) {
			var gid = record.get("gid");
			if(gid) {
				loadModelViewTab(ModelPathUtils.getModelSessionPersonPath(['/', gid, '.html']));
			}
		}
	}, this);

	var sessionPersonsGridStoreReloadTask = {
		run:sessionPersonsGridStoreReload,
		scope:this,
		interval:60000
	}

	Ext.TaskMgr.start(sessionPersonsGridStoreReloadTask);

	sessionPersonsGridPanel.on("destroy", function() {
		Ext.TaskMgr.stop(sessionPersonsGridStoreReloadTask);
	});

	var sessionPersonsPanel = new Ext.Panel({
		title:"Team",
		tools:[{
			id:'refresh',
			handler:sessionPersonsGridStoreReload,
			scope:this
		}],
		items: [
			sessionPersonsGridPanel
		],
		style:{
			"margin":"10px"
		},
		width: 600
	});
	
	addItemModelViewTab(sessionPersonsPanel, true);
<%--

	Add Session Person
--%>
	<sec:authorize ifContainsAny="FACILITY_ADMIN_SESSIONS">

	var sessionPersonStore = new Ext.data.JsonStore({
		url:ModelPathUtils.getModelSessionPersonPath('/form/search.json'),
		baseParams:{ session:'${session.gid}' },
		autoDestroy:true,
		fields: [{
			name:'gid'
		},{
			name:'personGid'
		},{
			name:'sessionGid'			
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
	
	var sessionPersonForm = new Ext.ss.core.SessionPersonFormPanel({
		url:ModelPathUtils.getModelSessionPersonPath('/form/add.json'),
		method: 'POST', 
		defaults: {
			disabled:true
		},
		submitText: 'Add',
		labelAlign: 'right',
		buttonAlign: 'center',		
		border: false,
		sessionPersonStore:sessionPersonStore,
		waitMsg:'Adding Person...',
		waitMsgTarget: true,
		padding: '5 5 5 5'
	});
	
	sessionPersonForm.ss.fields.personGid.setDisabled(false);
	sessionPersonForm.ss.fields.role.setDisabled(false);

	sessionPersonForm.getForm().on('actioncomplete', function(form, action) {
		if(action.type == 'submit' && action.result.success == true) {
			if(action.result.viewUrl) {
				loadModelViewTab(action.result.viewUrl);
			}
		}
	}, this);

	<c:if test="${not empty sessionRoleOptions}">
	sessionPersonForm.ss.fields.role.getStore().loadData(<hmc:write source="${sessionRoleOptions}"/>);
	</c:if>	

	var panel = new Ext.Panel({
		title: 'Add Person',
		items: [ sessionPersonForm ],
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
