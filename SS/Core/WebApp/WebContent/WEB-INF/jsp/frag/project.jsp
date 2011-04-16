<%-- 
	Copyright (c) Canadian Light Source, Inc. All rights reserved.
	- see license.txt for details.
	
	Description:
		Project html fragment.
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
				href:"#projects",
				onclick:"return loadModelViewTab(ModelPathUtils.getProjectsPath('.html'));",
				html:"Projects"
			}
		},{
			xtype:"box",
			html: "&raquo;"
		},{
			xtype:"box",
			html: "Project"
		}],
		bodyCssClass:"ss-navigation-trail"
	});

	addItemModelViewTab(navigationPanel);
<%--


	Edit/View Project Form
--%>
	var projectFormPanel = new Ext.ss.core.ProjectFormPanel({
		url: ModelPathUtils.getProjectPath(${project.id}) + '/form/edit.json',
		method: 'POST',
		<sec:authorize ifNotGranted="ROLE_ADMIN_PROJECTS">
		defaults: {
			disabled:true,
		},
		buttonDefaults: {
			hidden:true
		},
		</sec:authorize>
		submitText: 'Save',
		labelAlign: 'right',
		buttonAlign: 'center',
		waitMsg:'Saving project...',
		waitMsgTarget: true,
		border: false,
		padding: '5 5 5 5'
	});
	
	<c:if test="${not empty projectStatusList}">
	var projectStatusStoreData = { response:
		<jsp:include page="/WEB-INF/jsp/include/marshal-json.jsp" flush="true">  
			<jsp:param name="source" value="projectStatusList"/>
		</jsp:include>
	};
	
	projectFormPanel.ss.fields.status.getStore().loadData(projectStatusStoreData);
	</c:if>
	
	<c:if test="${not empty project}">
	var projectFormValues = 
		<jsp:include page="/WEB-INF/jsp/include/marshal-json.jsp" flush="true">  
			<jsp:param name="source" value="project"/>
		</jsp:include>;
	
	projectFormPanel.getForm().setValues(projectFormValues.project);
	</c:if>
	
	var linksPanel = new Ext.Panel({
		border:false,
		layout: 'hbox',
		items: [{
			xtype:'box',
			flex:1
		},{
			xtype:'box',
			autoEl:{
				tag: 'a',
				html: 'Team',
				href:'#persons${project.id}',
				onclick: "return loadModelViewTab(ModelPathUtils.getProjectPersonsPath(${project.id}, '.html'));"
			}
		},{
			xtype:'box',
			flex:1
		},{
			xtype:'box',
			autoEl:{
				tag: 'a',
				html: 'Samples',
				href:'#samples${project.id}',
				onclick: "return loadModelViewTab(ModelPathUtils.getSamplesPath(${project.id}, '.html'));"
			}
		},{
			xtype:'box',
			flex:1
		},{
			xtype:'box',
			autoEl:{
				tag: 'a',
				html: 'Sessions',
				href:'#sessions${project.id}',
				onclick: "return loadModelViewTab(ModelPathUtils.getSessionsPath(${project.id}, '.html'));"
			}
		},{
			xtype:'box',
			flex:1
		}],
		padding: '10px'
	});
	
	function removeProject() {
		Ext.Msg.confirm('Question', 'Do you REALLY want to remove this project?', function(ans) {
			if(ans == 'yes') {
				Ext.Ajax.request({
					url:ModelPathUtils.getProjectPath(${project.id}, '/remove.json'),
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
		title: 'Project (Id:${project.id})',
		<sec:authorize ifAnyGranted="ROLE_ADMIN_PROJECTS">
		tools:[{
			id:'close',
			handler:removeProject,
			scope:this
		}],
		</sec:authorize>
		items: [ 
			projectFormPanel,
			linksPanel
		],
		style:{
			"margin":"10px"
		},
		width: 325,
	});

	addItemModelViewTab(panel, true);
});
</script>
</div>

