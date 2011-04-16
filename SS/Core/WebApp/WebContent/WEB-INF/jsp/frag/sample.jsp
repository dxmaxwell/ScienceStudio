<%-- 
	Copyright (c) Canadian Light Source, Inc. All rights reserved.
	- see license.txt for details.
	
	Description:
		Sample html fragment.
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
				href:"#samples${project.id}",
				onclick: "return loadModelViewTab(ModelPathUtils.getSamplesPath(${project.id}, '.html'));",
				html:"Samples"
			}
		},{
			xtype:"box",
			html: "&raquo;"
		},{
			xtype:"box",
			html: "Sample"
		}],
		bodyCssClass:"ss-navigation-trail"
	});

	addItemModelViewTab(navigationPanel);
<%--

	Edit Sample Form
--%>
	var sampleForm = new Ext.ss.core.SampleFormPanel({
		url: ModelPathUtils.getSamplePath(${sample.id}, '/form/edit.json'),
		method: 'POST',
		<sec:authorize ifNotGranted="ROLE_ADMIN_PROJECTS,PROJECT_ROLE_EXPERIMENTER_${project.id}">
		defaults: {
			disabled:true
		},
		buttonDefaults: {
			hidden:true
		},
		</sec:authorize>
		submitText: 'Save',
		labelAlign: 'right',
		buttonAlign: 'center',		
		border: false,
		waitMsg:'Saving Sample...',
		waitMsgTarget: true,
		padding: '5 5 5 5'
	});
	
	<c:if test="${not empty sampleStateList}">
	var stateStoreData = { response:
		<jsp:include page="/WEB-INF/jsp/include/marshal-json.jsp" flush="true">  
			<jsp:param name="source" value="sampleStateList"/>
		</jsp:include>
	};
	
	sampleForm.ss.fields.state.getStore().loadData(stateStoreData);
	</c:if>
	
	<c:if test="${not empty sampleFormBacker}">
	var sampleFormValues =
		<jsp:include page="/WEB-INF/jsp/include/marshal-json.jsp" flush="true">  
			<jsp:param name="source" value="sampleFormBacker"/>
		</jsp:include>;
	
	// Hack to make the checkbox group load values properly //
	sampleFormValues.sampleFormBacker.hazards = sampleFormValues.sampleFormBacker;
	//////////////////////////////////////////////////////////
	sampleForm.getForm().setValues(sampleFormValues.sampleFormBacker);
	</c:if>	

	function removeSample() {
		Ext.Msg.confirm('Question', 'Do you REALLY want to remove this sample?', function(ans) {
			if(ans == 'yes') {
				Ext.Ajax.request({
					url:ModelPathUtils.getSamplePath(${sample.id}, '/remove.json'),
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
		title:'Sample (Id:${sample.id})',
		<sec:authorize ifAnyGranted="ROLE_ADMIN_PROJECTS,PROJECT_ROLE_EXPERIMENTER_${project.id}">
		tools:[{
			id:'close',
			handler:removeSample,
			scope:this
		}],
		</sec:authorize>
		items:[
			sampleForm
		],
		style:{
			"margin":"10px"
		},
		width:400
	});

	addItemModelViewTab(panel, true);
});
</script>
</div>
