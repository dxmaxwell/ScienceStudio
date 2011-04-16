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
			html: "Samples"
		}],
		bodyCssClass:"ss-navigation-trail"
	});

	addItemModelViewTab(navigationPanel);
<%--

	Samples Grid
--%>
	var samplesGridStore = new Ext.data.JsonStore({
		url: ModelPathUtils.getSamplesPath(${project.id}, ".json"),
		autoDestroy: true,
		autoLoad: false,
		root:"response",
		fields:[{
			name:"id", mapping:"sample.id"
		},{
			name:"projectId", mapping:"sample.projectId"
		},{
			name:"name", mapping:"sample.name"
		},{
			name:"casNumber", mapping:"sample.casNumber"
		},{
			name:"quantity", mapping:"sample.quantity"
		},{
			name:"state", mapping:"sample.state"
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
			header: "Id", width:30, dataIndex:"id", sortable:true
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
			var sampleId = record.get("id");
			if(sampleId) {
				loadModelViewTab(ModelPathUtils.getSamplePath(sampleId, '.html'));
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
<sec:authorize ifAnyGranted="ROLE_ADMIN_PROJECTS,PROJECT_ROLE_EXPERIMENTER_${project.id}">

	var sampleForm = new Ext.ss.core.SampleFormPanel({
		url: ModelPathUtils.getSamplesPath(${project.id}, '/form/add.json'),
		method: 'POST',
		submitText: 'Add',
		labelAlign: 'right',
		buttonAlign: 'center',		
		border: false,
		waitMsg:'Adding sample...',
		waitMsgTarget: true,
		padding: '5 5 5 5'
	});
	
	<c:if test="${not empty sampleStateList}">
	var stateStoreData = { response:
		<jsp:include page="/WEB-INF/jsp/include/marshal-json.jsp" flush="true">  
			<jsp:param name="source" value="sampleStateList"/>
		</jsp:include>
	};
	
	sampleForm.ss.fields.state.getStore().loadData(stateStoreData);
	</c:if>	

	sampleForm.getForm().on('actioncomplete', function(form, action) {
		if(action.type == 'submit' && action.result.success == true) {
			if(action.result.response && action.result.response.viewUrl) {
				loadModelViewTab(action.result.response.viewUrl);
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
