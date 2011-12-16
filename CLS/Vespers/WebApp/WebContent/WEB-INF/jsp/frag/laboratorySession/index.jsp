<%--
	Copyright (c) Canadian Light Source, Inc. All rights reserved.
	- see license.txt for details.
	
	Description:
		index.jsp
--%>
<%@ include file="/WEB-INF/jsp/include/taglibs.jsp"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" trimDirectiveWhitespaces="true" %>
<div>
	<script type="text/javascript">

		Ext.onReady(function() {
			
			var laboratorySessionFields = [
				{name: 'gid', type: 'string'},
				{name: 'name', type: 'string'},
				{name: 'projectGid', type: 'string'},
				{name: 'projectName', type: 'string'},
				{name: 'proposal', type: 'string'},
				{name: 'startDate', type: 'date', dateFormat:'c' },
				{name: 'endDate', type: 'date', dateFormat:'c' },
				{name: 'status', type: 'string'}
			];
		
			var laboratorySessionData = [
   			/*<c:forEach items="${laboratorySessionList}" var="laboratorySession" varStatus="status">*/
   				[
   					'${laboratorySession.gid}',
   					'${laboratorySession.name}',
   					'${laboratorySession.projectGid}',
   					'${laboratorySession.projectName}',
   					'${laboratorySession.proposal}',
   					'${laboratorySession.startDay}T${laboratorySession.startTime}',
   					'${laboratorySession.endDay}T${laboratorySession.endTime}',
   					'${laboratorySession.status}'
   				 ]/*<c:if test="${!status.last}">*/,/*</c:if>*/
   			/*</c:forEach>*/
   			];
		
			var labSessionStore = new Ext.data.SimpleStore({
				fields:laboratorySessionFields,
				data:laboratorySessionData
			}); 

			var runningBtn = new Ext.Button({
				text: 'Show Running Session',
				hidden:true
			});
			
			/*<c:if test="${runningSessionId > 0}">*/
			runningBtn.on('click', function() {
				Ext.get('MAIN_PANEL').getUpdater().update({
					url:'session/${runningSessionId}/show.html',
					scripts:true
				});
			}, this);
			
			runningBtn.show();
			/*</c:if>*/
			
			var labSessionGrid = new Ext.grid.GridPanel({
				title: '${laboratoryName} Sessions @ ${facilityName}',
				store: labSessionStore,
				columns: [
					{header: "Project", width: 150, sortable: true, dataIndex: 'projectName'},
					{header: "Session", width: 150, sortable: true, dataIndex: 'name'},
					{header: "Proposal", width: 80, sortable: true, dataIndex: 'proposal'},
					{header: "Start", width: 120, sortable: true, renderer: Ext.util.Format.dateRenderer('Y-m-d H:i'), dataIndex: 'startDate'},
					{header: "End", width: 120, sortable: true, renderer: Ext.util.Format.dateRenderer('Y-m-d H:i'), dataIndex: 'endDate'},
					{header: "Status", width: 80, sortable: true, dataIndex: 'status'}
				],
				viewConfig: {
					forceFit: true
			    },
				sm: new Ext.grid.RowSelectionModel({
					singleSelect: true,
					listeners: {
						'rowselect' : {
							fn: function(rowSelectionModel, rowIndex, record) {
								var gid = record.get('gid');
								var updater = new Ext.Updater('MAIN_PANEL');
								if(gid && updater) {
									updater.update({
										url:'session/' + gid + '/show.html',
										scripts:true
									});
								}
							}
			    		}
					}
				}),
				buttons: [runningBtn],
				style:'padding-top: 40px; padding-left: 50px;',
				width: 700, height: 400,
				frame: true
			});
			
			var labSessionIndex = new Ext.Panel({
				items: [labSessionGrid],
				width: '100%', height: '100%',
				border: false,
				applyTo:'laboratorySessionIndex'
			});
		});
	</script>
	<div id="laboratorySessionIndex"></div>
</div>