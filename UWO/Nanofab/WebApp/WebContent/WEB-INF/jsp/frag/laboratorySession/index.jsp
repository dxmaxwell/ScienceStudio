<%@ include file="/WEB-INF/jsp/include/taglibs.jsp"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<div>
	<script type="text/javascript">

		Ext.onReady(function() {
			
			var laboratorySessionFields = [
				{name: 'gid', type: 'string'},
				{name: 'name', type: 'string'},
				{name: 'projectGid', type: 'string' },
				{name: 'projectName', type: 'string'},
				{name: 'proposal', type: 'string'},
				{name: 'startDateTime', type: 'date', dateFormat: 'c'},
				{name: 'endDateTime', type: 'date', dateFormat: 'c'},
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
				handler: function() {
					Ext.get('mainPanel').getUpdater().update({
						url:'session/${runningSessionGid}/show.html',
						scripts:true
					});
				}
			});
			
			var labSessionGrid = new Ext.grid.GridPanel({
				title: 'Sessions',
				store: labSessionStore,
				columns: [
					{header: "Project", width: 150, sortable: true, dataIndex: 'projectName'},
					{header: "Session", width: 150, sortable: true, dataIndex: 'name'},
					{header: "Proposal", width: 80, sortable: true, dataIndex: 'proposal'},
					{header: "Start", width: 120, sortable: true, renderer: Ext.util.Format.dateRenderer('Y-m-d H:i'), dataIndex: 'startDateTime'},
					{header: "End", width: 120, sortable: true, renderer: Ext.util.Format.dateRenderer('Y-m-d H:i'), dataIndex: 'endDateTime'},
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
								
								var updater = new Ext.Updater('mainPanel');
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