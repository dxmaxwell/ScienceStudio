<%-- 
	Copyright (c) Canadian Light Source, Inc. All rights reserved.
	- see license.txt for details.
	
	Description:
		ProjectPerson html fragment.
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
				href:"#persons${project.id}",
				onclick: "return loadModelViewTab(ModelPathUtils.getProjectPersonsPath(${project.id}, '.html'));",
				html:"Team"
			}
		},{
			xtype:"box",
			html: "&raquo;"
		},{
			xtype:"box",
			html: "Person"
		}],
		bodyCssClass:"ss-navigation-trail"
	});

	addItemModelViewTab(navigationPanel);
<%--


	Edit Project Person
--%>
	var projectPersonForm = new Ext.ss.core.ProjectPersonFormPanel({
		url: ModelPathUtils.getProjectPersonPath(${projectPersonFormBacker.id}, '/form/edit.json'),
		method: 'POST', 
		defaults: {
			disabled:true
		},
		buttonDefaults: {
			hidden:true
		},
		submitText: 'Save',
		labelAlign: 'right',
		buttonAlign: 'center',
		waitMsg:'Saving person...',
		waitMsgTarget: true,
		border: false,
		padding: '5 5 5 5'
	});

	<sec:authorize ifAnyGranted="ROLE_ADMIN_PROJECTS">
	projectPersonForm.ss.fields.projectRole.setDisabled(false);
	projectPersonForm.ss.buttons.submit.setVisible(true);
	</sec:authorize>

	<c:if test="${not empty projectRoleList}">
	var projectRoleStoreData = { response:
		<jsp:include page="/WEB-INF/jsp/include/marshal-json.jsp" flush="true">  
			<jsp:param name="source" value="projectRoleList"/>
		</jsp:include>
	};
	
	projectPersonForm.ss.fields.projectRole.getStore().loadData(projectRoleStoreData);
	</c:if>	

	<c:if test="${not empty projectPersonFormBacker}">
	var ProjectPersonStoreData = { response:[
		<jsp:include page="/WEB-INF/jsp/include/marshal-json.jsp" flush="true">  
			<jsp:param name="source" value="projectPersonFormBacker"/>
		</jsp:include>
	]};
	
	projectPersonForm.ss.fields.personUid.getStore().loadData(ProjectPersonStoreData);
	projectPersonForm.getForm().setValues(ProjectPersonStoreData.response[0].projectPersonFormBacker);
	</c:if>

	function removeProjectPerson() {
		Ext.Msg.confirm('Question', 'Do you REALLY want to remove this person?', function(ans) {
			if(ans == 'yes') {
				Ext.Ajax.request({
					url:ModelPathUtils.getProjectPersonPath(${projectPersonFormBacker.id}, '/remove.json'),
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
		title: 'Person',
		<sec:authorize ifAnyGranted="ROLE_ADMIN_PROJECTS,PROJECT_ROLE_EXPERIMENTER_${projectPersonFormBacker.projectId}">
		tools:[{
			id:'close',
			handler:removeProjectPerson,
			scope:this
		}],
		</sec:authorize>
		items:[
			projectPersonForm
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