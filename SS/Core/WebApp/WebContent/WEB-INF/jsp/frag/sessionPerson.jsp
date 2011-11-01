<%-- 
	Copyright (c) Canadian Light Source, Inc. All rights reserved.
	- see license.txt for details.
	
	Description:
		SessionPerson html fragment.
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
				href:"#persons${session.gid}",
				onclick: "return loadModelViewTab(ModelPathUtils.getModelSessionPersonPath('.html?session=${session.gid}'));",
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


	Edit Session Person
--%>
	var sessionPersonForm = new Ext.ss.core.SessionPersonFormPanel({
		url: ModelPathUtils.getModelSessionPersonPath('/form/edit.json'),
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

	<c:if test="${permissions.edit}">
	sessionPersonForm.ss.fields.role.setDisabled(false);
	sessionPersonForm.ss.fields.personGid.setDisabled(false);
	sessionPersonForm.ss.fields.personGid.setReadOnly(true);
	sessionPersonForm.ss.buttons.submit.setVisible(true);
	</c:if>

	<c:if test="${not empty sessionRoleOptions}">
	sessionPersonForm.ss.fields.role.getStore().loadData(<hmc:write source="${sessionRoleOptions}"/>);
	</c:if>	

	<c:if test="${not empty sessionPerson}">
	var sessionPersonStoreData = [ <hmc:write source="${sessionPerson}"/> ];	
	sessionPersonForm.ss.fields.personGid.getStore().loadData(sessionPersonStoreData);
	sessionPersonForm.getForm().setValues(sessionPersonStoreData[0]);
	</c:if>

	function removeSessionPerson() {
		Ext.Msg.confirm('Question', 'Do you REALLY want to remove this person?', function(ans) {
			if(ans == 'yes') {
				Ext.Ajax.request({
					method:'POST',
					params:{ gid:'${sessionPerson.gid}' },
					url:ModelPathUtils.getModelSessionPersonPath('/form/remove.json'),
					failure:function(response, options) {
						Ext.Msg.alert('Error', 'Network connection problem.');
					},
					success:function(response, options) {
						var json = Ext.decode(response.responseText, true);
						if(json) {
 							if(json.success) {
								if(json.viewUrl) {
									loadModelViewTab(json.viewUrl);
								} else {
									loadModelViewTab(ModelPathUtils.getModelSessionPersonPath('.html'));
								}
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
		<c:if test="${permissions.remove}">
		tools:[{
			id:'close',
			handler:removeSessionPerson,
			scope:this
		}],
		</c:if>
		items:[
			sessionPersonForm
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