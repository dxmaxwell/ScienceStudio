<%--
	Copyright (c) Canadian Light Source, Inc. All rights reserved.
	- see license.txt for details.
	
	Description:
		edit.jsp
--%>
<%@ include file="/WEB-INF/jsp/include/taglibs.jsp"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" trimDirectiveWhitespaces="true"%>
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
			
			var projectNameFld = new Ext.form.TextField({
				name:'projectName',
				fieldLabel: 'Project',
				value: '${laboratorySession.projectName}',
				disabled:true
			});

			var sessionNameFld = new Ext.form.TextField({
				name:'sessionName',
				fieldLabel: 'Session',
				value: '${laboratorySession.sessionName}',
				disabled:true
			});

			/*<spring:bind path="laboratorySession.proposal">*/
			var proposalFld = new Ext.form.TextField({
				name:'${status.expression}',
				fieldLabel: 'Proposal',
				value: '${status.value}',
				msgTarget: 'side'
			});
			/*<c:if test="${not empty status.errorMessage}">*/
			proposalFld.on('valid', function(field) {
				field.markInvalid('${status.errorMessage}');
			});
			/*</c:if>*/
			/*</spring:bind>*/
			
			/*<spring:bind path="laboratorySession.startDate">*/
			var startDateFld = new Ext.form.DateField({
				name: '${status.expression}',
				fieldLabel: 'Start',
				value: '${status.value}',
				format: Date.patterns.ISO8601Date,
				msgTarget: 'side'
			});
			/*<c:if test="${not empty status.errorMessage}">*/
			startDateFld.on('valid', function(field) {
				field.markInvalid('${status.errorMessage}');
			});
			/*</c:if>*/
			/*</spring:bind>*/
			
			/*<spring:bind path="laboratorySession.startTime">*/
			var startTimeFld = new Ext.form.TimeField({
				name: '${status.expression}',
				fieldLabel: 'Start Time',
				value: '${status.value}',
				format: Date.patterns.ISO8601TimeShrt,
				hideLabel: true,
				msgTarget: 'side',
				width: 72
			});
			/*<c:if test="${not empty status.errorMessage}">*/
			startTimeFld.on('valid', function(field) {
				field.markInvalid('${status.errorMessage}');
			});
			/*</c:if>*/
			/*</spring:bind>*/
			
			/*<spring:bind path="laboratorySession.endDate">*/
			var endDateFld = new Ext.form.DateField({
				name: '${status.expression}',
				fieldLabel: 'End',
				value: '${status.value}',
				format: Date.patterns.ISO8601Date,
				msgTarget: 'side'
			});
			/*<c:if test="${not empty status.errorMessage}">*/
			endDateFld.on('valid', function(field) {
				field.markInvalid('${status.errorMessage}');
			});
			/*</c:if>*/
			/*</spring:bind>*/

			/*<spring:bind path="laboratorySession.endTime">*/
			var endTimeFld = new Ext.form.TimeField({
				name: '${status.expression}',
				fieldLabel: 'End Time',
				value: '${status.value}',
				format: Date.patterns.ISO8601TimeShrt,
				hideLabel: true,
				msgTarget: 'side',
				width: 72
			});
			/*<c:if test="${not empty status.errorMessage}">*/
			endTimeFld.on('valid', function(field) {
				field.markInvalid('${status.errorMessage}');
			});
			/*</c:if>*/
			/*</spring:bind>*/
			
			var statusFld = new Ext.form.TextField({
				name: 'status',
				fieldLabel: 'Status',
				value: '${laboratorySession.status}',
				disabled:true
			});
			
			var updateBtn = new Ext.Button({
				text: 'Update',
				handler: function() {
					var updater = new Ext.Updater('MAIN_PANEL');
					if(updater) {
						updater.formUpdate(
							'laboratorySessionEditForm',
							'session/${laboratorySession.sessionId}/edit.html'
						);
					}
				}
			});
			
			var cancelBtn = new Ext.Button({
				text: 'Cancel',
				handler: function() {
					var updater = new Ext.Updater('MAIN_PANEL');
					if(updater) {
						updater.update({
							url:'session/${laboratorySession.sessionId}/show.html',
							scripts:true
						});
					}
				}
			});
			
			var labSessionPanel =  new Ext.form.FormPanel({
				formId:'laboratorySessionEditForm',
				title: 'Edit VESPERS Session',
				labelAlign: 'right',
				labelWidth: 60,			
				items: [
					projectNameFld,
					sessionNameFld,
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
				buttonAlign:'center',
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