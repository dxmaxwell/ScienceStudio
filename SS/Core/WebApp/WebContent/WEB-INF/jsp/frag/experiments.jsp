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
				onclick: "return loadModelViewTab(ModelPathUtils.getModelSessionPath('/${project.gid}.html'));",
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
			html: "Experiments"
		}],
		bodyCssClass:"ss-navigation-trail"
	});
	
	addItemModelViewTab(navigationPanel);
<%--

	Experiments Grid
--%>
	var experimentsGridStore = new Ext.data.JsonStore({
		url: ModelPathUtils.getModelExperimentPath('/grid.json?session=${session.gid}'),
		autoDestroy: true,
		autoLoad: false,
		fields:[{
			name:"gid"
		},{
			name:"name"
		},{
			name:"sampleName"
		},{
			name:"instrumentName"
		},{
			name:"techniqueName"
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
			header: "GID", width:50, dataIndex:"gid", sortable:true
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
			var gid = record.get("gid");
			if(gid) {
				loadModelViewTab(ModelPathUtils.getModelExperimentPath(['/', gid, '.html']));
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
	<c:if test="${permissions.add}">
	var experimentForm = new Ext.ss.core.ExperimentFormPanel({
		url: ModelPathUtils.getModelExperimentPath('/form/add.json'),
		method: 'POST',
		submitText: 'Add',
		labelAlign: 'right',
		buttonAlign: 'center',
		border: false,
		waitMsg:'Adding Experiment...',
		waitMsgTarget: true,
		padding: '5 5 5 5'
	});
	
	experimentForm.ss.fields.sessionGid.setValue('${session.gid}');

	experimentForm.getForm().on('actioncomplete', function(form, action) {
		if(action.type == 'submit' && action.result.success == true) {
			if(action.result.viewUrl) {
				loadModelViewTab(action.result.viewUrl);
			}
		}
	}, this);
	
	<c:if test="${not empty sampleList}">
	experimentForm.ss.stores.sample.loadData(<hmc:write source="${sampleList}"/>);
	</c:if>
	
	<c:if test="${not empty instrumentList}">
	experimentForm.ss.stores.instrumentStore.loadData(<hmc:write source="${instrumentList}"/>);
	</c:if>

	<c:if test="${not empty instrumentTechniqueList}">
	experimentForm.ss.stores.instrumentTechnique.loadData(<hmc:write source="${instrumentTechniqueList}"/>);
	</c:if>
	
	var panel = new Ext.Panel({
		title: 'Add Experiment',
		items: [ experimentForm ],
		style: { "margin":"10px" },
		width: 400
	});
	
	addItemModelViewTab(panel, true);
	</c:if>
});
</script>
</div>