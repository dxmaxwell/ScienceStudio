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
				href:"#persons${project.gid}",
				onclick: "return loadModelViewTab(ModelPathUtils.getModelProjectPersonPath('.html?project=${project.gid}'));",
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
		url: ModelPathUtils.getModelProjectPersonPath('/form/edit.json'),
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

	<sec:authorize ifContainsAny="PROJECT_RESEARCHER,FACILITY_ADMIN_PROJECTS">
	projectPersonForm.ss.fields.role.setDisabled(false);
	projectPersonForm.ss.fields.personGid.setDisabled(false);
	projectPersonForm.ss.fields.personGid.setReadOnly(true);
	projectPersonForm.ss.buttons.submit.setVisible(true);
	</sec:authorize>

	<c:if test="${not empty projectRoleOptions}">
	projectPersonForm.ss.fields.role.getStore().loadData(<hmc:write source="${projectRoleOptions}"/>);
	</c:if>	

	<c:if test="${not empty projectPerson}">
	var ProjectPersonStoreData = [ <hmc:write source="${projectPerson}"/> ];	
	projectPersonForm.ss.fields.personGid.getStore().loadData(ProjectPersonStoreData);
	projectPersonForm.getForm().setValues(ProjectPersonStoreData[0]);
	</c:if>

	function removeProjectPerson() {
		Ext.Msg.confirm('Question', 'Do you REALLY want to remove this person?', function(ans) {
			if(ans == 'yes') {
				Ext.Ajax.request({
					method:'POST',
					params:{ gid:'${projectPerson.gid}' },
					url:ModelPathUtils.getModelProjectPersonPath('/form/remove.json'),
					failure:function(response, options) {
						Ext.Msg.alert('Error', 'Network connection problem.');
					},
					success:function(response, options) {
						var json = Ext.decode(response.responseText, true);
						if(json) {
 							if(json.success) {
								loadModelViewTab(ModelPathUtils.getModelProjectPersonPath('.html?project=${project.gid}'));
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
		title: 'Person',
		<sec:authorize ifContainsAny="PROJECT_RESEARCHER,FACILITY_ADMIN_PROJECTS">
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