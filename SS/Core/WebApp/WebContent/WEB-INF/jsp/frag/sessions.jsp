<%-- 
	Copyright (c) Canadian Light Source, Inc. All rights reserved.
	- see license.txt for details.
	
	Description:
		Sessions html fragment.
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
			html: "Sessions"
		}],
		bodyCssClass:"ss-navigation-trail"
	});

	addItemModelViewTab(navigationPanel);
<%--

	Sessions Grid
--%>
	var sessionsGridStore = new Ext.data.JsonStore({
		url: ModelPathUtils.getModelSessionPath('.json?project=${project.gid}'),
		autoDestroy: true,
		autoLoad: false,
		fields:[{
			name:"gid"
		},{
			name:"name"
		},{
			name:"proposal"
		},{
			name:"startDate",
			type:"date", dateFormat:"c"
		},{
			name:"endDate",
			type:"date", dateFormat:"c"
		}]
	});

	var sessionsGridStoreReload = function() {
		if(sessionsGridStore) {
			sessionsGridStore.reload();
		}
	};

	var sessionsGridPanel = new Ext.grid.GridPanel({
		store: sessionsGridStore,
		deferRowRender: false,
		viewConfig:{
			forceFit:true
		},
		columns: [{
			header: "GID", width:50, dataIndex:"gid", sortable:true
		},{
			header: "Name", width: 180, dataIndex: 'name', sortable: true
		},{
			header: "Proposal", width: 80, dataIndex: 'proposal', sortable: true
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

	sessionsGridPanel.on('rowclick', function(grid, index, event) {
		var record = grid.getStore().getAt(index);
		if(record) {
			var gid = record.get('gid');
			if(gid) {
				loadModelViewTab(ModelPathUtils.getModelSessionPath(['/', gid, '.html']));
			}
		}
	}, this);

	var sessionsGridStoreReloadTask = {
		run:sessionsGridStoreReload,
		interval:60000,
		scope:this
	}

	sessionsGridPanel.on("afterrender", function() {
		Ext.TaskMgr.start(sessionsGridStoreReloadTask);
	}, this);

	sessionsGridPanel.on("destroy", function() {
		Ext.TaskMgr.stop(sessionsGridStoreReloadTask);
	}, this);

	var sessionsPanel = new Ext.Panel({
		title:"Sessions",
		tools:[{
			id:'refresh',
			handler:sessionsGridStoreReload,
			scope:this
		}],
		items:[
			sessionsGridPanel
		],
		style:{
			"margin":"10px"
		},
		width: 600
	});
	
	addItemModelViewTab(sessionsPanel, true);
<%--

	Add Session Form
--%>
	<sec:authorize ifContainsAny="FACILITY_ADMIN_SESSIONS">
	var sessionForm = new Ext.ss.core.SessionFormPanel({
		url: ModelPathUtils.getModelSessionPath('/form/add.json'),
		method: 'POST',
		submitText: 'Add',
		labelAlign: 'right',
		buttonAlign: 'center',		
		border: false,
		waitMsg:'Adding Session...',
		waitMsgTarget: true,
		padding: '5 5 5 5'
	});
	
	sessionForm.ss.fields.projectGid.setValue('${project.gid}');

	sessionForm.getForm().on('actioncomplete', function(form, action) {
		if(action.type == 'submit' && action.result.success == true) {
			if(action.result && action.result.viewUrl) {
				loadModelViewTab(action.result.viewUrl);
			}
		}
	}, this);

	<c:if test="${not empty laboratoryList}">
	sessionForm.ss.fields.laboratory.getStore().loadData(<hmc:write source="${laboratoryList}"/>);
	</c:if>

	var panel = new Ext.Panel({
		title: 'Add Session',
		items: [ sessionForm ],
		style: { "margin":"10px" },
		width: 400
	});

	addItemModelViewTab(panel, true);
	</sec:authorize>
});
</script>
</div>
