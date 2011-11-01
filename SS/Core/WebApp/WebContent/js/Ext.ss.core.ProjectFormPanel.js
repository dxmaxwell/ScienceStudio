/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *      ProjectFormPanel class.
 *     
 */
Ext.namespace('Ext.ss.core');

Ext.ss.core.ProjectFormPanel = function(config) {
	
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
	
	this.ss.fields.gid = new Ext.form.Hidden(Ext.applyIf({
		name: 'gid',
		disabled:false
	}, defaults));
	
	this.ss.fields.name = new Ext.form.TextField(Ext.applyIf({
		fieldLabel: 'Name',
		name: 'name',
		width: 150
	}, defaults));
	
	this.ss.fields.description = new Ext.form.TextArea(Ext.applyIf({
		fieldLabel: 'Description',
		name: 'description',
		width: 150
	}, defaults));
	
	this.ss.fields.startDay = new Ext.form.DateField(Ext.applyIf({
		fieldLabel: 'Start Date',
		name: 'startDay',
		format:Date.patterns.ISO8601Short,
		altFormats:Date.altFormats,
		width: 150
	}, defaults));
	                
	this.ss.fields.endDay = new Ext.form.DateField(Ext.applyIf({
		fieldLabel: 'End Date',
		name: 'endDay',
		format:Date.patterns.ISO8601Short,
		altFormats:Date.altFormats,
		width: 150
	}, defaults));
	
	var statusStore;
	if(formPanelConfig.statusStore) {
		statusStore  = formPanelConfig.statusStore;
		delete formPanelConfig.statusStore;
	} 
	else {
		statusStore = new Ext.data.JsonStore({
			autoDestroy:true,
			fields: [{
				name:'value'
			},{
				name:'display'
			}]
		});
	}
	
	this.ss.fields.status = new Ext.form.ComboBox(Ext.applyIf({
		name:'status',
		fieldLabel: 'Status',
		mode:'local',
		store:statusStore,
		triggerAction:'all',
		editable:false,
		forceSelection:true,
		valueField:'value',
		displayField:'display',
		width: 150
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
	
	this.ss.buttons.submit.on('click', function(button, event) {
		messagePanel.removeAll();
		messagePanel.doLayout();
		this.getForm().submit({
			waitMsg: waitMsg,
			success: function(form, action) {
				var json = action.response.responseJson||Ext.decode(action.response.responseText);
				if(json && json.message) {
					messagePanel.removeAll();
					messagePanel.add({
						html: json.message
					});
					messagePanel.doLayout();
				}
			},
			failure: function(form, action) {
				var json = action.response.responseJson||Ext.decode(action.response.responseText);
				if(json && json.message) {
					messagePanel.removeAll();
					messagePanel.add({
						html: json.message,
						style: { 'color':'red' }
					});
					messagePanel.doLayout();
				}
			},
			scope:this
		});
	}, this);
	
	formPanelConfig.items = [
	     this.ss.fields.gid,
	     this.ss.fields.name,
	     this.ss.fields.description,
	     this.ss.fields.startDay,
	     this.ss.fields.endDay,
	     this.ss.fields.status,
	     messagePanel
	];
	
	formPanelConfig.buttons = [
	     this.ss.buttons.submit                   
	];
	
	Ext.ss.core.ProjectFormPanel.superclass.constructor.call(this, formPanelConfig);
};

Ext.extend(Ext.ss.core.ProjectFormPanel, Ext.form.FormPanel, {
	
	ss: {
		fields: {},
		buttons: {}
	}
});
