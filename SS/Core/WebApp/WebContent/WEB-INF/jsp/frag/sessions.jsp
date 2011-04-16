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
			html: "Sessions"
		}],
		bodyCssClass:"ss-navigation-trail"
	});

	addItemModelViewTab(navigationPanel);
<%--

	Sessions Grid
--%>
	var sessionsGridStore = new Ext.data.JsonStore({
		url: ModelPathUtils.getSessionsPath(${project.id}, ".json"),
		autoDestroy: true,
		autoLoad: false,
		root:"response",
		fields:[{
			name:"id", mapping:"session.id"
		},{
			name:"name", mapping:"session.name"
		},{
			name:"proposal", mapping:"session.proposal"
		},{
			name:"startDate", mapping:"session.startDate",
			type:"date", dateFormat:Date.patterns.ISO8601Full
		},{
			name:"endDate", mapping:"session.endDate",
			type:"date", dateFormat:Date.patterns.ISO8601Full
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
			header: "Id", width:30, dataIndex:"id", sortable:true
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
			var sessionId = record.get('id');
			if(sessionId) {
				loadModelViewTab(ModelPathUtils.getSessionPath(sessionId, '.html'));
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
	<sec:authorize ifAnyGranted="ROLE_ADMIN_PROJECTS">
	var sessionForm = new Ext.ss.core.SessionFormPanel({
		url: ModelPathUtils.getSessionsPath(${project.id}, '/form/add.json'),
		method: 'POST',
		submitText: 'Add',
		labelAlign: 'right',
		buttonAlign: 'center',		
		border: false,
		waitMsg:'Adding Session...',
		waitMsgTarget: true,
		padding: '5 5 5 5'
	});
	
	sessionForm.getForm().on('actioncomplete', function(form, action) {
		if(action.type == 'submit' && action.result.success == true) {
			if(action.result.response && action.result.response.viewUrl) {
				loadModelViewTab(action.result.response.viewUrl);
			}
		}
	}, this);

	<c:if test="${not empty laboratoryList}">
	var laboratoryStoreData = { response:
		<jsp:include page="/WEB-INF/jsp/include/marshal-json.jsp" flush="true">  
			<jsp:param name="source" value="laboratoryList"/>
		</jsp:include>
	};
	
	sessionForm.ss.fields.laboratory.getStore().loadData(laboratoryStoreData);
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
