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
			autoEl:{
				tag: "a",
				href:"#experiments${session.gid}",
				onclick: "return loadModelViewTab(ModelPathUtils.getModelExperimentPath('.html?session=${session.gid}'));",
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
		url: ModelPathUtils.getModelExperimentPath('/form/edit.json'),
		method: 'POST',
		submitText: 'Save',
		labelAlign: 'right',
		buttonAlign: 'center',
		<sec:authorize ifContainsNone="SESSION_EXPERIMENTER,FACILITY_ADMIN_SESSIONS">
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
	experimentForm.ss.stores.sample.loadData(<hmc:write source="${sampleList}"/>);
	</c:if>	

	<c:if test="${not empty instrumentList}">
	experimentForm.ss.stores.instrumentStore.loadData(<hmc:write source="${instrumentList}"/>);
	</c:if>

	<c:if test="${not empty instrumentTechniqueList}">
	experimentForm.ss.stores.instrumentTechnique.loadData(<hmc:write source="${instrumentTechniqueList}"/>);
	// Filter all techniques from the data store EXCEPT those for the currently selected instrument. //
	experimentForm.ss.stores.instrumentTechnique.filterBy(function(record, id) { 
		return (record.get('instrumentGid') == "${experiment.instrumentGid}");
	});
	</c:if>

	<c:if test="${not empty experiment}">
	experimentForm.getForm().setValues(<hmc:write source="${experiment}"/>);
	</c:if>	

	var linksPanel = new Ext.Panel({
		border:false,
		layout: 'hbox',
		items: [{
			xtype:'box',
			autoEl:{
				tag: 'a',
				html: 'Scans',
				href:'#scans${experiment.gid}',
				onclick: "return loadModelViewTab(ModelPathUtils.getModelScanPath('.html?experiment=${experiment.gid}'));"
			}
		}],
		padding: '10px'
	});

	function removeExperiment() {
		Ext.Msg.confirm('Question', 'Do you REALLY want to remove this experiment?', function(ans) {
			if(ans == 'yes') {
				Ext.Ajax.request({
					method:'POST',
					params:{ gid:'${experiment.gid}' },
					url:ModelPathUtils.getModelExperimentPath('/form/remove.json'),
					failure:function(response, options) {
						Ext.Msg.alert('Error', 'Network connection problem.');
					},
					success:function(response, options) {
						var json = Ext.decode(response.responseText, true);
						if(json) {
 							if(json.success) {
								loadModelViewTab(ModelPathUtils.getModelExperimentPath('.html?session=${session.gid}'));
							}
							else {
								if(json.message) {
									Ext.Msg.alert('Error', json.message);
								} else {
									Ext.Msg.alert('Error', 'An unspecified error has occurred.');
								}
							}
						}
					}
				});
			}
		});
	};

	var panel = new Ext.Panel({
		title: 'Experiment',
		<sec:authorize ifContainsAny="SESSION_EXPERIMENTER,FACILITY_ADMIN_SESSIONS">
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