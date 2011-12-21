/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *     experiment.js
 */

fieldWidth = 100;

var acquiringScanData = false;

/************************** Microscope Position Panel **************************

Removed from interface indefinitely.
 
var microInBtn = new Ext.Button({
	id: 'MICROSCOPE_IN_BTN',
	text: 'In'
});
	
var microOutBtn = new Ext.Button({
	id: 'MICROSCOPE_OUT_BTN',
	text: 'Out'
});
	
var microPosTxtFld = new Ext.form.TextField({
	fieldLabel: 'Position',
	name: 'microscopePosition',
	width: fieldWidth,
	readOnly: true,
	cls: 'ss-form-field-readonly'
});
    
var microPosForm = new Ext.FormPanel({
	border: false,
    labelAlign: 'left',
    labelWidth: 50,
    labelPad: 5,
    layoutConfig: { labelSeparator: ':' },
	items: [ microPosTxtFld ],
	buttons: [ microInBtn, microOutBtn ],
	buttonAlign: 'left',
	width: 'auto'
});

var microPosPanel = new Ext.Panel({
	title: 'Microscope Position',
	width: 'auto',
	collapsible: true,
	items: [ microPosForm ]
});
*/

/************************** HV Stage Motors Panel ************************/
var hSetPointFld = new Ext.form.NumberField({
	name: 'setPointH',
	fieldLabel: 'X',
	decimalPrecision: 6,
	width: fieldWidth
});

var hPositionFld = new Ext.form.NumberField({
	name: 'positionH',
	hideLabel: true,
	decimalPrecision: 6,
	width: fieldWidth,
	readOnly: true,
	cls: 'ss-form-field-readonly'
});

var vSetPointFld = new Ext.form.NumberField({
	name: 'setPointV',
	fieldLabel: 'V',
	decimalPrecision: 6,
	width: fieldWidth
});

var vPositionFld = new Ext.form.NumberField({
	name: 'positionV',
	hideLabel: true,
	decimalPrecision: 6,
	width: fieldWidth,
	readOnly: true,
	cls: 'ss-form-field-readonly'
});

var hvStatusFld = new Ext.form.TextField({
	name: 'status',
	fieldLabel: 'Status',
	width: fieldWidth,
	readOnly: true,
	cls: 'ss-form-field-label'
});

var moveHVStage = function() {
	hvStageForm.getForm().submit({
		url: 'sample/stage/hv.json',
		failure: function(form, action) {
			var json = Ext.decode(action.response.responseText, true);
			if(json && json.message) {
				Ext.Msg.alert("Error", json.message);
			} else {
				Ext.Msg.alert("Error", 'An unspecified error has occurred.');
			}
		}
	});
};

var moveHVBtn = new Ext.Button({
	colspan: 4,
	text: 'Move',
	handler: moveHVStage
});

var hvStageForm = new Ext.FormPanel({
	border: false,
	labelPad: 5,
	labelWidth: 10,
	layout: 'table',
	layoutConfig: {
		columns: 2
	},
	defaults: {
		layout: 'form',
		border: false,
		bodyStyle: 'padding: 2px'
	},
	items: [{
		items: [ hSetPointFld ]
	}, {
		items: [ hPositionFld ]
	}, {
		items: [ vSetPointFld ]
	}, {
		items: [ vPositionFld ]
	}, {
		colspan: 2,
		labelWidth: 35,
		items: [ hvStatusFld ]
	}],
	buttons: [
	    moveHVBtn
	],
	buttonAlign: 'left'
});

var hvStagePanel = new Ext.Panel({
	title: 'XV Position (mm)',
	collapsible: true,
	items: [ hvStageForm ]
});

var hvStageLoad = function() {
	if(heartbeatData.sampleStageHV) {
		var hvData = heartbeatData.sampleStageHV;
		if(hvData.moving != undefined) {
			if(hvData.moving) {
				hvData.status = 'MOVING';
			} else {
				hvData.status = 'STOPPED';
			}	
		} else {
			hvData.status = 'UNKNOWN';
		}
		hvStageForm.getForm().setValues(hvData);
	}
};

var hvStageLoadTask = {
	run: hvStageLoad,
	interval: heartbeatInterval,
	scope: this
};

heartbeatTasksToStart.push(hvStageLoadTask);

/************************** XYZ Stage Motors Panel *************************
 
Remove from interface indefinitely.
 
var xSetPointFld = new Ext.form.NumberField({
	id: 'xyzSetPointX',
	name: 'setPointX',
	decimalPrecision: 6,
	width: fieldWidth
});

var xPositionFld = new Ext.form.NumberField({
	id: 'xyzPositionX',
	name: 'xyzPositionX',
	decimalPrecision: 6,
	width: fieldWidth,
	readOnly: true,
	cls: 'ss-form-field-readonly'
});

var ySetPointFld = new Ext.form.NumberField({
	id: 'xyzSetPointY',
	name: 'setPointY',
	decimalPrecision: 6,
	width: fieldWidth
});

var yPositionFld = new Ext.form.NumberField({
	id: 'xyzPositionY',
	name: 'xyzPositionY',
	decimalPrecision: 6,
	width: fieldWidth,
	readOnly: true,
	cls: 'ss-form-field-readonly'
});

var zSetPointFld = new Ext.form.NumberField({
	id: 'xyzSetPointZ',
	name: 'setPointZ',
	decimalPrecision: 6,
	width: fieldWidth
});

var zPositionFld = new Ext.form.NumberField({
	id: 'xyzPositionZ',
	name: 'xyzPositionZ',
	width: fieldWidth,
	decimalPrecision: 6,
	readOnly: true,
	cls: 'ss-form-field-readonly'
});

var moveXYZBtn = new Ext.Button({
	id: 'MOVE_XYZ_BTN',
	text: 'Move',
	colspan: 4
});

moveXYZBtn.on('click', function() {
	moveXYZStage();
});

var xyzStageForm = new Ext.FormPanel({
	border: false,
	layout: 'table',
	defaults: {
		bodyStyle: 'padding: 2px'
	},
	layoutConfig: {
		columns: 4
	},
	items: [
		{html: 'X: ',border: false},xSetPointFld,{border: false},xPositionFld,
		{border: false, colspan: 4},
		{html: 'Y: ',border: false},ySetPointFld,{border: false},yPositionFld,
		{border: false, colspan: 4},
		{html: 'Z: ',border: false},zSetPointFld,{border: false},zPositionFld
	],
	buttons: [ moveXYZBtn ],
	buttonAlign: 'left'
});

var xyzStagePanel = new Ext.Panel({
	title: 'XYZ Position (mm)',
	collapsible: true,
	items: [ xyzStageForm ]
});

var xyzStageLoad = function() {
	if(beamlineHeartbeatStore.getCount() > 0) {
		var record = beamlineHeartbeatStore.getAt(0);
		xyzStageForm.getForm().loadRecord(record);
	}
};

var xyzStageTask = {
	run:xyzStageLoad,
	interval:1000
};

function moveXYZStage() {
var x = Ext.getCmp('xyzSetPointX').getValue();
var y = Ext.getCmp('xyzSetPointY').getValue();
var z = Ext.getCmp('xyzSetPointZ').getValue();
Ext.Ajax.request({
			url : beamlineBaseURL + '/updateSampleStageXYZ.xml',
			method : 'POST',
			async : false,
			disableCaching : false,
			params : {
				setPointX : x,
				setPointY : y,
				setPointZ : z
			}
		});
}

*/

/************** Sample Image Scale Panel ****************/
var sampleImageScaleFld = new Ext.form.NumberField({
	name: 'scaleSP',
	hideLabel: true,
	decimalPrecision: 6,
	width: fieldWidth
});

var sampleImageScaleFbkFld = new Ext.form.NumberField({
	name: 'scale',
	hideLabel: true,
	decimalPrecision: 6,
	width: fieldWidth,
	readOnly: true,
	cls: 'ss-form-field-readonly'
});

var setSampleImageScaleBtn = new Ext.Button({
	text: 'Set'
});

setSampleImageScaleBtn.on('click', function() {
	sampleImageScaleForm.getForm().submit({
		url: 'sample/camera/scale.json',
		failure: function(form, action) {
			var json = Ext.decode(action.response.responseText, true);
			if(json && json.message) {
				Ext.Msg.alert("Error", json.message);
			} else {
				Ext.Msg.alert("Error", 'An unspecified error has occurred.');
			}
		}
	});
}); 

var sampleImageScaleForm = new Ext.FormPanel({
	border: false,
	labelPad: 2,
	labelWidth: 35,
	layout: 'table',
	layoutConfig: {
		columns: 2
	},
	defaults: {
		layout: 'form',
		border: false,
		style: { 'padding': '2px' }
	},
	items: [{
	    items: [ sampleImageScaleFld ]
	},{
	    items: [ sampleImageScaleFbkFld ] 
	}],
	buttons: [
	    setSampleImageScaleBtn
	],
	buttonAlign: 'left'
});

var sampleImageScalePanel = new Ext.Panel({
	title: 'Sample Image Scale',
	collapsible: true,
	items: [ sampleImageScaleForm ]
});

var sampleImageScaleLoad = function() {
	if(heartbeatData.sampleCamera) {
		sampleImageScaleForm.getForm().setValues(heartbeatData.sampleCamera);
		if(heartbeatData.sampleCamera.scale) {
			var scale = heartbeatData.sampleCamera.scale;
			if((siCurrentScale != scale) && (scale > 0.0)) {
				siCurrentScale = scale;
				sampleImageUpdate();
			}
		}
	}
};

var sampleImageScaleTask = {
	run: sampleImageScaleLoad,
	interval: heartbeatInterval * 2
};

heartbeatTasksToStart.push(sampleImageScaleTask);

/********************** Beamline Comments ********************/
var biSpotSizeFld = new Ext.form.TextField({
	name: 'spotSizeComment',
	fieldLabel:'Spot Size',
	value: 'N/A',
	width: 175,
	readOnly:true,
	cls:'ss-form-field-label'
});

var biGeneralFld = new Ext.form.TextArea({
	name: 'generalComment',
	fieldLabel:'Comment',
	value: 'N/A',
	width: 175,
	readOnly:true,
	cls:'ss-form-field-label'
});

var beamlineInfoForm = new Ext.FormPanel({
	border: false,
    labelAlign: 'left',
    labelWidth: 60,
	items: [
	     biSpotSizeFld,
	     biGeneralFld
	]
});

var beamlineInfoPanel = new Ext.Panel({
	title: 'Setup Information',
	collapsible: true,
	items: [ beamlineInfoForm ]
});

var beamlineInfoLoad = function() {
	if(heartbeatData.beamlineInformation) {
		beamlineInfoForm.getForm().setValues(heartbeatData.beamlineInformation);
	}
};

var beamlineInformationTask = {
	run:beamlineInfoLoad,
	interval: heartbeatInterval * 3
};

heartbeatTasksToStart.push(beamlineInformationTask);

/************************** Session Panel *****************************/

var bsExperimentNameFld = new Ext.form.TextField({
	name:'experimentName',
	fieldLabel:'Experiment',
	width:'auto',
	readOnly:true,
	cls:'ss-form-field-label'
});

var bsScanNameFld = new Ext.form.TextField({
	name:'scanName',
	fieldLabel:'Scan',
	width:'auto',
	readOnly:true,
	cls:'ss-form-field-label'
});

var sessionForm = new Ext.FormPanel({
	border: false,
    labelAlign: 'left',
    labelWidth: 60,
    labelPad: 5,
	items: [
	     bsExperimentNameFld,
	     bsScanNameFld
	]
});

var sessionPanel = new Ext.Panel({
	title: 'Scan Information',
	collapsible: true,
	items: [ sessionForm ]
});

var sessionLoad = function() {
	if(heartbeatData.beamlineSession) {
		sessionForm.getForm().setValues(heartbeatData.beamlineSession);
	}
};

var sessionTask = {
	run:sessionLoad,
	interval: heartbeatInterval * 2
};

heartbeatTasksToStart.push(sessionTask);

/************************** Scan Setup Panel *****************************/

var experimentStore = new Ext.data.JsonStore({
	url: 'experiments.json',
	root: 'experiments',
	fields: [ 'gid', 'name' ]
});

var experimentFld = new Ext.form.ComboBox({
	hiddenName: 'experimentGid',
	hideLabel: true,
	store: experimentStore,
	mode: 'remote',
	queryParam: '_dq',
	triggerAction: 'all',
	forceSelection: true,
	editable: false,
	displayField: 'name',
	valueField: 'gid',
	listWidth: 150,
	width: 'auto'
});

experimentFld.on('beforequery', function(queryEvent) {
	delete queryEvent.combo.lastQuery;
});

experimentFld.on('select', function(combo, record, index) {
	experimentFormPanel.getForm().submit({
		url: 'session.json',
		failure: function(form, action) {
			var json = Ext.decode(action.response.responseText, true);
			if(json && json.message) {
				Ext.Msg.alert("Error", json.message);
			} else {
				Ext.Msg.alert("Error", 'An unspecified error has occurred.');
			}
		}
	});
});

var experimentFormPanel = new Ext.FormPanel({
	border: false,
	items: [{
		xtype: 'fieldset',
		title: 'Experiment',
		items: [ experimentFld ]
	}]
});

var startPositionHFld = new Ext.form.NumberField({
	name: 'startPositionH',
	fieldLabel: 'X',
	decimalPrecision: 6,
	width: fieldWidth
});

var startPositionHFbkFld = new Ext.form.NumberField({
	name: 'startPositionX',
	hideLabel: true,
	decimalPrecision: 6,
	width: fieldWidth,
	readOnly: true,
	cls: 'ss-form-field-readonly'
});

var startPositionVFld = new Ext.form.NumberField({
	name: 'startPositionV',
	fieldLabel: 'V',
	decimalPrecision: 6,
	width: fieldWidth
});

var startPositionVFbkFld = new Ext.form.NumberField({
	name: 'startPositionY',
	hideLabel: true,
	decimalPrecision: 6,
	width: fieldWidth,
	readOnly: true,
	cls: 'ss-form-field-readonly'
});

var endPositionHFld = new Ext.form.NumberField({
	name: 'endPositionH',
	fieldLabel: 'X',
	decimalPrecision: 6,
	width: fieldWidth
});

var endPositionHFbkFld = new Ext.form.NumberField({
	name: 'endPositionX',
	hideLabel: true,
	decimalPrecision: 6,
	width: fieldWidth,
	readOnly: true,
	cls: 'ss-form-field-readonly'
});

var endPositionVFld = new Ext.form.NumberField({
	name: 'endPositionV',
	fieldLabel: 'V',
	decimalPrecision: 6,
	width: fieldWidth
});

var endPositionVFbkFld = new Ext.form.NumberField({
	name: 'endPositionY',
	hideLabel: true,
	decimalPrecision: 6,
	width: fieldWidth,
	readOnly: true,
	cls: 'ss-form-field-readonly'
});

var nPointsHFld = new Ext.form.NumberField({
	name: 'nPointsH',
	fieldLabel: 'X',
	decimalPrecision: 0,
	minValue: 1,
	width: fieldWidth,
	readOnly: true,
	cls: 'ss-form-field-readonly'
});

var nPointsHFbkFld = new Ext.form.NumberField({
	name: 'nPointsX',
	hideLabel: true,
	decimalPrecision: 0,
	width: fieldWidth,
	readOnly: true,
	cls: 'ss-form-field-readonly'
});

var nPointsVFld = new Ext.form.NumberField({
	name: 'nPointsV',
	fieldLabel: 'V',
	decimalPrecision: 0,
	minValue: 1,
	width: fieldWidth,
	readOnly: true,
	cls: 'ss-form-field-readonly'
});

var nPointsVFbkFld = new Ext.form.NumberField({
	name: 'nPointsY',
	hideLabel: true,
	decimalPrecision: 0,
	width: fieldWidth,
	readOnly: true,
	cls: 'ss-form-field-readonly'
});

var stepSizeFld = new Ext.form.NumberField({
	name: 'stepSizeH',
	hideLabel: true,
	decimalPrecision: 6,
	minValue: Number.MIN_VALUE,
	width: fieldWidth
});

var stepSizeFbkFld = new Ext.form.NumberField({
	name: 'stepSizeX',
	hideLabel: true,
	decimalPrecision: 6,
	width: fieldWidth,
	readOnly: true,
	cls: 'ss-form-field-readonly'
});

var scanSetupFormSubmit = function() {
	scanSetupFormPanel.getForm().submit({
		url: 'scan/device/setup.json',
		failure: function(form, action) {
			siUpdateScanPending = false;
			var json = Ext.decode(action.response.responseText, true);
			if(json && json.message) {
				Ext.Msg.alert("Error", json.message);
			} else {
				Ext.Msg.alert("Error", 'An unspecified error has occurred.');
			}
		}
	});
};

var scanSetupBtn = new Ext.Button({
	text: 'Set',
	handler: scanSetupFormSubmit
});

var scanSetupFormPanel = new Ext.FormPanel({
	border: false,
	items: [{
	        xtype: 'fieldset',
	        title: 'Step Size (mm)',
	        layout: 'table',
	        layoutConfig: { 
	    	    columns: 2 
	    	},
	    	defaults: {
	    		layout: 'form',
	    		border: false,
	    		bodyStyle: 'padding: 2px'
	    	},
	        items: [{
	        	items: [ stepSizeFld ]
	        }, {
	        	items: [ stepSizeFbkFld ] 
	        }]
    	},{
	    	xtype: 'fieldset',
	    	title: 'Start Position (mm)',
	    	labelWidth: 10,
	    	layout: 'table',
	        layoutConfig: { 
	    	    columns: 2 
	    	},
	    	defaults: {
	    		layout: 'form',
	    		border: false,
	    		bodyStyle: 'padding: 2px'
	    	},
	    	items: [{
	    		items: [ startPositionHFld ]
	    	},{
	    		items: [ startPositionHFbkFld ]
	    	}, {
	    		items: [startPositionVFld ]
	    	}, {
	    		items: [ startPositionVFbkFld ]
	    	}]
	    },{
	    	xtype: 'fieldset',
	    	title: 'End Position (mm)',
	    	labelWidth: 10,
	    	layout: 'table',
	        layoutConfig: { 
	    	    columns: 2
	    	},
	    	defaults: {
	    		layout: 'form',
	    		border: false,
	    		bodyStyle: 'padding: 2px'
	    	},
	    	items: [{
	    		items: [ endPositionHFld ]
	    	},{
	    		items: [ endPositionHFbkFld ]
	    	},{
	    		items: [ endPositionVFld ]
	    	},{
	    		items: [ endPositionVFbkFld ]
	    	}]
	    },{
	    	xtype: 'fieldset',
	    	title: 'Number of Points',
	    	labelWidth: 10,
	    	layout: 'table',
	        layoutConfig: { 
	    	    columns: 2
	    	},
	    	defaults: {
	    		layout: 'form',
	    		border: false,
	    		bodyStyle: 'padding: 2px'
	    	},
	    	items: [{
	    		items: [ nPointsHFld ]
	    	},{
	    		items: [ nPointsHFbkFld ]
	    	},{
	    		items: [ nPointsVFld ]
	    	},{
	    		items: [ nPointsVFbkFld ]
	    	}]
	    }
	],
	buttons: [ scanSetupBtn ],
	buttonAlign: 'left'
});

var scanSetupPanel = new Ext.Panel({
	title: 'Scan Setup',
	collapsible: true,
	layout: 'table',
	defaults: {
		bodyStyle: 'padding: 2px'
	},
	layoutConfig: {
		columns: 1
	},
	items: [
	    experimentFormPanel,
	    scanSetupFormPanel
	]
});

var scanSetupLoad = function() {
	if(heartbeatData.scanDevice) {
		scanSetupFormPanel.getForm().setValues(heartbeatData.scanDevice);
	}
};

var scanSetupTask = {
	run: scanSetupLoad,
	interval: heartbeatInterval
};

heartbeatTasksToStart.push(scanSetupTask);

/************************** Scan Control Panel *****************************/

var startScanBtn = new Ext.Button({
	text: 'Start'
});
startScanBtn.on('click', function() {
	startScanWindow.show();
	startScanFormScanNameFld.focus();
});

var stopScanBtn = new Ext.Button({
	text: 'Stop'
});
stopScanBtn.on('click', function() {
	Ext.Ajax.request({
		method: 'POST',
		url: 'scan/device/stop.json',
		callback: function(options, success, response) {
			var json = Ext.decode(response.responseText, true);
			if(json && !json.success) {
				if(json.message) {
					Ext.Msg.alert("Error", json.message);
				} else {
					Ext.Msg.alert("Error", 'An unspecified error has occurred.');
				}
			}
		}
	});
});

var pauseScanBtn = new Ext.Button({
	text: 'Pause'
});
pauseScanBtn.on('click', function() {
	Ext.Ajax.request({
		method: 'POST',
		url: 'scan/device/pause.json',
		callback: function(options, success, response) {
			var json = Ext.decode(response.responseText, true);
			if(json && !json.success) {
				if(json.message) {
					Ext.Msg.alert("Error", json.message);
				} else {
					Ext.Msg.alert("Error", 'An unspecified error has occurred.');
				}
			}
		}
	});
});

var scanControlPanel = new Ext.Panel({
	title: 'Scan Control',
	collapsible: true,
	items:[{
		xtype: 'panel',
		buttons: [ 
		    startScanBtn,
		    stopScanBtn,
		    pauseScanBtn
		],
		buttonAlign: 'center',
		border: false
	}]
});

var startScanFormScanNameFld = new Ext.form.TextField({
	name: 'scanName',
	hideLabel: true,
	width: 300
});

var acceptStartScanBtn = new Ext.Button({
	text: 'OK'
});
acceptStartScanBtn.on('click', function() {
	startScanForm.getForm().submit({
		url:  'scan/device/start.json',
		success: function() {
			startScanWindow.close();
		},
		failure: function(form, action) {
			var json = Ext.decode(action.response.responseText, true);
			if(json && json.message) {
				Ext.Msg.alert("Error", json.message);
			} else {
				Ext.Msg.alert("Error", 'An unspecified error has occurred.');
			}
		}
	});
});

var cancelStartScanBtn = new Ext.Button({
	text: 'Cancel'
});
cancelStartScanBtn.on('click', function() {
	startScanWindow.close();
});

var startScanForm = new Ext.form.FormPanel({
	border: false,
	items: [
	   startScanFormScanNameFld
	],
	buttons: [
	   acceptStartScanBtn,
	   cancelStartScanBtn
	],
	buttonAlign: 'left'
});

var startScanWindow = new Ext.Window({
	title: 'Enter Scan Name',
	items: [ startScanForm ],
	hidden: true
});
startScanWindow.on('beforeclose', function(window) {
	window.hide();
	return false;
});


/************************** Scan Status Panel *****************************/
var scanStateFld = new Ext.form.TextField({
	name: 'scanState',
	fieldLabel: 'Status',
	readOnly: true,
	cls: 'ss-form-field-label',
	width: 'auto'
});

var scanProgressFld = new Ext.form.TextField({
	name: 'progress',
	fieldLabel: 'Progress',
	readOnly: true,
	cls: 'ss-form-field-label',
	width: 'auto'
}); 

var scanStatusFormPanel = new Ext.form.FormPanel({
	items: [ 
	    scanStateFld,
	    scanProgressFld
	],
	border: false
}); 

var scanStatusLoad = function() {
	if(heartbeatData && heartbeatData.scanDevice) {
		var sdData = heartbeatData.scanDevice;
		
		var count = 0;
		if(sdData.scanCount) {
			count = sdData.scanCount;
		}
		
		var countTotal = 0;
		if(sdData.scanCountTotal) {
			countTotal = sdData.scanCountTotal;
		}
		
		sdData.progress = '0%';
		if(countTotal > 0) {
			sdData.progress = Math.round(100.0 * (count / countTotal)) + '%';
		}

		scanStatusFormPanel.getForm().setValues(sdData);
		
		var state = '';
		if(sdData.scanState) {
			state = sdData.scanState;
		}
		
		if(state == 'STOPPED' || state == 'COMPLETE') {
		    scanSetupBtn.enable();
		    startScanBtn.enable();
		    stopScanBtn.disable();
		    pauseScanBtn.disable();
		    pauseScanBtn.setText('Pause');
		    acquiringScanData = false;
		}
		else if(state == 'STARTING' || state == 'ACQUIRING') {
			scanSetupBtn.disable();
			startScanBtn.disable();
			stopScanBtn.enable();
			pauseScanBtn.enable();
			pauseScanBtn.setText('Pause');
			acquiringScanData = true;
		}
		else if(state == 'STOPPING') {
			scanSetupBtn.disable();
			startScanBtn.disable();
			stopScanBtn.disable();
			pauseScanBtn.disable();
			pauseScanBtn.setText('Pause');
			acquiringScanData = true;
		}
		else if(state == 'PAUSED') {
			scanSetupBtn.disable();
			startScanBtn.disable();
			stopScanBtn.disable();
			pauseScanBtn.enable();
			pauseScanBtn.setText('Resume');
			acquiringScanData = true;
		}
	}	
};

var scanStatusTask = {
	run: scanStatusLoad,
	interval:heartbeatInterval
};

heartbeatTasksToStart.push(scanStatusTask);

var scanStatusPanel = new Ext.Panel({
	title: 'Scan Status',
	collapsible: true,
	items: [ scanStatusFormPanel ]
});

/************************** Experiment Panel **************************/
var expWestPanel = new Ext.Panel({
	region: 'west',
	//title: 'Controls',
	split: true,
	collapsible: true,
	autoScroll: true,
	width: (2 * fieldWidth) + 70,
	defaults: {
		bodyStyle: 'padding: 2px'
	},
	items: [
	    beamlineInfoPanel,
	    sessionPanel,
	    scanStatusPanel,
	    hvStagePanel,
		sampleImageScalePanel
	]
});

var expImagePanel = new Ext.Panel({
	region: 'center',
	title: 'Sample Image',
	items: [{
		xtype: 'box',
		listeners: {
			"render": {
				fn: function(cmp) {
					initSampleImageCanvas(640, 480, cmp.getEl());
				},
				single: true
			}
		}
	}],
	autoScroll: true
});

var expEastPanel = new Ext.Panel({
	region: 'east',
	//title: 'Experiment Parameters',
	split: true,
	collapsible: true,
	autoScroll: true,
	width: (2 * fieldWidth) + 100,
	defaults: {
		bodyStyle: 'padding: 2px'
	},
	items: [
		scanSetupPanel,
		scanControlPanel
	]
});

var experimentPanel = new Ext.Panel({
	title: 'Experiment Setup',
	closable: false,	
	layout: 'border',
	items:[
	    expEastPanel,
	    expImagePanel,
	    expWestPanel
	],
	autoScroll: true
});
