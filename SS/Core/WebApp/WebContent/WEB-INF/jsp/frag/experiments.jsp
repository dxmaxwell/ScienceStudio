<%-- 
	Copyright (c) Canadian Light Source, Inc. All rights reserved.
	- see license.txt for details.
	
	Description:
		Experiments html fragment.
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
			autoEl:{
				tag: "a",
				href:"#sessions${project.id}",
				onclick: "return loadModelViewTab(ModelPathUtils.getSessionsPath(${project.id}, '.html'));",
				html:"Sessions"
			}
		},{
			xtype:"box",
			html: "&raquo;"
		},{
			xtype:"box",
			autoEl:{
				tag: "a",
				href:"#session${session.id}",
				onclick: "return loadModelViewTab(ModelPathUtils.getSessionPath(${session.id}, '.html'));",
				html:"${session.name}"
			}
		},{
			xtype:"box",
			html: "&raquo;"
		},{
			xtype:"box",
			html: "Experiments"
		}],
		bodyCssClass:"ss-navigation-trail"
	});
	
	addItemModelViewTab(navigationPanel);
<%--

	Experiments Grid
--%>
	var experimentsGridStore = new Ext.data.JsonStore({
		url: ModelPathUtils.getExperimentsPath(${session.id}, "/grid.json"),
		autoDestroy: true,
		autoLoad: false,
		root:"response",
		fields:[{
			name:"id", mapping:"experimentGridBacker.id"
		},{
			name:"projectId", mapping:"experimentGridBacker.projectId"
		},{
			name:"sessionId", mapping:"experimentGridBacker.sessionId"
		},{
			name:"name", mapping:"experimentGridBacker.name"
		},{
			name:"sampleName", mapping:"experimentGridBacker.sampleName"
		},{
			name:"instrumentName", mapping:"experimentGridBacker.instrumentName"
		},{
			name:"techniqueName", mapping:"experimentGridBacker.techniqueName"
		}]
	});

	var experimentsGridStoreReload = function() {
		if(experimentsGridStore) {
			experimentsGridStore.reload();
		}
	};

	var experimentsGridPanel = new Ext.grid.GridPanel({
		store: experimentsGridStore,
		deferRowRender: false,
		viewConfig:{
			forceFit:true
		},
		columns: [{
			header: "Id", width:30, dataIndex:"id", sortable:true
		},{
			header: "Name", width: 180, dataIndex: 'name', sortable: true
		},{
			header: "Sample", width: 80, dataIndex: 'sampleName', sortable: true
		},{
			header: "Instrument", width: 80, dataIndex: 'instrumentName', sortable: true
		},{
			header: "Technique", width: 80, dataIndex: 'techniqueName', sortable: true
		}],
		border:false,
		height:300
	});

	experimentsGridPanel.on('rowclick', function(grid, index, event) {
		var record = grid.getStore().getAt(index);
		if(record) {
			var experimentId = record.get("id");
			if(experimentId) {
				loadModelViewTab(ModelPathUtils.getExperimentPath(experimentId, '.html'));
			}
		}
	}, this);

	var experimentsGridStoreReloadTask = {
		run:experimentsGridStoreReload,
		scope:this,
		interval:60000
	}

	Ext.TaskMgr.start(experimentsGridStoreReloadTask);

	experimentsGridPanel.on("destroy", function() {
		Ext.TaskMgr.stop(experimentsGridStoreReloadTask);
	});

	var experimentsPanel = new Ext.Panel({
		title:"Experiments",
		tools:[{
			id:'refresh',
			handler:experimentsGridStoreReload,
			scope:this
		}],
		items:[
			experimentsGridPanel
		],
		style:{
			"margin":"10px"
		},
		width: 600
	});
	
	addItemModelViewTab(experimentsPanel, true);
<%--

	Add Experiment Form
--%>
	<sec:authorize ifAnyGranted="ROLE_ADMIN_PROJECTS,PROJECT_ROLE_EXPERIMENTER_${project.id}">
	var experimentForm = new Ext.ss.core.ExperimentFormPanel({
		url: ModelPathUtils.getExperimentsPath(${session.id}, '/form/add.json'),
		method: 'POST',
		submitText: 'Add',
		labelAlign: 'right',
		buttonAlign: 'center',
		border: false,
		waitMsg:'Adding Experiment...',
		waitMsgTarget: true,
		padding: '5 5 5 5'
	});
	
	experimentForm.getForm().on('actioncomplete', function(form, action) {
		if(action.type == 'submit' && action.result.success == true) {
			if(action.result.response && action.result.response.viewUrl) {
				loadModelViewTab(action.result.response.viewUrl);
			}
		}
	}, this);
	
	<c:if test="${not empty sampleList}">
	var sampleStoreData = { response:
		<jsp:include page="/WEB-INF/jsp/include/marshal-json.jsp" flush="true">  
			<jsp:param name="source" value="sampleList"/>
		</jsp:include>	
	};
	
	experimentForm.ss.stores.sample.loadData(sampleStoreData);
	</c:if>
	
	<c:if test="${not empty instrumentTechniqueOptionList}">
	var instrumentTechniqueOptionStoreData = { response:
		<jsp:include page="/WEB-INF/jsp/include/marshal-json.jsp" flush="true">  
			<jsp:param name="source" value="instrumentTechniqueOptionList"/>
		</jsp:include>
	};
	
	experimentForm.ss.stores.instrumentTechniqueOption.loadData(instrumentTechniqueOptionStoreData);
	</c:if>
	
	var panel = new Ext.Panel({
		title: 'Add Experiment',
		items: [ experimentForm ],
		style: { "margin":"10px" },
		width: 400
	});
	
	addItemModelViewTab(panel, true);
	</sec:authorize>
});
</script>
</div>