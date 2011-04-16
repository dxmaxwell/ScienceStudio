/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *      Ext.ss.core.ProjectsGridPanel class.
 *     
 */
Ext.namespace('Ext.ss.core');

Ext.ss.core.ProjectsGridPanel = function(config) {

	var gridPanelConfig = Ext.apply({}, config);
	
	if(!gridPanelConfig.store) {
		gridPanelConfig.store = new Ext.data.JsonStore({
			autoDestroy: true,
			root:'rows',
			fields:[
			   { name:'id', mapping:'project.id' },
			   { name:'name', mapping:'project.name' },
			   { name:'startDate', mapping:'project.startDate', type:'date', dateFormat:Date.patterns.ISO8601Full },
			   { name:'endDate', mapping:'project.endDate', type:'date', dateFormat:Date.patterns.ISO8601Full },
		       { name:'status', mapping:'project.status' }
		    ]
		});
	}
	
	if(!gridPanelConfig.sm) {
		gridPanelConfig.sm = new Ext.grid.RowSelectionModel({
			singleSelect:true
		});
	}
		
	gridPanelConfig.columns = [
	    {header: "Name", width: 180, dataIndex: 'name', sortable: true},
	    {header: "Start Date", width: 100, dataIndex: 'startDate', sortable: true, xtype:'datecolumn', format:Date.patterns.ISO8601Short },
	    {header: "End Date", width: 100, dataIndex: 'endDate', sortable: true,  xtype:'datecolumn', format:Date.patterns.ISO8601Short },
	    {header: "Status", width: 50, dataIndex: 'status', sortable: false } 
	];
	
	Ext.ss.core.ProjectsGridPanel.superclass.constructor.call(this, gridPanelConfig);
};

Ext.extend(Ext.ss.core.ProjectsGridPanel, Ext.grid.GridPanel);
