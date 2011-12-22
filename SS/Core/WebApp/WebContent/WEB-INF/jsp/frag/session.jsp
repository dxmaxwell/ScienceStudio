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
			html: "Session"
		}],
		bodyCssClass:"ss-navigation-trail"
	});

	addItemModelViewTab(navigationPanel);

	<%--
				
		Edit Session Form
	--%>
	var sessionForm = new Ext.ss.core.SessionFormPanel({
		url: ModelPathUtils.getModelSessionPath('/form/edit.json'),
		method: 'POST',
		submitText: 'Save',
		labelAlign: 'right',
		buttonAlign: 'center',
		border: false,
		waitMsg:'Saving Session...',
		waitMsgTarget: true,
		padding: '5 5 5 5'
	});

	<sec:authorize ifContainsNone="SESSION_EXPERIMENTER,FACILITY_ADMIN_SESSIONS">
	sessionForm.ss.fields.name.setDisabled(true);
	sessionForm.ss.fields.description.setDisabled(true);
	sessionForm.ss.buttons.submit.setVisible(false);
	</sec:authorize>

	<sec:authorize ifContainsNone="FACILITY_ADMIN_SESSIONS">
	sessionForm.ss.fields.proposal.setDisabled(true);
	sessionForm.ss.fields.startDay.setDisabled(true);
	sessionForm.ss.fields.startTime.setDisabled(true);
	sessionForm.ss.fields.endDay.setDisabled(true);
	sessionForm.ss.fields.endTime.setDisabled(true);
	</sec:authorize>

	sessionForm.ss.fields.laboratory.setDisabled(true);

	<c:if test="${not empty laboratoryList}">	
	sessionForm.ss.fields.laboratory.getStore().loadData(<hmc:write source="${laboratoryList}"/>);
	</c:if>

	<c:if test="${not empty session}">
	sessionForm.getForm().setValues(<hmc:write source="${session}"/>);
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
				href:'#persons${session.gid}',
				onclick: "return loadModelViewTab(ModelPathUtils.getModelSessionPersonPath('.html?session=${session.gid}'));"
			},
		},{
			xtype:'box',
			flex:1
		},{
			xtype:'box',
			autoEl:{
				tag: 'a',
				html: 'Experiments',
				href:'#experiments${session.gid}',
				onclick: "return loadModelViewTab(ModelPathUtils.getModelExperimentPath('.html?session=${session.gid}'));"
			},
		},{
			xtype:'box',
			flex:1
		}],
		padding: '10px'
	});

	function removeSession() {
		Ext.Msg.confirm('Question', 'Do you REALLY want to remove this session?', function(ans) {
			if(ans == 'yes') {
				Ext.Ajax.request({
					method:'POST',
					params:{ gid:'${session.gid}' },
					url:ModelPathUtils.getModelSessionPath('/form/remove.json'),
					failure:function(response, options) {
						Ext.Msg.alert('Error', 'Network connection problem.');
					},
					success:function(response, options) {
						var json = Ext.decode(response.responseText, true);
						if(json) {
 							if(json.success) {
								loadModelViewTab(ModelPathUtils.getModelProjectPath('.html?project=${project.gid}'));
							}
							else  {
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

	var sessionPanel = new Ext.Panel({
		title: 'Session (GID:${session.gid})',
		<sec:authorize ifContainsAny="FACILITY_ADMIN_SESSIONS">
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

	addItemModelViewTab(sessionPanel, true);

	<%--
				
		Join Session Form
	--%>

	<c:if test="${fn:containsIgnoreCase(sessionType,'daq')==true}"> 

	var joinSessionButton = new Ext.Button({
		text:'Join Session'
	});

	joinSessionButton.on('click', function() {
		openLabViewTab('${session.gid}', joinSessionCheckbox.getValue());
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
	</c:if> 

	<%--
				
		import data Form
	--%>

	<c:if test="${fn:containsIgnoreCase(sessionType,'import')==true}">

	var loadDataButton = new Ext.Button({
		text:'Load Data'
	});

	loadDataButton.on('click', function() {
		openLabViewTab('${session.gid}', loadDataCheckbox.getValue());
	}, this);

	var loadDataCheckbox = new Ext.form.Checkbox({
		labelSeparator:''
	});	

	var loadDataCheckboxLabel = new Ext.form.Label({
		text:' Explore and load data in new window?',
		cls:'x-form-item-label',
		width:200
	});

	var loadDataForm = new Ext.form.FormPanel({
		items: [{
			layout: 'column',
			defaults: {
				layout: 'form',
				border: false
			},
			items: [{
				columnWidth: 0.3,
				items: [ loadDataCheckbox ]
			},{
				columnWidth: 0.7,
				items: [ loadDataCheckboxLabel ]
			}],
			cls:'x-form-item',
			border: false
		}],
		buttons: [ loadDataButton ],
		buttonAlign: 'center',
		labelWidth: 85,
		border: false
	});

	var loadDataPanel = new Ext.Panel({
		title:"Load",
		items: [ loadDataForm ],
		style: { "margin":"10px" },
		width: 400
	});

	addItemModelViewTab(loadDataPanel, true);
	</c:if>

});
</script>
</div>
