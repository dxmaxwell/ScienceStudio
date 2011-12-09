<%-- 
	Copyright (c) Canadian Light Source, Inc. All rights reserved.
	- see license.txt for details.
	
	Description:
		Scan html fragment.
--%>
<%@ page session="true" language="java"  contentType="text/xml; charset=UTF-8" pageEncoding="UTF-8" trimDirectiveWhitespaces="true"%>
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
			autoEl:{
				tag: "a",
				href:"#experiment${experiment.gid}",
				onclick: "return loadModelViewTab(ModelPathUtils.getModelExperimentPath('/${experiment.gid}.html'));",
				html:"${experiment.name}"
			}
		},{
			xtype:"box",
			html: "&raquo;"
		},{
			xtype:"box",
			autoEl:{
				tag: "a",
				href:"#scans${experiment.gid}",
				onclick: "return loadModelViewTab(ModelPathUtils.getModelScanPath('.html?experiment=${experiment.gid}'));",
				html:"Scans"
			}
		},{
			xtype:"box",
			html: "&raquo;"
		},{
			xtype:"box",
			html: "Scan"
		}],
		bodyCssClass:"ss-navigation-trail"
	});
	
	addItemModelViewTab(navigationPanel);
<%--

	Edit Scan Form
--%>
	var scanForm = new Ext.ss.core.ScanFormPanel({
		url: ModelPathUtils.getModelScanPath('/form/edit.json'),
		method: 'POST',
		submitText: 'Save',
		labelAlign: 'right',
		buttonAlign: 'center',
		defaults: {
			disabled:true
		},
		buttonDefaults: {
			hidden:true
		},
		border: false,
		waitMsg:'Saving Session...',
		waitMsgTarget: true,
		padding: '5 5 5 5'
	});

	<sec:authorize ifContainsAny="SESSION_EXPERIMENTER,FACILITY_ADMIN_SESSIONS">
	scanForm.ss.fields.name.setDisabled(false);
	scanForm.ss.buttons.submit.setVisible(true);
	</sec:authorize>

	<c:if test="${not empty scan}">
	var scanFormData = <hmc:write source="${scan}"/>

	if(scanFormData.startDate) {
		var startDate = Date.parseDate(scanFormData.startDate, 'c');
		if(startDate) {
			scanFormData.startDate = startDate.format(Date.patterns.ISO8601Long);
		}
	}

	if(scanFormData.endDate) {
		var endDate = Date.parseDate(scanFormData.endDate, 'c');
		if(endDate) {
			scanFormData.endDate = endDate.format(Date.patterns.ISO8601Long);
		}
	}

	scanForm.getForm().setValues(scanFormData);
	</c:if>	


	var scanPanel = new Ext.Panel({
		title:'Scan',
		items:[ scanForm ],
		style:{ "margin":"10px" },
		width:600
	});

	addItemModelViewTab(scanPanel);

	<%--
	
		View Scan Data Form
	--%>
	var viewScanDataButton = new Ext.Button({
		text:'View Data'
	});

	viewScanDataButton.on('click', function() {
		openDataViewTab('${scan.gid}', viewScanDataCheckbox.getValue());
	}, this);

	var viewScanDataCheckbox = new Ext.form.Checkbox({
		labelSeparator:''
	});

	var viewScanDataCheckboxLabel = new Ext.form.Label({
		text:' View scan data in new window?',
		cls:'x-form-item-label',
		width:200
	});

	var viewScanDataForm = new Ext.form.FormPanel({
		items: [{
			layout: 'column',
			defaults: {
				layout: 'form',
				border: false
			},
			items: [{
				columnWidth: 0.3,
				items: [ viewScanDataCheckbox ]
			},{
				columnWidth: 0.7,
				items: [ viewScanDataCheckboxLabel ]
			}],
			cls:'x-form-item',
			border: false
		}],
		buttons: [ viewScanDataButton ],
		buttonAlign: 'center',
		labelWidth: 85,
		border: false
	});

	var viewScanDataPanel = new Ext.Panel({
		title:"Data",
		items: [ viewScanDataForm ],
		style: { "margin":"10px" },
		width: 400
	});

	addItemModelViewTab(viewScanDataPanel, true);
});
</script>
</div>
