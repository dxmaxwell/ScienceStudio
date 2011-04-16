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
			root:'response',
			success: 'success',
			fields:[{
				name:'id', mapping:'sample.id'
			},{
				name:'name', mapping:'sample.name'
			}]
		});
	}
	
	this.ss.fields.sample = new Ext.form.ComboBox(Ext.applyIf({
		fieldLabel: 'Sample',
		hiddenName: 'sampleId',
		store:this.ss.stores.sample,
		triggerAction:'all',
		mode: 'local',
		editable:false,
		forceSelection:true,
		valueField:'id',
		displayField:'name',
		emptyText:'Select a sample...',
		width: 150
	}, defaults));
	
	if(formPanelConfig.instrumentTechniqueOptionStore) {
		this.ss.stores.instrumentTechniqueOption = formPanelConfig.instrumentStore;
		delete formPanelConfig.instrumentTechniqueOptionStore;
	} 
	else {
		this.ss.stores.instrumentTechniqueOption = new Ext.data.JsonStore({
			autoDestroy:true,
			root:'response',
			success: 'success',
			fields:[{
				name:'id', mapping:'instrumentTechniqueOption.id'
			},{
				name:'instrumentId', mapping:'instrumentTechniqueOption.instrumentId'
			},{			
				name:'techniqueId', mapping:'instrumentTechniqueOption.techniqueId'
			},{	
				name:'instrumentLongName', mapping:'instrumentTechniqueOption.instrumentLongName'	
			},{
				name:'techniqueLongName', mapping:'instrumentTechniqueOption.techniqueLongName'
			}]
		});
		
		this.ss.stores.instrumentTechniqueOption.on('load', function(store, records, options) {
			instrumentStore.removeAll();
			instrumentStore.add(records);
			techniqueStore.removeAll();
			techniqueStore.add(records);
		}, this);
	}
	
	instrumentStore = new Ext.data.JsonStore({
		autoDestroy:true,
		root:'response',
		success: 'success',
		fields:[{
			name:'id', mapping:'instrumentTechniqueOption.id'
		},{
			name:'instrumentId', mapping:'instrumentTechniqueOption.instrumentId'
		},{			
			name:'techniqueId', mapping:'instrumentTechniqueOption.techniqueId'
		},{	
			name:'instrumentLongName', mapping:'instrumentTechniqueOption.instrumentLongName'	
		},{
			name:'techniqueLongName', mapping:'instrumentTechniqueOption.techniqueLongName'
		}]
	});
	
	this.ss.fields.instrument = new Ext.form.ComboBox(Ext.applyIf({
		fieldLabel: 'Instrument',
		hiddenName: 'instrumentId',
		store:instrumentStore,
		triggerAction:'all',
		mode: 'local',
		editable:false,
		forceSelection:true,
		valueField:'instrumentId',
		displayField:'instrumentLongName',
		emptyText:'Select an instrument...',
		width: 150
	}, defaults));
	
	this.ss.fields.instrument.on('beforequery', function(queryEvent) {
		
		instrumentStore.removeAll();
		
		var records = [];
		for(var idx = 0; idx < this.ss.stores.instrumentTechniqueOption.getCount(); idx++) {
			
			var record = this.ss.stores.instrumentTechniqueOption.getAt(idx);
			if(record) {
				var found = false;
				for(var i = 0; i < records.length; i++) {
					if(record.get('instrumentId') == records[i].get('instrumentId')) {
						found = true;
						break;
					}
				}
				
				if(!found) {
					records.push(record);
				}
			}	
		}
		
		instrumentStore.add(records);
		
	}, this);

	techniqueStore = new Ext.data.JsonStore({
		autoDestroy:true,
		root:'response',
		success: 'success',
		fields:[{
			name:'id', mapping:'instrumentTechniqueOption.id'
		},{
			name:'instrumentId', mapping:'instrumentTechniqueOption.instrumentId'
		},{			
			name:'techniqueId', mapping:'instrumentTechniqueOption.techniqueId'
		},{	
			name:'instrumentLongName', mapping:'instrumentTechniqueOption.instrumentLongName'	
		},{
			name:'techniqueLongName', mapping:'instrumentTechniqueOption.techniqueLongName'
		}]
	});

	this.ss.fields.technique = new Ext.form.ComboBox(Ext.applyIf({
		fieldLabel: 'Technique',
		hiddenName: 'instrumentTechniqueId',
		store:techniqueStore,
		triggerAction:'all',
		mode: 'local',
		editable:false,
		forceSelection:true,
		valueField:'id',
		displayField:'techniqueLongName',
		emptyText:'Select a technique...',
		width: 150
	}, defaults));
	
	this.ss.fields.technique.on('beforequery', function(queryEvent) {
		
		techniqueStore.removeAll();
		
		var instrumentId = this.ss.fields.instrument.getValue();
		if(instrumentId) {
			var records = this.ss.stores.instrumentTechniqueOption.query('instrumentId', instrumentId);
			techniqueStore.add(records.getRange());
		}
		
	}, this);
	
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
	     this.ss.fields.description,
	     this.ss.fields.sample,
	     this.ss.fields.instrument,
	     this.ss.fields.technique,
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
