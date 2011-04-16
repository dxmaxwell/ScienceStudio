<%-- 
	Copyright (c) Canadian Light Source, Inc. All rights reserved.
	- see license.txt for details.
	
	Description:
		Session html fragment.
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
			html: "Session"
		}],
		bodyCssClass:"ss-navigation-trail"
	});

	addItemModelViewTab(navigationPanel);

	<%--
				
		Edit Session Form
	--%>
	var sessionForm = new Ext.ss.core.SessionFormPanel({
		url: ModelPathUtils.getSessionPath(${session.id}, '/form/edit.json'),
		method: 'POST',
		submitText: 'Save',
		labelAlign: 'right',
		buttonAlign: 'center',
		<sec:authorize ifNotGranted="ROLE_ADMIN_PROJECTS">
		defaults: {
			disabled:true
		},
		buttonDefaults: {
			hidden:true
		},
		</sec:authorize>
		border: false,
		waitMsg:'Saving Session...',
		waitMsgTarget: true,
		padding: '5 5 5 5'
	});

	<c:if test="${not empty laboratoryList}">
	var laboratoryStoreData = { response:
		<jsp:include page="/WEB-INF/jsp/include/marshal-json.jsp" flush="true">  
			<jsp:param name="source" value="laboratoryList"/>
		</jsp:include>
	};
	
	sessionForm.ss.fields.laboratory.getStore().loadData(laboratoryStoreData);
	</c:if>	

	<c:if test="${not empty sessionFormBacker}">
	var sessionFormData = 
		<jsp:include page="/WEB-INF/jsp/include/marshal-json.jsp" flush="true">  
			<jsp:param name="source" value="sessionFormBacker"/>
		</jsp:include>;
	
	sessionForm.getForm().setValues(sessionFormData.sessionFormBacker);
	</c:if>	

	var linksPanel = new Ext.Panel({
		border:false,
		layout: 'hbox',
		items: [{
			xtype:'box',
			autoEl:{
				tag: 'a',
				html: 'Experiments',
				href:'#experiments${session.id}',
				onclick: "return loadModelViewTab(ModelPathUtils.getExperimentsPath(${session.id}, '.html'));"
			}
		}],
		padding: '10px'
	});

	function removeSession() {
		Ext.Msg.confirm('Question', 'Do you REALLY want to remove this session?', function(ans) {
			if(ans == 'yes') {
				Ext.Ajax.request({
					url:ModelPathUtils.getSessionPath(${session.id}, '/remove.json'),
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

	var sessionPanel = new Ext.Panel({
		title: 'Session (Id:${session.id})',
		<sec:authorize ifAnyGranted="ROLE_ADMIN_PROJECTS">
		tools:[{
			id:'close',
			handler:removeSession,
			scope:this
		}],
		</sec:authorize>
		items:[
			sessionForm,
			linksPanel
		],
		style:{
			"margin":"10px"
		},
		width: 400
	});

	addItemModelViewTab(sessionPanel);

	<%--
				
		Join Session Form
	--%>
	var joinSessionButton = new Ext.Button({
		text:'Join Session'
	});

	joinSessionButton.on('click', function() {
		openLabViewTab(${session.id}, joinSessionCheckbox.getValue());
	}, this);

	var joinSessionCheckbox = new Ext.form.Checkbox({
		labelSeparator:''
	});	

	var joinSessionCheckboxLabel = new Ext.form.Label({
		text:' Open session in new window?',
		cls:'x-form-item-label',
		width:200
	});

	var joinSessionForm = new Ext.form.FormPanel({
		items: [{
			layout: 'column',
			defaults: {
				layout: 'form',
				border: false
			},
			items: [{
				columnWidth: 0.3,
				items: [ joinSessionCheckbox ]
			},{
				columnWidth: 0.7,				
				items: [ joinSessionCheckboxLabel ]
			}],
			cls:'x-form-item',
			border: false
		}],
		buttons: [ joinSessionButton ],
		buttonAlign: 'center',
		labelWidth: 85,
		border: false
	});

	var joinSessionPanel = new Ext.Panel({
		title:"Join",
		items: [ joinSessionForm ],
		style: { "margin":"10px" },
		width: 400
	});

	addItemModelViewTab(joinSessionPanel, true);
});
</script>
</div>
