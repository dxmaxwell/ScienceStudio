<%-- 
	Copyright (c) Canadian Light Source, Inc. All rights reserved.
	- see license.txt for details.
	
	Description:
		Scan html fragment.
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
			autoEl:{
				tag: "a",
				href:"#experiment${experiment.id}",
				onclick: "return loadModelViewTab(ModelPathUtils.getExperimentPath(${experiment.id}, '.html'));",
				html:"${experiment.name}"
			}
		},{
			xtype:"box",
			html: "&raquo;"
		},{
			xtype:"box",
			autoEl:{
				tag: "a",
				href:"#scans${experiment.id}",
				onclick: "return loadModelViewTab(ModelPathUtils.getScansPath(${experiment.id}, '.html'));",
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
		url: ModelPathUtils.getScanPath(${scan.id}, '/form/edit.json'),
		method: 'POST',
		submitText: 'Save',
		labelAlign: 'right',
		buttonAlign: 'center',
		<sec:authorize ifNotGranted="ROLE_ADMIN_PROJECTS,PROJECT_ROLE_EXPERIMENTER_${project.id}">
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

	<c:if test="${not empty scanFormBacker}">
	var scanFormData = 
		<jsp:include page="/WEB-INF/jsp/include/marshal-json.jsp" flush="true">  
			<jsp:param name="source" value="scanFormBacker"/>
		</jsp:include>;

	//if(scanFormData.scan.startDate) {
	//	var startDate = Date.parseDate(scanFormData.scan.startDate, Date.patterns.ISO8601Full);
	//	if(startDate) {
	//		scanFormData.scan.startDate = startDate.format(Date.patterns.ISO8601Long);
	//	}
	//}

	//if(scanFormData.scan.endDate) {
	//	var endDate = Date.parseDate(scanFormData.scan.endDate, Date.patterns.ISO8601Full);
	//	if(endDate) {
	//		scanFormData.scan.endDate = endDate.format(Date.patterns.ISO8601Long);
	//	}
	//}

	scanForm.getForm().setValues(scanFormData.scanFormBacker);
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
		openDataViewTab(${scan.id}, viewScanDataCheckbox.getValue());
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

