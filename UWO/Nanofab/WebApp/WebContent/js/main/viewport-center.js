/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *  	viewport-center.js
 *
 */

var labelWidth = 80;
var fieldWidth = 175;
var txtFldWidth = 175;

/************************** Laboratory Session Panel **************************/
var lsProjectNameFld = new Ext.form.TextField({
	name: 'projectName',
	fieldLabel: 'Project',
	width: fieldWidth,
	readOnly: true,
	cls: 'ss-form-field-label',
	value: 'N/A'
});

var lsSessionNameFld = new Ext.form.TextField({
	name: 'sessionName',
	fieldLabel: 'Session',
	width: fieldWidth,
	readOnly: true,
	cls: 'ss-form-field-label',
	value: 'N/A'
});

var lsExperimentNameFld = new Ext.form.TextField({
	name: 'experimentName',
	fieldLabel: 'Experiment',
	width: fieldWidth,
	readOnly: true,
	cls: 'ss-form-field-label',
	value: 'N/A'
});

var lsSessionTimeoutFld = new Ext.form.TextField({
	name: 'sessionTimeoutHHMM',
	fieldLabel: 'Timeout',
	width: fieldWidth,
	readOnly: true,
	cls: 'ss-form-field-label',
	value: 'N/A'
});

var lsPersonNameFld = new Ext.form.TextField({
	name: 'controllerName',
	fieldLabel: 'Controller',
	width: fieldWidth,
	readonly: true,
	cls: 'ss-form-field-label',
	value: 'N/A'
});

var laboratorySessionForm = new Ext.FormPanel({
    labelAlign: 'left',
    labelWidth: labelWidth,
	items: [
	     lsProjectNameFld,
	     lsSessionNameFld,
	     lsExperimentNameFld,
	     lsSessionTimeoutFld,
	     lsPersonNameFld
	],
	border: false
});

var laboratorySessionPanel = new Ext.Panel({
	title: 'Session',
	collapsible: false,
	items:[ laboratorySessionForm ],
	margins:'0px 10px',
	padding:'5px',
	width:275
});

var laboratorySessionFormLoad = function() {
	if(heartbeatData.laboratorySession) {
		var lsData = heartbeatData.laboratorySession;
		
		var sessionRunning;
		if(lsData.sessionTimeout > 0) {
			lsData.sessionTimeoutHHMM = convertSecondsToHHMM(lsData.sessionTimeout);
			sessionRunning = true;
		} else {
			lsData.sessionTimeoutHHMM = '0:00';
			sessionRunning = false;
		}
		
		laboratorySessionForm.getForm().setValues(lsData);
		
		if(sessionRunning) {
			if(lsData.controllerGid == personGid) {
				showSessionController();
			} else {
				showSessionObserver();
			}
		} else {
			showSessionStopped();
		}
	}
};

var laboratorySessionTask = {
	run: laboratorySessionFormLoad,
	interval: heartbeatInterval
};

heartbeatTasksToStart.push(laboratorySessionTask);

/************************** Laboratory Session Control Panel **************************/

var lsControlBtn = new Ext.Button({
	text: 'Control Session',
	handler: function(button, event) {	
		var w = window.open('session/control.html', 'NANOFAB_VNC_VIEWER_WINDOW', 'scrollbars=yes', false);
		if(w) { w.focus(); }
	}
});

var lsControlBtnPanel = new Ext.Panel({
	items: [{
		html: 'Take control of session and open remote desktop with full access.',
		bodyStyle: 'font-size: 16px; text-align: center;',
		border: false
	}],
	buttons: [ lsControlBtn ],
	buttonAlign: 'center',
	padding:'5px',
	border: false
});

var lsObserveBtn = new Ext.Button({
	text: 'Observe Session',
	handler: function(button, event) {	
		var w = window.open('session/observe.html', 'NANOFAB_VNC_VIEWER_WINDOW', 'scrollbars=yes', false);
		if(w) { w.focus(); }
	}
});

var lsObserveBtnPanel = new Ext.Panel({
	items: [{
		html: 'Open remote desktop with view-only access.',
		bodyStyle: 'font-size: 16px; text-align: center;',
		border: false
	}],
	buttons: [ lsObserveBtn ],
	buttonAlign: 'center',
	padding:'5px',
	border: false
});

var lsControlPanel = new Ext.Panel({
	title:'Control',
	items:[
	    lsObserveBtnPanel,
	    lsControlBtnPanel
	],
	margins:'0px 10px',
	padding:'5px',
	width:575
});

/******************* Laboratory Session Upper Panel *******************/

var lsUpperPanel = new Ext.Panel({
	title: 'Manage Session',
	layout: {
		type:'hbox',
		align:'stretchmax'
	},
	items:[
	    laboratorySessionPanel,
	    lsControlPanel
	],
	padding:'10px',
	border: false
});

/************************** Laboratory Session Scan Panel **************************/

var lsExperimentStore = new Ext.data.JsonStore({
	url: 'experiments.json',
	root:'experiments',
	successProperty: 'success',
	messageProperty: 'message',
	fields:[ 'gid', 'name' ],
	listeners: {
		'exception':dataStoreExceptionHandler
	}
});

var lsExperimentField = new Ext.form.ComboBox({
	hideLabel: true,
	hiddenName:'experimentGid',
	store: lsExperimentStore,
	emptyText: 'Select an Experiment',
	mode: 'remote',
	editable: false,
	triggerAction: 'all',
	forceSelection: true,
	displayField: 'name',
	valueField: 'gid',
	border: false,
	width:200,
	listeners:{
		'beforequery':function(queryEvent) {
			delete queryEvent.combo.lastQuery;
		},
		'select':function(combo, record, index) {
			lsExperimentFormPanel.getForm().submit({
				url: 'session.json',
				failure:function(form, action) {
					lsExperimentField.clearValue();
					formActionFailureHandler(form, action);
				},
				scope:this
			});
		},
		scope:this
	}
});

var lsExperimentFormPanel = new Ext.FormPanel({
	defaults: {
		border: false
	},
	items: [{
		xtype:'box',
		html:'Select an Experiment to<br/>associate with new Scans.',
		style:'font-size: 16px; text-align: center;'
	}, {
		items: [ lsExperimentField ],
		style: 'margin: 7px auto 0px auto;',
		width: 205
	}],
	style: 'margin: 10px 5px 0px;',
	border: false
});

var lsCreateScanBtn = new Ext.Button({
	text: 'Create Scan',
	handler: function() {
		createScanWindow.show();
	},
	scope:this
});	
	
var lsCreateScanBtnPanel = new Ext.Panel({
	items: [{
		html: 'Create Scan with the selected files.',
		bodyStyle: 'font-size: 16px; text-align: center;',
		border: false
	}],
	buttons:[ lsCreateScanBtn ],
	buttonAlign: 'center',
	style: 'margin: 40px 5px 0px;',
	border: false
});

var createScanFormScanNameFld = new Ext.form.TextField({
	name: 'scanName',
	hideLabel: true,
	width: 300
});

var acceptCreateScanBtn = new Ext.Button({
	text: 'OK',
	handler: function(button, event) {
		var scanFiles = [];
		var records = lsSelectedFileStore.getRange();
		for(var idx=0; idx<records.length; idx++) {
			scanFiles.push(records[idx].get('path'));
		}
		if(scanFiles.length == 0) {
			Ext.Msg.alert('Message', 'Please select at least one data file.');	
		}
		else {
			createScanForm.getForm().submit({
				url: 'scan/create.json',
				params: {
					'scanFiles': scanFiles
				},
				success:function(form, action) {
					createScanWindow.close();
					Ext.Msg.alert("Message", "Scan created successfully.");
				},
				failure:formActionFailureHandler,	
				scope:this
			});
		}
	},
	scope:this
});

var cancelCreateScanBtn = new Ext.Button({
	text: 'Cancel',
	handler: function() {
		createScanWindow.close();
	},
	scope:this
});

var createScanForm = new Ext.form.FormPanel({
	items: [
	   createScanFormScanNameFld
	],
	buttons: [
	   acceptCreateScanBtn,
	   cancelCreateScanBtn
	],
	buttonAlign: 'left',
	border: false
});

var createScanWindow = new Ext.Window({
	title: 'Enter Scan Name',
	items: [ createScanForm ],
	hidden: true,
	listeners: {
		'beforeclose':function(window) {
			window.hide();
			return false;
		}
	}
});

var lsCreateScanPanel = new Ext.Panel({
	title: 'Scan',
	items: [
	    lsExperimentFormPanel,
	    lsCreateScanBtnPanel
	],
	margins:'0px 10px',
	width:300
});

/************* Laboratory Session File Panel *******************/

var lsDirectoryStore = new Ext.data.JsonStore({
    url: 'share/directories.json',
    root: 'directories',
    successProperty: 'success',
    messageProperty: 'message',
    fields: [ 'name', 'path', 'size' ],
    listeners: {
		'exception': dataStoreExceptionHandler
	}
});

var lsDirectoryCbx = new Ext.form.ComboBox({
	fieldLabel: 'Directory',
	store: lsDirectoryStore,
	emptyText: 'Select a Directory',
	mode: 'remote',
	editable:false,
	triggerAction: 'all',
	forceSelection: true,
	displayField: 'path',
	valueField: 'path',
	width: 200,
	listeners:{
		'beforequery':function(queryEvent) {
			delete queryEvent.combo.lastQuery;
		},
		'select':function(cbx, record, idx ) {
			var path = record.get('path');
			if(path) {
				lsFileStore.load({
					params: { 
						'dir':path 
					},
					add: false
				});
			}
		}
	}
});

var lsFileStore = new Ext.data.JsonStore({
	url: 'share/files.json',
	root: 'files',
    successProperty: 'success',
    messageProperty: 'message',
    fields: [ 'name', 'path', 'size' ],
    sortInfo: { 
		field: 'name',
		direction: 'ASC'
	},
	listeners: {
		'exception':dataStoreExceptionHandler
	}
});

var lsFileReloadButton = new Ext.Button({
	text:'Refresh',
	handler:function() {
		if(lsFileStore.lastOptions) {
			lsFileStore.reload();
		}
	},
	scope:this
});

var lsFileGridSM = new Ext.grid.RowSelectionModel({
	singleSelect: true,
	listeners: {
		'rowselect':function(sm, rowIdx, record) {
			var path = record.get('path');
			var records = lsSelectedFileStore.query('path', path);
			if(records.getCount() == 0) {	
				lsSelectedFileStore.addSorted(record);
				lsSelectedFileStore.commitChanges();
			} else {
				Ext.Msg.alert('Warning', 'File has already been selected.');
			}
			sm.clearSelections();
		}
	}
});

var lsFileGridPanel = new Ext.grid.GridPanel({
	sm: lsFileGridSM,
	store: lsFileStore,
	columns: [{
		header: 'Name',
		dataIndex: 'name',
		sortable: true
	}, { 
		header: 'Size',
		dataIndex: 'size',
		sortable: false,
		width: 50
    }],
    viewConfig: {
        forceFit: true
	},
	flex:1
});

var lsFilePanel = new Ext.Panel({
	title:'LEO1540XB Data Files',
	layout:{
		type:'vbox',
		align:'stretch'
	},
	items:[{
		layout:'hbox',
		defaults:{
			margins:'5px 5px',
			border:false
		},
		items:[{
			layout:'form',
			labelPad:10,
			labelWidth:50,
			items:[ lsDirectoryCbx ],
			border:false
		},{
			items:[ lsFileReloadButton ]
		}]
	}, 
		lsFileGridPanel,
	{
		html: 'Click file to select for Scan.',
		bodyStyle: 'font-size: 14px',
		border: false
	}],
	margins:'0px 10px',
	border:false,
	height:300,
	width:350
});

/************* Laboratory Session Selected File Panel *******************/

var lsSelectedFileStore = new Ext.data.ArrayStore({
	autoLoad: false,
	fields: [
	    'name', 'path', 'size'
	],
	sortInfo: { 
		field: 'path',
		direction: 'ASC'
	}
});

var lsSelectedFileGridSM = new Ext.grid.RowSelectionModel({
	singleSelect: true,
	listeners:{
		'rowselect':function(sm, rowIdx, record) {
			lsSelectedFileStore.remove(record);
			lsSelectedFileStore.commitChanges();
			sm.clearSelections();
		}
	}
});

var lsSelectedFileGridPanel = new Ext.grid.GridPanel({
	sm: lsSelectedFileGridSM,
	store: lsSelectedFileStore,
	columns: [{
		header: 'Path',
		dataIndex: 'path',
		sortable: true 
	}, { 
		header: 'Size',
		dataIndex: 'size',
		sortable: false,
		width: 50
    }],
    viewConfig: {
        forceFit: true
	},
	flex:1
});

var lsSelectedFilePanel = new Ext.Panel({
	title: 'Selected Data Files',
	layout:'vbox',
	items:[
		lsSelectedFileGridPanel,	
	{
	    html: 'Click file to de-select for Scan.',
	    bodyStyle: 'font-size: 14px',
	    border: false	
	}],
	margins:'0px 10px',
	border:false,
	height:300,
	width:300
});

/********************** Laboratory Session Lower Panel **********************/

var lsLowerPanel = new Ext.Panel({
	title: 'Manage Data',
	layout:{
		type:'hbox'
	},
	items:[
	 	lsFilePanel,
	 	lsSelectedFilePanel,
	 	lsCreateScanPanel
	],
	padding:'10px'
});

/*********************** Laboratory Session Viewport ********************/

var centerPanel = new Ext.Panel( {
	region: 'center',
	items: [ lsUpperPanel, lsLowerPanel ],
	autoScroll: true
});
