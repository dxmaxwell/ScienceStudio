/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *      SessionFormPanel class.
 *     
 */
Ext.namespace('Ext.ss.core');

Ext.ss.core.SessionFormPanel = function(config) {
	
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
		name:'gid',
		disabled:false
	}, defaults));
	
	this.ss.fields.projectGid = new Ext.form.Hidden(Ext.applyIf({
		name:'projectGid',
		disabled:false
	}, defaults));
	
	this.ss.fields.name = new Ext.form.TextField(Ext.applyIf({
		fieldLabel: 'Name',
		name: 'name',
		width: 200
	}, defaults));
	
	this.ss.fields.description = new Ext.form.TextArea(Ext.applyIf({
		fieldLabel: 'Description',
		name: 'description',
		width: 200
	}, defaults));
	
	this.ss.fields.proposal = new Ext.form.TextField(Ext.applyIf({
		fieldLabel: 'Proposal',
		name: 'proposal',
		width: 100
	}, defaults));
	
	var laboratoryStore;
	if(formPanelConfig.laboratoryStore) {
		laboratoryStore  = formPanelConfig.laboratoryStore;
		delete formPanelConfig.laboratoryStore;
	} 
	else {
		laboratoryStore = new Ext.data.JsonStore({
			autoDestroy:true,
			fields:[{
				name:'gid'
			},{
				name:'longName'
			}]
		});
	}
	
	this.ss.fields.laboratory = new Ext.form.ComboBox(Ext.applyIf({
		fieldLabel: 'Laboratory',
		hiddenName: 'laboratoryGid',
		store:laboratoryStore,
		mode: 'local',
		triggerAction:'all',
		editable:false,
		forceSelection:true,
		valueField:'gid',
		displayField:'longName',
		emptyText:'Select a laboratory...',
		width: 200
	}, defaults));
	
	this.ss.fields.startDay = new Ext.form.DateField(Ext.applyIf({
		fieldLabel: 'Start Date',
		name: 'startDay',
		format: Date.patterns.ISO8601Date,
		altFormats:Date.altDateFormats,
		width: 100
	}, defaults));
	                
	this.ss.fields.endDay = new Ext.form.DateField(Ext.applyIf({
		fieldLabel: 'End Date',
		name: 'endDay',
		format: Date.patterns.ISO8601Date,
		altFormats:Date.altDateFormats,
		width: 100
	}, defaults));
	
	this.ss.fields.startTime = new Ext.form.TimeField(Ext.applyIf({
		fieldLabel: 'Start Time',
		name: 'startTime',
		format: Date.patterns.ISO8601TimeShrt,
		altFormats: Date.altTimeFormats,
		minValue: '00:00',
		maxValue: '23:30',
		increment: 30,
		width: 100
	}, defaults));
	
	this.ss.fields.endTime = new Ext.form.TimeField(Ext.applyIf({
		fieldLabel: 'End Time',
		name: 'endTime',
		format: Date.patterns.ISO8601TimeShrt,
		altFormats: Date.altTimeFormats,
		minValue: '00:00',
		maxValue: '23:30',
		increment: 30,
		width: 100
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
		text:submitText
	}, buttonDefaults));
	
	this.ss.buttons.submit.on('click', function() {
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
	     this.ss.fields.projectGid,
	     this.ss.fields.name,
	     this.ss.fields.description,
	     this.ss.fields.laboratory,
	     this.ss.fields.proposal,
	     this.ss.fields.startDay,
	     this.ss.fields.startTime,
	     this.ss.fields.endDay,
	     this.ss.fields.endTime,
	     messagePanel
	];
	
	formPanelConfig.buttons = [
	     this.ss.buttons.submit
	];
	
	Ext.ss.core.SessionFormPanel.superclass.constructor.call(this, formPanelConfig);
};

Ext.extend(Ext.ss.core.SessionFormPanel, Ext.form.FormPanel, {
	
	ss: {
		fields: {},
		buttons: {}
	}
});
