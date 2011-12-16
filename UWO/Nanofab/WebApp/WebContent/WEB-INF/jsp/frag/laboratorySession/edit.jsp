<%@ include file="/WEB-INF/jsp/include/taglibs.jsp"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<div>
	<script type="text/javascript">
		Ext.onReady(function() {
			/*<c:choose><c:when test="${empty laboratorySession}">*/
			
			var errorTitle = 
			/*<c:choose><c:when test="${empty errorTitle}">*/
				'Error';
			/*</c:when><c:otherwise>*/
				'${errorTitle}';
			/*</c:otherwise></c:choose>*/
			
			var errorMessage =
			/*<c:choose><c:when test="${empty errorMessage}">*/
				'Cannot Edit Laboratory Session.';
			/*</c:when><c:otherwise>*/
			 	'${errorMessage}';
			/*</c:otherwise></c:choose>*/
				
			var labSessionPanel = new Ext.Panel({
				title: errorTitle,
				html: errorMessage,
				style:'padding-top: 40px; padding-left: 50px;',
				width: 400, height: 'auto',
				frame: true
			});
		
			/*</c:when><c:otherwise>*/
			
			var gidFld = new Ext.form.Hidden({
				name:'${gid}',
				value:'${laboratorySession.gid}'
			});
			
			var nameFld = new Ext.form.TextField({
				name:'name',
				fieldLabel: 'Session',
				value: '${laboratorySession.name}',
				cls:'x-item-disabled',
				readOnly:true
			});
			
			var descriptionFld = new Ext.form.Hidden({
				name:'description',
				value:'${laboratorySession.description}'
			});
			
			var laboratoryGidFld = new Ext.form.Hidden({
				name:'laboratoryGid',
				value:'${laboratorySession.laboratoryGid}'
			});
			
			var projectGidFld = new Ext.form.Hidden({
				name:'projectGid',
				value:'${laboratorySession.projectGid}'
			});
			
			var projectNameFld = new Ext.form.TextField({
				name:'projectName',
				fieldLabel: 'Project',
				value: '${laboratorySession.projectName}',
				cls:'x-item-disabled',
				readOnly:true
			});

			/*<spring:bind path="laboratorySession.proposal">*/
			var proposalFld = new Ext.form.TextField({
				name:'${status.expression}',
				fieldLabel: 'Proposal',
				value: '${status.value}',
				msgTarget: 'side'
			});
			/*<c:forEach items="${status.errorMessages}" var="msg" varStatus="forEachStatus"><c:if test="${forEachStatus.last}">*/
			proposalFld.on('valid', function(field) { field.markInvalid('${msg}'); });
			/*</c:if></c:forEach>*/
			/*</spring:bind>*/
			
			/*<spring:bind path="laboratorySession.startDay">*/
			var startDateFld = new Ext.form.DateField({
				name: '${status.expression}',
				fieldLabel: 'Start',
				value: '${status.value}',
				format: Date.patterns.ISO8601Date,
				msgTarget: 'side'
			});
			/*<c:forEach items="${status.errorMessages}" var="msg" varStatus="forEachStatus"><c:if test="${forEachStatus.last}">*/
			startDateFld.on('valid', function(field) { field.markInvalid('${msg}'); });
			/*</c:if></c:forEach>*/
			/*</spring:bind>*/
			
			/*<spring:bind path="laboratorySession.startTime">*/
			var startTimeFld = new Ext.form.TimeField({
				name: '${status.expression}',
				fieldLabel: 'Start Time',
				value: '${status.value}',
				format: Date.patterns.ISO8601TimeShrt,
				altFormats: Date.patterns.ISO8601TimeFull,
				hideLabel: true,
				msgTarget: 'side',
				width: 72
			});
			/*<c:forEach items="${status.errorMessages}" var="msg" varStatus="forEachStatus"><c:if test="${forEachStatus.last}">*/
			startTimeFld.on('valid', function(field) { field.markInvalid('${msg}'); });
			/*</c:if></c:forEach>*/
			/*</spring:bind>*/
			
			/*<spring:bind path="laboratorySession.endDay">*/
			var endDateFld = new Ext.form.DateField({
				name: '${status.expression}',
				fieldLabel: 'End',
				value: '${status.value}',
				format: Date.patterns.ISO8601Date,
				msgTarget: 'side'
			});
			/*<c:forEach items="${status.errorMessages}" var="msg" varStatus="forEachStatus"><c:if test="${forEachStatus.last}">*/
			endDateFld.on('valid', function(field) { field.markInvalid('${msg}'); });
			/*</c:if></c:forEach>*/
			/*</spring:bind>*/

			/*<spring:bind path="laboratorySession.endTime">*/
			var endTimeFld = new Ext.form.TimeField({
				name: '${status.expression}',
				fieldLabel: 'End Time',
				value: '${status.value}',
				format: Date.patterns.ISO8601TimeShrt,
				altFormats: Date.patterns.ISO8601TimeFull,
				hideLabel: true,
				msgTarget: 'side',
				width: 72
			});
			/*<c:forEach items="${status.errorMessages}" var="msg" varStatus="forEachStatus"><c:if test="${forEachStatus.last}">*/
			endTimeFld.on('valid', function(field) { field.markInvalid('${msg}'); });
			/*</c:if></c:forEach>*/
			/*</spring:bind>*/
			
			var statusFld = new Ext.form.TextField({
				name: 'status',
				fieldLabel: 'Status',
				value: '${laboratorySession.status}',
				cls:'x-item-disabled',
				readOnly:true
			});
			
			var updateBtn = new Ext.Button({
				text: 'Update',
				handler: function() {
					var updater = new Ext.Updater('mainPanel');
					if(updater) {
						updater.formUpdate(
							'laboratorySessionEditForm',
							'session/${laboratorySession.gid}/edit.html'
						);
					}
				}
			});
			
			var cancelBtn = new Ext.Button({
				text: 'Cancel',
				handler: function() {
					var updater = new Ext.Updater('mainPanel');
					if(updater) {
						updater.update({
							url:'session/${laboratorySession.gid}/show.html',
							scripts:true
						});
					}
				}
			});
			
			var labSessionPanel =  new Ext.form.FormPanel({
				formId:'laboratorySessionEditForm',
				title: 'Edit Session',
				labelAlign: 'right',
				labelWidth: 60,
				items: [
				    projectGidFld,
					projectNameFld,
					gidFld,
					nameFld,
					descriptionFld,
					laboratoryGidFld,
					proposalFld,
					{
						layout: 'column',
						defaults: {
							layout: 'form'
						},
						items: [
							{items:startDateFld, columnWidth:0.65}, 
							{items:startTimeFld, columnWidth:0.35}	
						]
					}, {
						layout: 'column',
						defaults: {
							layout: 'form'
						},
						items: [
							{items:endDateFld, columnWidth:0.65},
							{items:endTimeFld, columnWidth:0.35}
						]
					},
					statusFld
				],
				buttons: [
					updateBtn, cancelBtn
				],
				buttonAlign: 'center',
				style:'padding-top: 40px; padding-left: 50px;',
				width: 370, height: 'auto',
				frame: true				
			});

			/*</c:otherwise></c:choose>*/
			
			var labSessionEdit = new Ext.Panel({
				items: [labSessionPanel],
				width:'auto', height:'auto',
				border: false,
				applyTo:'laboratorySessionEdit'
			});
		});
	</script>
	<div id="laboratorySessionEdit"></div>
</div>