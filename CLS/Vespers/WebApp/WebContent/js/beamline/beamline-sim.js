/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *     beamline-sim.js
 *     
 */


/**
 *
 * @include "include.js"
 */

var labelWidth = 80;
var fieldWidth = 175;
var txtFldWidth = 175;

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
			if(bsData.controllerGid == personGid) {
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
	    blSessionPanel
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
