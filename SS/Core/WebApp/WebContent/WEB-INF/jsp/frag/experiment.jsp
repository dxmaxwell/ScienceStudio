<%-- 
	Copyright (c) Canadian Light Source, Inc. All rights reserved.
	- see license.txt for details.
	
	Description:
		Experiment html fragment.
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
			autoEl:{
				tag: "a",
				href:"#experiments${session.id}",
				onclick: "return loadModelViewTab(ModelPathUtils.getExperimentsPath(${session.id}, '.html'));",
				html:"Experiments"
			}
		},{
			xtype:"box",
			html: "&raquo;"
		},{
			xtype:"box",
			html: "Experiment"
		}],
		bodyCssClass:"ss-navigation-trail"
	});
	
	addItemModelViewTab(navigationPanel);
<%--

	Edit Experiment Form
--%>
	var experimentForm = new Ext.ss.core.ExperimentFormPanel({
		url: ModelPathUtils.getExperimentPath(${experiment.id}, '/form/edit.json'),
		method: 'POST',
		submitText: 'Save',
		labelAlign: 'right',
		buttonAlign: 'center',
		<sec:authorize ifNotGranted="ROLE_ADMIN_PROJECTS,PROJECT_ROLE_EXPERIMENTER_${project.id}">
		defaults: {
			disabled:true
		},
		buttonDefaults: {
			hidden:true,
		},
		</sec:authorize>
		border: false,
		waitMsg:'Saving Experiment...',
		waitMsgTarget: true,
		padding: '5 5 5 5'
	});
	
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

	<c:if test="${not empty experimentFormBacker}">
	var experimentFormValues = 
		<jsp:include page="/WEB-INF/jsp/include/marshal-json.jsp" flush="true">  
			<jsp:param name="source" value="experimentFormBacker"/>
		</jsp:include>;
	
	experimentForm.getForm().setValues(experimentFormValues.experimentFormBacker);
	</c:if>	

	var linksPanel = new Ext.Panel({
		border:false,
		layout: 'hbox',
		items: [{
			xtype:'box',
			autoEl:{
				tag: 'a',
				html: 'Scans',
				href:'#scans${experiment.id}',
				onclick: "return loadModelViewTab(ModelPathUtils.getScansPath(${experiment.id}, '.html'));"
			}
		}],
		padding: '10px'
	});

	function removeExperiment() {
		Ext.Msg.confirm('Question', 'Do you REALLY want to remove this experiment?', function(ans) {
			if(ans == 'yes') {
				Ext.Ajax.request({
					url:ModelPathUtils.getExperimentPath(${experiment.id}, '/remove.json'),
					failure:function(response, options) {
						Ext.Msg.alert('Error', 'Network connection problem.');
					},
					success:function(response, options) {
						var json = Ext.decode(response.responseText, true);
						if(json) {
 							if(json.success && json.response.viewUrl) {
								loadModelViewTab(json.response.viewUrl);
							}
							else if(json.globalErrors && json.globalErrors[0]) {
								Ext.Msg.alert('Error', json.globalErrors[0]);
							}
							else {
								Ext.Msg.alert('Error', 'An unspecified error has occurred.');
							}
						}
					}
				});
			}
		});
	};

	var panel = new Ext.Panel({
		title: 'Experiment',
		<sec:authorize ifAnyGranted="ROLE_ADMIN_PROJECTS,PROJECT_ROLE_EXPERIMENTER_${project.id}">
		tools:[{
			id:'close',
			handler:removeExperiment,
			scope:this
		}],
		</sec:authorize>
		items:[
			experimentForm,
			linksPanel
		],
		style:{
			"margin":"10px"
		},
		width: 400
	});

	addItemModelViewTab(panel, true);
});
</script>
</div>