/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *     beamline.js
 *     
 */



  /**
 *
 * @include "include.js"
 */

var labelWidth = 80;
var fieldWidth = 175;
var txtFldWidth = 175;

/*********************** Storage Ring Status Panel ***********************/
var srEnergyFld = new Ext.form.TextField({
	fieldLabel: 'Energy',
	name: 'srEnergy',
	width: fieldWidth,
	readOnly: true,
	cls: 'ss-form-field-label',
	value: 'N/A'
});

var srInjectingFld = new Ext.form.TextField({
	fieldLabel: 'Injecting',
	name: 'srInjecting',
	width: fieldWidth,
	readOnly: true,
	cls: 'ss-form-field-label',
	value: 'N/A'
});

var srShuttersFld = new Ext.form.TextField({
	fieldLabel: 'Shutters',
	name: 'srShutters',
	width: fieldWidth,
	readOnly: true,
	cls: 'ss-form-field-label',
	value: 'N/A'
});

var srMessageFld = new Ext.form.TextArea({
	fieldLabel: 'Message',
	name: 'srStatusMsg',
	width: fieldWidth,
	readOnly: true,
	cls: 'ss-form-field-label',
	value: 'N/A'
});

var srHalfLifeFld = new Ext.form.TextField({
	fieldLabel: 'HalfLife',
	name: 'srHalfLife',
	width: fieldWidth,
	readOnly: true,
	cls: 'ss-form-field-label',
	value: 'N/A'
});

var srCurrentFld = new Ext.form.TextField({
	fieldLabel: 'Current',
	name: 'srCurrent',
	width: fieldWidth,
	readOnly: true,
	cls: 'ss-form-field-label',
	value: 'N/A'
});

var srStatusForm = new Ext.FormPanel({
	border: false,
    labelAlign: 'left',
    labelWidth: labelWidth,
    layoutConfig: { labelSeparator: ':' },
	items: [
	    srEnergyFld,
	    srCurrentFld,
	    srHalfLifeFld,
	    srMessageFld,
	    srShuttersFld,
	    srInjectingFld
	]
});

var srStatusPanel = new Ext.Panel({
	title: 'Storage Ring',
	collapsible: false,
	items: [ srStatusForm ]
});

var srStatusLoad = function() {
	if(heartbeatData && heartbeatData.storageRingStatus) {
		srStatusForm.getForm().setValues(heartbeatData.storageRingStatus);
	}
};

var srStatusTask = {
	run:srStatusLoad,
	interval: heartbeatInterval * 2
};

heartbeatTasksToStart.push(srStatusTask);

/************************** Shutter Status Panel **************************/
var shPSH1StateFld = new Ext.form.TextField({
	fieldLabel: 'Photon 1',
	name: 'psh1State',
	width: fieldWidth,
	readOnly: true,
	cls: 'ss-form-field-label',
	value: 'N/A'
});

var shPSH2StateFld = new Ext.form.TextField({
	fieldLabel: 'Photon 2',
	name: 'psh2State',
	width: fieldWidth,
	readOnly: true,
	cls: 'ss-form-field-label',
	value: 'N/A'
});

var shSSH1StateFld = new Ext.form.TextField({
	fieldLabel: 'Safety 1',
	name: 'ssh1State',
	width: fieldWidth,
	readOnly: true,
	cls: 'ss-form-field-label',
	value: 'N/A'
});

var shSSH2StateFld = new Ext.form.TextField({
	fieldLabel: 'Safety 2',
	name: 'ssh2State',
	width: fieldWidth,
	readOnly: true,
	cls: 'ss-form-field-label',
	value: 'N/A'
});

var shStatusForm = new Ext.FormPanel({
	border: false,
    labelAlign: 'left',
    labelWidth: labelWidth,
    layoutConfig: { labelSeparator: ':' },
	items: [
	    shPSH1StateFld,
	    shSSH1StateFld,
	    shPSH2StateFld,
	    shSSH2StateFld
	]
});

var shStatusPanel = new Ext.Panel({
	title: 'Shutters',
	collapsed: true,
	collapsible: true,
	items: [ shStatusForm ]
});


var shStatusLoad = function() {
	if(heartbeatData.shutterStatus) {
		shStatusForm.getForm().setValues(heartbeatData.shutterStatus);
	}
};

var shStatusTask = {
	run: shStatusLoad,
	interval: heartbeatInterval * 2
};

heartbeatTasksToStart.push(shStatusTask);

/************************** Mono Status Panel **************************/
var mnEnergyFbkFld = new Ext.form.TextField({
	name: 'monoEnergyFbk',
	fieldLabel: 'Energy (eV)',
	width: fieldWidth,
	readOnly: true,
	cls: 'ss-form-field-label',
	value: 'N/A'
});

var mnStripeFbkFld = new Ext.form.TextField({
	name: 'monoStripeFbk',
	fieldLabel: 'Stripe',
	width: fieldWidth,
	readOnly: true,
	cls: 'ss-form-field-label',
	value: 'N/A'
});

var mnStatusForm = new Ext.FormPanel({
	border: false,
    labelAlign: 'left',
    labelWidth: labelWidth,
    layoutConfig: { labelSeparator: ':' },
	items: [
	   mnEnergyFbkFld,
	   mnStripeFbkFld
	]
});

var mnStatusPanel = new Ext.Panel({
	title: 'Monochrometer',
	collapsed: true,
	collapsible: true,
	items: [ mnStatusForm ]
});

var mnStatusLoad = function() {
	if(heartbeatData && heartbeatData.monoStatus) {
		if(heartbeatData.monoStatus.monoEnergyFbk) {
			heartbeatData.monoStatus.monoEnergyFbk =
				convertToFixed0(heartbeatData.monoStatus.monoEnergyFbk);
		}
		mnStatusForm.getForm().setValues(heartbeatData.monoStatus);
	}
};

var mnStatusTask = {
	run: mnStatusLoad,
	interval: heartbeatInterval * 2
};

heartbeatTasksToStart.push(mnStatusTask);

/************************** Mirror Status Panel **************************/
var mr1AStripeFbkFld = new Ext.form.TextField({
	name: 'm1AStripe',
	fieldLabel: 'M1A Stripe',
	width: fieldWidth,
	readOnly: true,
	cls: 'ss-form-field-label',
	value: 'N/A'
});

var mr1BStripeFbkFld = new Ext.form.TextField({
	name: 'm1BStripe',
	fieldLabel: 'M1B Stripe',
	width: fieldWidth,
	readOnly: true,
	cls: 'ss-form-field-label',
	value: 'N/A'
});

var mr2AStripeFbkFld = new Ext.form.TextField({
	name: 'm2AStripe',
	fieldLabel: 'M2A Stripe',
	width: fieldWidth,
	readOnly: true,
	cls: 'ss-form-field-label',
	value: 'N/A'
});

var mr2BStripeFbkFld = new Ext.form.TextField({
	name: 'm2BStripe',
	fieldLabel: 'M2B Stripe',
	width: fieldWidth,
	readOnly: true,
	cls: 'ss-form-field-label',
	value: 'N/A'
});

var mrStatusForm = new Ext.FormPanel({
	border: false,
    labelAlign: 'left',
    labelWidth: labelWidth,
    layoutConfig: { labelSeparator: ':' },
	items: [
	   mr1AStripeFbkFld,
	   mr1BStripeFbkFld,
	   mr2AStripeFbkFld,
	   mr2BStripeFbkFld
	]
});

var mrStatusPanel = new Ext.Panel({
	title: 'Mirrors',
	collapsed: true,
	collapsible: true,
	items: [ mrStatusForm ]
});

var mrStatusLoad = function() {
	if(heartbeatData && heartbeatData.mirrorStatus) {
		mrStatusForm.getForm().setValues(heartbeatData.mirrorStatus);
	}
};

var mrStatusTask = {
	run: mrStatusLoad,
	interval: heartbeatInterval * 2
};

heartbeatTasksToStart.push(mrStatusTask);

/************************** Beamline Session Panel **************************/
var bsProjectNameFld = new Ext.form.TextField({
	name: 'projectName',
	fieldLabel: 'Project',
	width: fieldWidth,
	readOnly: true,
	cls: 'ss-form-field-label',
	value: 'N/A'
});

var bsSessionNameFld = new Ext.form.TextField({
	name: 'sessionName',
	fieldLabel: 'Session',
	width: fieldWidth,
	readOnly: true,
	cls: 'ss-form-field-label',
	value: 'N/A'
});

var bsProposalIdFld = new Ext.form.TextField({
	name: 'proposal',
	fieldLabel: 'Propsoal',
	width: fieldWidth,
	readOnly: true,
	cls: 'ss-form-field-label',
	value: 'N/A'
});

var bsSessionTimeoutFld = new Ext.form.TextField({
	name: 'sessionTimeoutHHMM',
	fieldLabel: 'Timeout',
	width: fieldWidth,
	readOnly: true,
	cls: 'ss-form-field-label',
	value: 'N/A'
});

var bsPersonNameFld = new Ext.form.TextField({
	name: 'controllerName',
	fieldLabel: 'Controller',
	width: fieldWidth,
	readonly: true,
	cls: 'ss-form-field-label',
	value: 'N/A'
});

var bsControlBtn = new Ext.Button({
	text: 'Control Session',
	handler: function() {
		Ext.Ajax.request({
			url: 'session/control.json',
			callback: function(options, success, response) {
				var json = Ext.decode(response.responseText, true);
				if(json && !json.success) {
					if(json.globalErrors && json.globalErrors[0]) {
						Ext.Msg.alert("Error", json.globalErrors[0]);
					} else {	
						Ext.Msg.alert("Error", 'An unspecified error has occurred.');
					}
				}
			}
		});
	}
});

var blSessionForm = new Ext.FormPanel({
	border: false,
    labelAlign: 'left',
    labelWidth: labelWidth,
	items: [
	     bsProjectNameFld,
	     bsSessionNameFld,
	     bsProposalIdFld,
	     bsSessionTimeoutFld,
	     bsPersonNameFld
	],
	buttons:[
	     bsControlBtn
	],
	buttonAlign: 'left'
});

var blSessionPanel = new Ext.Panel({
	title: 'Session',
	collapsible: false,
	items: [ blSessionForm ]
});

var blSessionLoad = function() {
	if(heartbeatData.beamlineSession) {
		var bsData = heartbeatData.beamlineSession;
		
		var sessionRunning;
		if(bsData.sessionTimeout > 0) {
			bsData.sessionTimeoutHHMM = convertSecondsToHHMM(bsData.sessionTimeout);
			sessionRunning = true;
		} else {
			bsData.sessionTimeoutHHMM = '0:00';
			sessionRunning = false;
		}
		
		blSessionForm.getForm().setValues(bsData);
		
		if(sessionRunning) {
			if(bsData.controllerUid == personUid) {
				showSessionController();
			}
			else {
				showSessionObserver();
			}
		}
		else {
			showSessionStopped();
		}
	}	
};

var blSessionTask = {
	run:blSessionLoad,
	interval: heartbeatInterval
};

heartbeatTasksToStart.push(blSessionTask);

/**************************** Beamline Panel Layout *************************/
var beamlineInfoPanel = new Ext.Panel({
	region: 'west',
	title: 'Beamline Information',
	split: true,
	collapsible: true,
	autoScroll: true,
	defaults: {
		bodyStyle: 'padding: 2px'
	},
	items: [
	    blSessionPanel,
	    srStatusPanel,
	    shStatusPanel,
	    mnStatusPanel,
	    mrStatusPanel
	],
	width: labelWidth + fieldWidth + 35
});

var beamlineImagePanel = new Ext.Panel({
	region: 'center',
	title: 'Beamline',
	items:[{
		xtype: 'box',
		autoEl:{
			tag: 'img',
			alt: 'VESPERS Endstation',
			src: '../img/vespers_endstation.png'
		}
	}],
	autoScroll: true
});

var beamlinePanel = new Ext.Panel({
	id: 'BEAMLINE_PANEL',
	title: 'Beamline Setup',
	layout: 'border',
	items:[
	      beamlineImagePanel,
	      beamlineInfoPanel
	],
	closable: false,
	autoScroll: true
});
