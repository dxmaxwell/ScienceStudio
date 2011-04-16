/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *    viewport-south.js
 */

var biSpotSizeFld = new Ext.form.TextField({
	name: 'spotSizeCommentSP',
	fieldLabel:'Spot Size',
	width: 175
});

var biSpotSizeFbkFld = new Ext.form.TextField({
	name: 'spotSizeComment',
	hideLabel: true,
	value: 'N/A',
	width: 175,
	readOnly:true,
	cls:'ss-form-field-readonly'
});

var biGeneralFld = new Ext.form.TextArea({
	name: 'generalCommentSP',
	fieldLabel:'Comment',
	width: 175
});

var biGeneralFbkFld = new Ext.form.TextArea({
	name: 'generalComment',
	hideLabel: true,
	value: 'N/A',
	width: 175,
	readOnly:true,
	cls:'ss-form-field-readonly'
});

var biSubmitBtn = new Ext.Button({
	text: 'Set'
});

biSubmitBtn.on('click', function () {
	beamlineInfoForm.getForm().submit({
		url: 'information.json',
		failure: function(form, action) {
			var json = Ext.decode(action.response.responseText, true);
			if(json && json.globalErrors && json.globalErrors[0]) {
				Ext.Msg.alert("Error", json.globalErrors[0]);
			} else {	
				Ext.Msg.alert("Error", 'An unspecified error has occurred.');
			}
		}
	});
}, this);

var beamlineInfoForm = new Ext.form.FormPanel({
	border: false,
    labelAlign: 'left',
    labelWidth: 60,
    layout: 'table',
    layoutConfig: {
    	columns: 2
    },
    defaults: {
    	layout: 'form',
    	border: false,
    	style: { 'padding':'5px' }
    },
	items: [{
		items: [ biSpotSizeFld ]
	},{
		items: [ biSpotSizeFbkFld ]
	},{
		items: [ biGeneralFld ]
	},{
	    items: [ biGeneralFbkFld ]
	}],
	buttons: [
	    biSubmitBtn
	],
	buttonAlign:'left'
});

var beamlineInfoPanel = new Ext.Panel({
	title: 'Beamline Information',
	items: [ beamlineInfoForm ],
	style: { 'margin': '20px 50px' },
	width: 500
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

var southPanel = new Ext.Panel({
	region: 'south',
	items: [ beamlineInfoPanel ],
	border: false,
	height: 200
});