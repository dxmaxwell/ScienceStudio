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
				href:"#samples${project.gid}",
				onclick: "return loadModelViewTab(ModelPathUtils.getModelSamplePath('.html?project=${project.gid}'));",
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
		url: ModelPathUtils.getModelSamplePath('/form/edit.json'),
		method: 'POST',
		<sec:authorize ifContainsNone="PROJECT_RESEARCHER,FACILITY_ADMIN_PROJECTS">
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
	
	<c:if test="${not empty sampleStateOptions}">
	sampleForm.ss.fields.state.getStore().loadData(<hmc:write source="${sampleStateOptions}"/>);
	</c:if>
	
	<c:if test="${not empty sample}">
	var sampleFormValues = <hmc:write source="${sample}"/>;
	// Hack to make the checkbox group load values properly //
	sampleFormValues.hazards = sampleFormValues;
	//////////////////////////////////////////////////////////
	sampleForm.getForm().setValues(sampleFormValues);
	</c:if>

	function removeSample() {
		Ext.Msg.confirm('Question', 'Do you REALLY want to remove this sample?', function(ans) {
			if(ans == 'yes') {
				Ext.Ajax.request({
					method:'POST',				
					params:{ gid:'${sample.gid}' },
					url:ModelPathUtils.getModelSamplePath('/form/remove.json'),
					failure:function(response, options) {
						Ext.Msg.alert('Error', 'Network connection problem.');
					},
					success:function(response, options) {
						var json = Ext.decode(response.responseText, true);
						if(json) {
 							if(json.success) {
								loadModelViewTab(ModelPathUtils.getModelSamplePath('.html?project=${project.gid}'));
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
		title:'Sample (GID:${sample.gid})',
		<sec:authorize ifContainsAny="PROJECT_RESEARCHER,FACILITY_ADMIN_PROJECTS">
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
