<%--
	Copyright (c) Canadian Light Source, Inc. All rights reserved.
	- see license.txt for details.
	
	Description:
		show.jsp
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
				'Cannot Show Laboratory Session.';
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
				fieldLabel: 'Project',
				value: '${laboratorySession.projectName}'
			});

			var sessionNameFld = new Ext.form.TextField({
				fieldLabel: 'Session',
				value: '${laboratorySession.name}'
			});

			var proposalFld = new Ext.form.TextField({
				fieldLabel: 'Proposal',
				value: '${laboratorySession.proposal}'
			});

			var startDateFld = new Ext.form.TextField({
				fieldLabel: 'Start',
				value: Date.parseDate('${laboratorySession.startDay}T${laboratorySession.startTime}', 'c').format('Y-m-d H:i')
			});
						
			var endDateFld = new Ext.form.TextField({
				fieldLabel: 'End', 
				value: Date.parseDate('${laboratorySession.endDay}T${laboratorySession.endTime}', 'c').format('Y-m-d H:i')
			});

			var statusFld = new Ext.form.TextField({
				fieldLabel: 'Status',
				value: '${laboratorySession.status}'
			});

			var msgPanel = new Ext.Panel({
				/*<c:if test="${! empty message}">*/
				html: '${message}',
				/*</c:if>*/
				style: {
					'color':'green',
					'font-size':'larger',
					'text-align':'center'
				},
				width: 'auto', height: 'auto',
				border: false
			});

			var errPanel = new Ext.Panel({
				/*<c:if test="${! empty errorMessage}">*/
				html: '${errorMessage}',
				/*</c:if>*/
				style: {
					'color':'red',
					'font-size':'larger',
					'text-align':'center'
				},
				width: 'auto', height: 'auto',
				border: false
			});
			
			var startBtn = new Ext.Button({
				text: 'Start',
				handler: function() {
					Ext.get('MAIN_PANEL').getUpdater().update({
						url:'session/${laboratorySession.gid}/start.html',
						text: 'Starting Session...',
						scripts:true,
						timeout:60
					});
				}
			});

			var stopBtn = new Ext.Button({
				text: 'Stop',
				handler: function() {
					Ext.get('MAIN_PANEL').getUpdater().update({
						url: 'session/${laboratorySession.gid}/stop.html',
						text: 'Stopping Session...',
						scripts:true,
						timeout:60
					});
				}
			});

			var editBtn = new Ext.Button({
				text:'Edit',
				handler: function() {
					Ext.get('MAIN_PANEL').getUpdater().update({
						url:'session/${laboratorySession.gid}/edit.html',
						scripts:true
					});
				}
			});
			
			/*<c:choose><c:when test="${runningSessionGid == laboratorySession.gid}">*/
			startBtn.hide();
			editBtn.hide(); 
			/*</c:when><c:when test="${runningSessionGid == '0'}">*/
			stopBtn.hide();
			/*</c:when><c:otherwise>*/
			startBtn.hide();
			stopBtn.hide();
			/*</c:otherwise></c:choose>*/
			
			var labSessionPanel =  new Ext.form.FormPanel({
				title: 'Laboratory Session',
				labelAlign: 'right',
				labelWidth: 60,
				defaults: {
					disabled:true
				},
				items: [
					projectNameFld,
					sessionNameFld,
					proposalFld,
					startDateFld,
					endDateFld,
					statusFld,
					{
						xtype:'panel',
						items:[
							msgPanel,
							errPanel
						],
						disabled:false
					}
				],
				buttons: [ startBtn, stopBtn, editBtn ],
				buttonAlign:'center',
				style:'padding-top: 40px; padding-left: 50px;',
				width: 325, height: 'auto',
				frame: true
			});

			/*</c:otherwise></c:choose>*/

			var backPanel = new Ext.Panel({
				html:	'<a href="laboratorySessions" onclick="Ext.get(\'MAIN_PANEL\').getUpdater().update({url:\'sessions.html\',scripts:true}); return false;">back</a>',
				style:'padding-top: 5px; padding-left: 50px;',
				width: 'auto', height: 'auto',
				border: false
			});
			
			var labSessionShow = new Ext.Panel({
				items: [labSessionPanel, backPanel],
				width: 'auto', height: 'auto',
				border: false,
				applyTo: 'laboratorySessionShow'
			});
		});
	</script>
	<div id="laboratorySessionShow"></div>
</div>