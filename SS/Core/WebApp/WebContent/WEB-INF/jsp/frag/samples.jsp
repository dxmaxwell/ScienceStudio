<%-- 
	Copyright (c) Canadian Light Source, Inc. All rights reserved.
	- see license.txt for details.
	
	Description:
		Samples html fragment.
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
				onclick: "return loadModelViewTab(ModelPathUtils.getModelProjectsPath('.html'));",
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
			html: "Samples"
		}],
		bodyCssClass:"ss-navigation-trail"
	});

	addItemModelViewTab(navigationPanel);
<%--

	Samples Grid
--%>
	var samplesGridStore = new Ext.data.JsonStore({
		url: ModelPathUtils.getModelSamplePath('.json?project=${project.gid}'),
		autoDestroy: true,
		autoLoad: false,
		fields:[{
			name:"gid"
		},{
			name:"projectGid"
		},{
			name:"name"
		},{
			name:"casNumber"
		},{
			name:"quantity"
		},{
			name:"state"
		}]
	});

	var samplesGridStoreReload = function() {
		if(samplesGridStore) {
			samplesGridStore.reload();
		}
	};

	var samplesGridPanel = new Ext.grid.GridPanel({
		store: samplesGridStore,
		deferRowRender: false,
		viewConfig:{
			forceFit:true
		},
		columns: [{
			header: "GID", width:50, dataIndex:"gid", sortable:true
		},{
			header: "Name", width: 180, dataIndex: 'name', sortable: true
		},{
			header: "CAS Number", width: 80, dataIndex: 'casNumber', sortable: true
		},{
			header: "Quantity", width: 80, dataIndex: 'quantity', sortable: true
		},{
			header: "State", width: 50, dataIndex: 'state', sortable: true
		}],
		border:false,
		height:400
	});

	samplesGridPanel.on('rowclick', function(grid, index, event) {
		var  record = grid.getStore().getAt(index);
		if(record) {
			var gid = record.get("gid");
			if(gid) {
				loadModelViewTab(ModelPathUtils.getModelSamplePath(['/', gid, '.html']));
			}
		}
	}, this);

	var samplesGridStoreReloadTask = {
		run:samplesGridStoreReload,
		scope:this,
		interval:60000
	}

	Ext.TaskMgr.start(samplesGridStoreReloadTask);

	samplesGridPanel.on("destroy", function() {
		Ext.TaskMgr.stop(samplesGridStoreReloadTask);
	});

	var samplesPanel = new Ext.Panel({
		title:"Samples",
		tools:[{
			id:'refresh',
			handler:samplesGridStoreReload,
			scope:this
		}],
		items:[
			samplesGridPanel
		],
		style:{
			"margin":"10px"
		},
		width: 600
	});
	
	addItemModelViewTab(samplesPanel, true);
<%--

	Add Sample Form
--%>
	<sec:authorize ifContainsAny="PROJECT_RESEARCHER,FACILITY_ADMIN_PROJECTS">

	var sampleForm = new Ext.ss.core.SampleFormPanel({
		url: ModelPathUtils.getModelSamplePath('/form/add.json'),
		method: 'POST',
		submitText: 'Add',
		labelAlign: 'right',
		buttonAlign: 'center',		
		border: false,
		waitMsg:'Adding sample...',
		waitMsgTarget: true,
		padding: '5 5 5 5'
	});
	
	sampleForm.ss.fields.projectGid.setValue('${project.gid}');

	<c:if test="${not empty sampleStateOptions}">
	sampleForm.ss.fields.state.getStore().loadData(<hmc:write source="${sampleStateOptions}"/>);
	</c:if>	

	sampleForm.getForm().on('actioncomplete', function(form, action) {
		if(action.type == 'submit' && action.result.success == true) {
			if(action.result.viewUrl) {
				loadModelViewTab(action.result.viewUrl);
			}
		}
	}, this);

	var panel = new Ext.Panel({
		title: 'Add Sample',
		items: [ sampleForm ],
		style: { "margin":"10px"},
		width: 400
	});

	addItemModelViewTab(panel, true);

	</sec:authorize>
});
</script>
</div>
