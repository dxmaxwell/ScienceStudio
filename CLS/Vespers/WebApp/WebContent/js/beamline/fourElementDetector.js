/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *     fourElementDetector.js
 *
 */


fieldWidth = 100;
fieldWidthFED = 70;

var livePlotFED = null;

/************************** Four Element Control Panel **************************/
var fedStateLbl = new Ext.form.TextField({
	fieldLabel: 'State',
	name: 'acquireStateAll',
	width: fieldWidth,
	readOnly: true,
	cls: 'ss-form-field-label',
	value: 'N/A'
});

var fedStartBtn = new Ext.Button({
	text: 'Start',
	handler: function(/*Button*/ button, /*EventObject*/ event) {
		Ext.Ajax.request({
			method : 'POST',
			url : 'detector/fed/start.json',
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

var fedStopBtn = new Ext.Button({
	text: 'Stop',
	handler: function(/*Button*/ button, /*EventObject*/ event) {
		Ext.Ajax.request({
			method : 'POST',
			url : 'detector/fed/stop.json',
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

var fedAcqForm = new Ext.FormPanel({
	border: false,
    labelAlign: 'left',
    labelWidth: 35,
    labelPad: 5,
    items: [
         fedStateLbl
    ],
    buttons: [
        fedStartBtn,
        fedStopBtn
    ],
    buttonAlign: 'left'
});

var fedAcqPanel = new Ext.Panel({
	title: 'Detector Control',
	collapsible: true,
	items: [ fedAcqForm ]
});

var fedAcqLoad = function() {
	if(heartbeatData && heartbeatData.fourElementDetector) {
		fedAcqForm.getForm().setValues(heartbeatData.fourElementDetector);
	} 
};

var fedAcqTask = {
	run: fedAcqLoad,
	interval: heartbeatInterval * 2
};
xrfTasksToStart.push(fedAcqTask);

/************************** Four Element Setup Panel **************************/

var fedPresetTimeAllFld = new Ext.form.NumberField({
	name: 'presetTimeAll',
	width: fieldWidth,
	readOnly: true,
	cls: 'ss-form-field-readonly'
});

var setFedPresetTimeAllFld = new Ext.form.NumberField({
	name: 'presetTimeAllSP',
	width: fieldWidth,
	minValue: 0
});

var fedMaxEnergyAllFld = new Ext.form.NumberField({
	name: 'maxEnergyAll',
	width: fieldWidth,
	readOnly: true,
	cls: 'ss-form-field-readonly'
});

var setFedMaxEnergyAllFld = new Ext.form.NumberField({
	name: 'maxEnergyAllSP',
	width: fieldWidth,
	minValue: 6,
	maxValue: 30
});

var setfedSetupBtn = new Ext.Button({
	text: 'Set',
	handler: function(/*Button*/ button, /*EventObject*/ event) {
		fedSetupForm.getForm().submit({
			url: 'detector/fed/setup.json',
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

var fedSetupForm = new Ext.FormPanel({
	border: false,
	layout: 'table',
	defaults: { bodyStyle: 'padding: 2px' },
	layoutConfig: { columns: 3 },
	items: [
		{html: "Preset (sec): ", border: false, colspan: 3},
		setFedPresetTimeAllFld, {border: false}, fedPresetTimeAllFld,
		{border: false, colspan: 3},
		{html: "Max Energy (KeV): ", border: false, colspan: 3},
		setFedMaxEnergyAllFld,{border: false}, fedMaxEnergyAllFld
	],
	buttons: [ setfedSetupBtn ],
	buttonAlign: 'left'
});

var fedSetupPanel = new Ext.Panel({
	title: 'Detector Setup',
	collapsible: true,
	items: [ fedSetupForm ]
});

var fedSetupLoad = function() {
	if(heartbeatData.fourElementDetector) {
		fedSetupForm.getForm().setValues(heartbeatData.fourElementDetector);
	} 
};

var fedSetupTask = {
	run:fedSetupLoad,
	interval: heartbeatInterval * 2
};

xrfTasksToStart.push(fedSetupTask);

/************************** Four Element Status Panel **************************/
var fedElapsedTimeLbl = new Ext.form.NumberField({
	name: 'elapsedTimeAll',
	fieldLabel: 'Elapsed Time (s)',
	width: fieldWidth,
	readOnly: true,
	cls: 'ss-form-field-label',
	value: 'N/A'
});

var fedDeadTimeLbl = new Ext.form.NumberField({
	name: 'deadTimeAll',
	fieldLabel: 'Dead Time (%)',
	width: fieldWidth,
	readOnly: true,
	cls: 'ss-form-field-label',
	value: 'N/A'
});

var fedSlowPeaks1Lbl = new Ext.form.NumberField({
	name: 'slowPeaks1',
	width:  fieldWidthFED,
	readOnly: true,
	cls: 'ss-form-field-label',
	value: 'N/A'
});

var fedSlowPeaks2Lbl = new Ext.form.NumberField({
	name: 'slowPeaks2',
	width:  fieldWidthFED,
	readOnly: true,
	cls: 'ss-form-field-label',
	value: 'N/A'
});

var fedSlowPeaks3Lbl = new Ext.form.NumberField({
	name: 'slowPeaks3',
	width:  fieldWidthFED,
	readOnly: true,
	cls: 'ss-form-field-label',
	value: 'N/A'
});

var fedSlowPeaks4Lbl = new Ext.form.NumberField({
	name: 'slowPeaks4',
	width:  fieldWidthFED,
	readOnly: true,
	cls: 'ss-form-field-label',
	value: 'N/A'
});

var inputCountRate1Lbl = new Ext.form.NumberField({
	name: 'inputCountRate1',
	width: fieldWidthFED,
	readOnly: true,
	cls: 'ss-form-field-label',
	value: 'N/A'
});

var inputCountRate2Lbl = new Ext.form.NumberField({
	name: 'inputCountRate2',
	width: fieldWidthFED,
	readOnly: true,
	cls: 'ss-form-field-label',
	value: 'N/A'
});

var inputCountRate3Lbl = new Ext.form.NumberField({
	name: 'inputCountRate3',
	width: fieldWidthFED,
	readOnly: true,
	cls: 'ss-form-field-label',
	value: 'N/A'
});

var inputCountRate4Lbl = new Ext.form.NumberField({
	name: 'inputCountRate4',
	width: fieldWidthFED,
	readOnly: true,
	cls: 'ss-form-field-label',
	value: 'N/A'
});

var outputCountRate1Lbl = new Ext.form.NumberField({
	name: 'outputCountRate1',
	width: fieldWidthFED,
	readOnly: true,
	cls: 'ss-form-field-label',
	value: 'N/A'
});

var outputCountRate2Lbl = new Ext.form.NumberField({
	name: 'outputCountRate2',
	width: fieldWidthFED,
	readOnly: true,
	cls: 'ss-form-field-label',
	value: 'N/A'
});

var outputCountRate3Lbl = new Ext.form.NumberField({
	name: 'outputCountRate3',
	width: fieldWidthFED,
	readOnly: true,
	cls: 'ss-form-field-label',
	value: 'N/A'
});

var outputCountRate4Lbl = new Ext.form.NumberField({
	name: 'outputCountRate4',
	width: fieldWidthFED,
	readOnly: true,
	cls: 'ss-form-field-label',
	value: 'N/A'
});

var fedStatusPanel = new Ext.FormPanel({
	title: 'Detector Status',
	collapsible: true,
    labelAlign: 'left',
    labelWidth: 100,
    labelPad: 5,
    defaults: {
		border: false
	},
	items: [
	    fedElapsedTimeLbl,
	    fedDeadTimeLbl,
	    {
	    	html: 'Incident Beam Count Rate (Io):'
	    }, {
	    	layout: 'table',
	    	layoutConfig: { columns: 4 },
	    	items: [ inputCountRate1Lbl, inputCountRate2Lbl, inputCountRate3Lbl, inputCountRate4Lbl ]
	    }, { 
	    	html: 'XRF Total Count Rate:'
		}, { 
	    	layout: 'table',
	    	layoutConfig: { columns: 4 },
	    	items: [ outputCountRate1Lbl,  outputCountRate2Lbl,  outputCountRate3Lbl,  outputCountRate4Lbl ]
	    }, {
	    	html: 'Total XRF Counts:'
	    }, {
	    	layout: 'table',
	    	layoutConfig: { columns: 4 },
	    	items: [ fedSlowPeaks1Lbl,  fedSlowPeaks2Lbl,  fedSlowPeaks3Lbl,  fedSlowPeaks4Lbl ]
	    }
	]
});

var fedStatusLoad = function() {
	if(heartbeatData && heartbeatData.fourElementDetector) {
		fedStatusPanel.getForm().setValues(heartbeatData.fourElementDetector);
	}
};

var fedStatusTask = {
	run: fedStatusLoad,
	interval: heartbeatInterval * 2
};
xrfTasksToStart.push(fedStatusTask);

/*********************** Four Element Plot Style Panel **********************/

var fedScaleStore = new Ext.data.ArrayStore({
	fields: ['value', 'display'],
	data: [
	    ['LINEAR', 'linear'],
		['LOG10', 'logarithmic']
	]
});

var fedScaleCmbx = new Ext.form.ComboBox({
	fieldLabel: 'Scale',
	name: 'fedScale',
	store: fedScaleStore,
	value: 'LINEAR',
	mode: 'local',
	editable : false,
	forceSelection: true,
	triggerAction: 'all',
	displayField: 'display',
	valueField: 'value',
	width: 100
});

fedScaleCmbx.on('select', function(combo, record, index) {
	if(livePlotFED) {
		var scale = record.get('value');
		livePlotFED.updateOptions({
			logscale: (scale == 'LOG10')
		});
	}
});

var fedPlotStylePanel = new Ext.FormPanel({
	title: 'Plot Style',
	collapsible: true,
	items: [ fedScaleCmbx ]
});

/************************** Four Element Save MCA Data Panel **************************/
var savefedXmlBtn = new Ext.Button({
	text: 'Save CDFML',
	handler: function(/*Button*/ button, /*EventObject*/ event) {
				window.location.href = 'detector/fed/save/spectrum.cdfml';
			}
});

var savefedImageBtn = new Ext.Button({
	text: 'Save PNG',
	handler: function(/*Button*/ button, /*EventObject*/ event) {
				window.location.href = 'detector/fed/save/spectrum.png';
			}
});

var savefedTxtBtn = new Ext.Button({
	text: 'Save TXT',
	handler: function(/*Button*/ button, /*EventObject*/ event) {
				window.location.href = 'detector/fed/save/spectrum.txt';
			}
});

var fedSaveDataPanel = new Ext.Panel({
	title: 'Save Data',
	collapsible: true,
	buttons: [
		savefedTxtBtn,
		savefedImageBtn,
		savefedXmlBtn
	],
	buttonAlign:'left'
});

var fedDataPanel = new Ext.Panel({
	region: 'center',
	title: 'Spectrum',
	html: "<div id='SPECTRUM_FED_DIV' style='width: 640;height: 480'></div>",
	autoScroll: true
});

var fedSpectrumRefresh = function() {
	Ext.Ajax.request({
		url : 'detector/fed/spectrum.json',
		disableCaching : false,
		success : function(response, options) {
			var livePlotData = [];
			var labelX = 'Energy';
			var labelY = 'Unknown';
			
			var json = response.responseJson||Ext.decode(response.responseText, true);
			if(json) {
			
				var scale = fedScaleCmbx.getValue();
				
				var maxEnergy = 0.0;
				if(json.maxEnergy) {
					maxEnergy = json.maxEnergy;
				}
				
				var spectrumAll = [];
				if(json.spectrumAll) {
					spectrumAll = json.spectrumAll;
				}
				
				var size = spectrumAll.length;
				if(size > 0) {
					var x0 = 1.0;
					var bin = 1.0;
					if(maxEnergy > 0.0) {
						bin = maxEnergy / size;
						x0 = bin / 2.0;
					}
					
					for (var i = 0; i < size; i++) {
						if(spectrumAll[i] < 1.0) {
							// Values less than one do not graph with logarithmic scale. // 
							livePlotData.push([x0 + (i * bin), 1.0 ]);
						} else {
							livePlotData.push([x0 + (i * bin), spectrumAll[i]]);
						}
					}
				}
			}
			
			// Dygraph will throw exception if the data set is empty.
			if(livePlotData.length == 0) {
				livePlotData.push([ 0, 0 ]);
			}
			
			if(livePlotFED == null) {
				var elm = Ext.getDom('SPECTRUM_FED_DIV');
				if(elm) {
					livePlotFED = new Dygraph(elm, livePlotData, {
						drawPoints: true,
						labels: [labelX, labelY],
						logscale: (scale == 'LOG10')
					});
				}
			} else {
				livePlotFED.updateOptions({
					'file': livePlotData,
					'labels': [labelX, labelY]
				});
			}
		}
	});
};

var fedSpectrumRefreshTask = {
	run: fedSpectrumRefresh,
	interval: heartbeatInterval * 3
};
xrfTasksToStart.push(fedSpectrumRefreshTask);

var fedControlsPanel = new Ext.Panel({
	region: 'west',
	split: true,
	collapsible: true,
	autoScroll: true,
	title: 'Four Element MCA',
    items: [
    	fedAcqPanel,
    	fedSetupPanel,
    	fedStatusPanel,
    	fedPlotStylePanel,
    	fedSaveDataPanel
    ]
});

var fourElementDetectorPanel = new Ext.Panel({
	id: 'FED_PANEL',
	title: 'FED Setup',
	closable: false,
	layout : 'border',
	items:[ 
	    fedDataPanel,
	    fedControlsPanel
	],
	autoScroll: true, 
	disabled: true
});
