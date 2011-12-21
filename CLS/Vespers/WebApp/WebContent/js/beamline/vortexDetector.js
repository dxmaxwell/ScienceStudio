/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *     vortexDetectorPanels.js
 *
 */


fieldWidth = 100;

var livePlotVtx = null;

/************************** Vortex Control Panel **************************/
var vtxStateLbl = new Ext.form.TextField({
	fieldLabel: 'State',
	name: 'acquireState',
	width: fieldWidth,
	readOnly: true,
	cls: 'ss-form-field-label',
	value: 'N/A'
});

var vtxStartBtn = new Ext.Button({
	text: 'Start',
	handler: function(button, event) {
		Ext.Ajax.request({
			method: 'POST',
			url: 'detector/vtx/start.json',
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
	}
});

var vtxStopBtn = new Ext.Button({
	text: 'Stop',
	handler: function(button, event) {
		Ext.Ajax.request({
			method: 'POST',
			url: 'detector/vtx/stop.json',
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
	}
});

var vtxAcqForm = new Ext.FormPanel({
	labelPad:5,
	labelWidth:35,
	labelAlign:'left',
    items:[
         vtxStateLbl
    ],
    buttons:[
        vtxStartBtn,
        vtxStopBtn
    ],
    buttonAlign: 'left',
    border: false
});

var vtxAcqPanel = new Ext.Panel({
	title: 'Detector Control',
	collapsible: true,
	items: [ vtxAcqForm ]
});

var vtxAcqLoad = function() {
	if(heartbeatData.vortexDetector) {
		vtxAcqForm.getForm().setValues(heartbeatData.vortexDetector);
	}
};

var vtxAcqTask = {
	run: vtxAcqLoad,
	interval: heartbeatInterval * 2
};

heartbeatTasksToStart.push(vtxAcqTask);

/************************** Vortex Setup Panel **************************/

var vtxPresetTimeFld = new Ext.form.NumberField({
	name: 'presetTime',
	width: fieldWidth,
	readOnly: true,
	cls: 'ss-form-field-readonly'
});

var setVtxPresetTimeFld = new Ext.form.NumberField({
	name: 'presetTimeSP',
	width: fieldWidth,
	minValue: 0
});

var vtxMaxEnergyFld = new Ext.form.NumberField({
	name: 'maxEnergy',
	width: fieldWidth,
	readOnly: true,
	cls: 'ss-form-field-readonly'
});

var setVtxMaxEnergyFld = new Ext.form.NumberField({
	name: 'maxEngySetPoint',
	width: fieldWidth,
	minValue: 6,
	maxValue: 30
});

var setVtxSetupBtn = new Ext.Button({
	text: 'Set',
	handler: function(button, event) {
		vtxSetupForm.getForm().submit({
			url: 'detector/vtx/setup.json',
			failure: function(form, action) {
				var json = Ext.decode(action.response.responseText, true);
				if(json && json.message) {
					Ext.Msg.alert("Error", json.message);
				} else {
					Ext.Msg.alert("Error", 'An unspecified error has occurred.');
				}
			}
		});
	}
});

var vtxSetupForm = new Ext.FormPanel({
	layout: 'table',
	defaults: { bodyStyle: 'padding: 2px' },
	layoutConfig: { columns: 3 },
	items: [
		{html: "Preset (sec): ", border: false, colspan: 3},
		setVtxPresetTimeFld, {border: false}, vtxPresetTimeFld,
		{border: false, colspan: 3},
		{html: "Max Energy (KeV): ", border: false, colspan: 3},
		setVtxMaxEnergyFld,{border: false}, vtxMaxEnergyFld
	],
	buttons: [ setVtxSetupBtn ],
	buttonAlign: 'left',
	border: false
});

var vtxSetupPanel = new Ext.Panel({
	title: 'Detector Setup',
	collapsible: true,
	items: [ vtxSetupForm ]
});

var vtxSetupLoad = function() {
	if(heartbeatData.vortexDetector) {
		vtxSetupForm.getForm().setValues(heartbeatData.vortexDetector);
	}
};

var vtxSetupTask = {
	run:vtxSetupLoad,
	interval: heartbeatInterval * 2
};

heartbeatTasksToStart.push(vtxSetupTask);

/************************** Vortex Status Panel **************************/

var vtxElapsedTimeLbl = new Ext.form.TextField({
	name: 'elapsedTime',
	fieldLabel: 'Elapsed Time (s)',
	width: fieldWidth,
	readOnly: true,
	cls: 'ss-form-field-label',
	value: 'N/A'
});

var vtxDeadTimeLbl = new Ext.form.TextField({
	name: 'deadTime',
	fieldLabel: 'Dead Time (%)',
	width: fieldWidth,
	readOnly: true,
	cls: 'ss-form-field-label',
	value: 'N/A'
});

var countsLbl = new Ext.form.TextField({
	fieldLabel: 'Total XRF Counts',
	name: 'counts',
	width: fieldWidth,
	readOnly: true,
	cls: 'ss-form-field-label',
	value: 'N/A'
});

var inputCountRateLbl = new Ext.form.TextField({
	fieldLabel: 'Incident Beam Count Rate (Io)',
	name: 'inputCountRate',
	width: fieldWidth,
	readOnly: true,
	cls: 'ss-form-field-label',
	value: 'N/A'
});

var outputCountRateLbl = new Ext.form.TextField({
	fieldLabel: 'XRF Total Count Rate',
	name: 'outputCountRate',
	width: fieldWidth,
	readOnly: true,
	cls: 'ss-form-field-label',
	value: 'N/A'
});

var vtxCountRatePanel = new Ext.FormPanel({
	title: 'Detector Status',
	collapsible: true,
    labelAlign: 'left',
    labelWidth: 125,
    labelPad: 5,
	items: [
	    vtxElapsedTimeLbl,
	    vtxDeadTimeLbl,
	    inputCountRateLbl,
	    outputCountRateLbl,
	    countsLbl
	]
});

var vtxCountLoad = function() {
	if(heartbeatData.vortexDetector) {
		vtxCountRatePanel.getForm().setValues(heartbeatData.vortexDetector);
	}
};

var vtxCountTask = {
	run: vtxCountLoad,
	interval: heartbeatInterval * 2
};

heartbeatTasksToStart.push(vtxCountTask);

/*********************** Vortex Plot Style Panel **********************/

var vtxScaleStore = new Ext.data.ArrayStore({
	fields: ['value', 'display'],
	data: [
	    ['LINEAR', 'linear'],
		['LOG10', 'logarithmic']
	]
});

var vtxScaleCmbx = new Ext.form.ComboBox({
	fieldLabel: 'Scale',
	name: 'vtxScale',
	store: vtxScaleStore,
	mode: 'local',
	triggerAction: 'all',
	forceSelection: true,
	valueField: 'value',
	displayField: 'display',
	value: 'LINEAR',
	width: 100
});

vtxScaleCmbx.on('select', function(combo, record, index) {
	if(livePlotVtx) {
		var scale = record.get('value');
		livePlotVtx.updateOptions({
			logscale: (scale == 'LOG10')
		});
	}
});

var vtxPlotStylePanel = new Ext.FormPanel({
	title: 'Plot Style',
	collapsible: true,
	items:[ vtxScaleCmbx ]
});

/************************** Save MCA Data Panel **************************/
var saveVtxXmlBtn = new Ext.Button({
	text: 'Save CDFML',
	handler: function(button, event) {
		window.location.href = 'detector/vtx/save/spectrum.cdfml';
	}
});

var saveVtxImageBtn = new Ext.Button({
	text: 'Save PNG',
	handler: function(button, event) {
		window.location.href = 'detector/vtx/save/spectrum.png';
	}
});

var saveVtxTxtBtn = new Ext.Button({
	text: 'Save TXT',
	handler: function(button, event) {
		window.location.href = 'detector/vtx/save/spectrum.txt';
	}
});

var vtxSaveDataPanel = new Ext.Panel({
	title: 'Save Data',
	collapsible: true,
	buttons:[
	    saveVtxTxtBtn,
	    saveVtxImageBtn,
	    saveVtxXmlBtn
	],
	buttonAlign:'left'
});

var vtxDataPanel = new Ext.Panel({
	region: 'center',
	title: 'Spectrum',
	// do not need the canvas object when using dygraph visualization components
	html: "<div id='SPECTRUM_VTX_DIV' style='width: 640;height: 480'></div>",
	autoScroll: true,
	width: 'auto'
});

var vtxSpectrumRefresh = function() {
	Ext.Ajax.request({
		url : 'detector/vtx/spectrum.json',
		disableCaching : false,
		success : function(response, options) {
			var livePlotData = [];
			var labelX = 'Energy';
			var labelY = 'Unknown';
			
			var json = response.responseJson||Ext.decode(response.responseText, true);
			if(json) {
			
				var scale = vtxScaleCmbx.getValue();
				
				var maxEnergy = 0.0;
				if(json.maxEnergy) {
					maxEnergy = json.maxEnergy;
				}
				
				var spectrum = [];
				if(json.spectrum) {
					spectrum = json.spectrum;
				}
				
				var size = spectrum.length;
				if(size > 0) {
					var x0 = 1.0;
					var bin = 1.0;
					if(maxEnergy > 0.0) {
						bin = maxEnergy / size;
						x0 = bin / 2.0;
					}
					
					for (var i = 0; i < size; i++) {
						if(spectrum[i] < 1.0) {
							// Values less than one do not graph with logarithmic scale. // 
							livePlotData.push([x0 + (i * bin), 1.0 ]);
						} else {
							livePlotData.push([x0 + (i * bin), spectrum[i]]);
						}
					}
				}
			}
			
			// Dygraph will throw exception if the data set is empty.
			if(livePlotData.length == 0) {
				livePlotData.push([ 0, 0 ]);
			}
			
			if(livePlotVtx == null) {
				var elm = Ext.getDom('SPECTRUM_VTX_DIV');
				if(elm) {
					livePlotVtx = new Dygraph(elm, livePlotData, {
						drawPoints: true,
						labels: [labelX, labelY],
						logscale: (scale == 'LOG10')
					});
				}
			} else {
				livePlotVtx.updateOptions({
					'file': livePlotData,
					'labels': [labelX, labelY]
				});
			}
		}
	});
};

var vtxSpectrumRefreshTask = {
	run: vtxSpectrumRefresh,
	interval: heartbeatInterval * 3
};

heartbeatTasksToStart.push(vtxSpectrumRefreshTask);

var vtxControlsPanel = new Ext.Panel({
	region: 'west',
	split: true,
	collapsible: true,
	autoScroll: true,
	title: 'Vortex MCA',
    items: [
    	vtxAcqPanel,
    	vtxSetupPanel,
    	vtxCountRatePanel,
    	vtxPlotStylePanel,
    	vtxSaveDataPanel
    ]
});

var vortexDetectorPanel = new Ext.Panel({
	id: 'VTX_PANEL',
	title:'Vortex Setup',
	closable: false,
	layout:'border',
	items:[
	    vtxDataPanel,
	    vtxControlsPanel
	],
	autoScroll: true
});
