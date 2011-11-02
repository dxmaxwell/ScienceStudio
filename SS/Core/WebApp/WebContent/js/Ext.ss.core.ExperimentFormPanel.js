/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *      ExperimentFormPanel class.
 *     
 */
Ext.namespace('Ext.ss.core');

Ext.ss.core.ExperimentFormPanel = function(config) {
	
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
	
	this.ss.fields.sessionGid = new Ext.form.Hidden(Ext.applyIf({
		name: 'sessionGid',
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

	if(formPanelConfig.sampleStore) {
		this.ss.stores.sample  = formPanelConfig.sampleStore;
		delete formPanelConfig.sampleStore;
	} 
	else {
		this.ss.stores.sample = new Ext.data.JsonStore({
			autoDestroy:true,
			fields:[{
				name:'gid'
			},{
				name:'name'
			}]
		});
	}
	
	this.ss.fields.sample = new Ext.form.ComboBox(Ext.applyIf({
		fieldLabel: 'Sample',
		hiddenName: 'sourceGid',
		store:this.ss.stores.sample,
		triggerAction:'all',
		mode: 'local',
		editable:false,
		forceSelection:true,
		valueField:'gid',
		displayField:'name',
		emptyText:'Select a sample...',
		width: 150
	}, defaults));
	
	if(formPanelConfig.instrumentStore) {
		this.ss.stores.instrumentStore = formPanelConfig.instrumentStore;
		delete formPanelConfig.instrumentStore;
	} 
	else {
		this.ss.stores.instrumentStore = new Ext.data.JsonStore({
			autoDestroy:true,
			fields:[{
				name:'gid'
			},{
				name:'name'
			},{
				name:'longName'
			}]
		});
	}
	
	this.ss.fields.instrument = new Ext.form.ComboBox(Ext.applyIf({
		fieldLabel: 'Instrument',
		hiddenName: 'instrumentGid',
		store:this.ss.stores.instrumentStore,
		triggerAction:'all',
		mode: 'local',
		editable:false,
		forceSelection:true,
		valueField:'gid',
		displayField:'longName',
		emptyText:'Select an instrument...',
		width: 150
	}, defaults));
	
	this.ss.fields.instrument.on('select', function(combo, record, index) {
		this.ss.fields.instrumentTechnique.clearValue();
		this.ss.stores.instrumentTechnique.clearFilter(false);
		this.ss.stores.instrumentTechnique.filterBy(function(record, id) {
			return (record.get('instrumentGid') == this.ss.fields.instrument.getValue());
		}, this);
	}, this);
	
	if(formPanelConfig.instrumentTechniqueStore) {
		this.ss.stores.instrumentTechnique = formPanelConfig.instrumentTechniqueStore;
		delete formPanelConfig.instrumentTechniqueStore;
	} 
	else {
		this.ss.stores.instrumentTechnique = new Ext.data.JsonStore({
			autoDestroy:true,
			fields:[{
				name:'gid'
			},{
				name:'instrumentGid'
			},{
				name:'techniqueGid'
			},{
				name:'instrumentLongName'
			},{
				name:'techniqueLongName'
			}]
		});
	}

	this.ss.fields.instrumentTechnique = new Ext.form.ComboBox(Ext.applyIf({
		fieldLabel: 'Technique',
		hiddenName: 'instrumentTechniqueGid',
		store:this.ss.stores.instrumentTechnique,
		lastQuery:'', /* Ensure the initial filter is not cleared. */
		triggerAction:'all',
		mode: 'local',
		editable:false,
		forceSelection:true,
		valueField:'gid',
		displayField:'techniqueLongName',
		emptyText:'Select a technique...',
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
	
	var waitMsg = 'Submitting Data...';
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
         this.ss.fields.sessionGid,
	     this.ss.fields.name,
	     this.ss.fields.description,
	     this.ss.fields.sample,
	     this.ss.fields.instrument,
	     this.ss.fields.instrumentTechnique,
	     messagePanel
	];
	
	formPanelConfig.buttons = [
	     this.ss.buttons.submit
	];
	
	Ext.ss.core.ExperimentFormPanel.superclass.constructor.call(this, formPanelConfig);
};

Ext.extend(Ext.ss.core.ExperimentFormPanel, Ext.form.FormPanel, {
	
	ss: {
		stores: {},
		fields: {},
		buttons: {}
	}
});
