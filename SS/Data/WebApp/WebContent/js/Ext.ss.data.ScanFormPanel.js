/**
 * Copyright (c) Canadian Light Source, Inc. All rights reserved. - see
 * license.txt for details.
 *
 * Description:
 * 		ScanFormPanel.js
 *
 */

Ext.namespace('Ext.ss.data');

Ext.ss.data.ScanFormPanel = function(config) {
	
	Ext.form.Field.prototype.msgTarget = 'side';
	
	var formPanelConfig = Ext.apply({}, config);
	
	var defaults = {};
	if(formPanelConfig.defaults) {
		defaults = formPanelConfig.defaults;
		delete formPanelConfig.defaults;
	}

	var buttonDefaults = {};
	if(formPanelConfig.buttonDefaults) {
		buttonDefaults = formPanelConfig.buttonDefaults;
		delete formPanelConfig.buttonDefaults;
	}
	
	this.ss.fields.name = new Ext.form.TextField(Ext.applyIf({
		fieldLabel: 'Name',
		name: 'name',
		width: 200
	}, defaults));
	
	this.ss.fields.dataUrl = new Ext.form.TextField(Ext.applyIf({
		fieldLabel : 'Data URL',
		name: 'dataUrl',
		width: 350,
		disabled: true
	}, defaults));
	
	this.ss.fields.parameters = new Ext.form.TextArea(Ext.applyIf({
		fieldLabel : 'Parameters',
		name: 'parameters',
		height: 200,
		width: 350,
		disabled: true
	}, defaults));
	
	this.ss.fields.startDate = new Ext.form.TextField(Ext.applyIf({
		fieldLabel: 'Start',
		name: 'startDate',
		width: 150,
		disabled: true
	}, defaults));

	this.ss.fields.endDate = new Ext.form.TextField(Ext.applyIf({
		fieldLabel: 'End',
		name: 'endDate',
		width: 150,
		disabled: true
	}, defaults));

	var messagePanel = new Ext.Panel({ 
		autoDestroy:true,
		defaults:{
			border:false
		},
		style: {
			'text-align':'center',
			'padding':'5px'
		},
		border:false
	});

	var submitText = 'Submit';
	if(formPanelConfig.submitText) {
		submitText = formPanelConfig.submitText;
		delete formPanelConfig.submitText;
	}
	
	var waitMsg = 'Saving Data...';
	if(formPanelConfig.waitMsg) {
		waitMsg = formPanelConfig.waitMsg;
		delete formPanelConfig.waitMsg;
	}
	
	this.ss.buttons.submit = new Ext.Button(Ext.applyIf({
		text: submitText
	}, buttonDefaults));

	this.ss.buttons.submit.on('click', function() {
		messagePanel.removeAll();
		messagePanel.doLayout();
		this.getForm().submit({
			waitMsg: waitMsg,
			success: function(form, action) {
				var json = action.response.responseJson||Ext.decode(action.response.responseText);
				if(json && json.response && json.response.message) {
					messagePanel.removeAll();
					messagePanel.add({
						html: json.response.message
					});
					messagePanel.doLayout();
				}
			},
			failure: function(form, action) {
				var json = action.response.responseJson||Ext.decode(action.response.responseText);
				if(json && json.globalErrors) {
					messagePanel.removeAll();
					for(var idx=0; idx < json.globalErrors.length; idx++) {
						messagePanel.add({
							html: json.globalErrors[idx],
							style: { 'color':'red' }
						});
						messagePanel.doLayout();
					}
				}
			},
			scope:this
		});
	}, this);
	
	formPanelConfig.items = [
	     this.ss.fields.name,
	     this.ss.fields.dataUrl,
	     this.ss.fields.parameters,
	     this.ss.fields.startDate,
	     this.ss.fields.endDate,
	     messagePanel
	];
	
	formPanelConfig.buttons = [
	     this.ss.buttons.submit
	];
	
	Ext.ss.data.ScanFormPanel.superclass.constructor.call(this, formPanelConfig);
};

Ext.extend(Ext.ss.data.ScanFormPanel, Ext.form.FormPanel, {
	
	ss: {
		fields: {},
		buttons: {}
	}
});
