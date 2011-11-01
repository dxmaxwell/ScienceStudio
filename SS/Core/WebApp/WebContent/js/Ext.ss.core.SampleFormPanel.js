/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *      SampleFormPanel class.
 *     
 */
Ext.namespace('Ext.ss.core');

Ext.ss.core.SampleFormPanel = function(config) {
	
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
	
	this.ss.fields.casNumber = new Ext.form.TextField(Ext.applyIf({
		fieldLabel: 'CAS Number',
		name: 'casNumber',
		width: 200
	}, defaults));
	
	this.ss.fields.quantity = new Ext.form.TextField(Ext.applyIf({
		fieldLabel: 'Quantity',
		name: 'quantity',
		width: 200
	}, defaults));
	
	var stateStore;
	if(formPanelConfig.stateStore) {
		stateStore  = formPanelConfig.stateStore;
		delete formPanelConfig.stateStore;
	} 
	else {
		stateStore = new Ext.data.JsonStore({
			autoDestroy:true,
			fields:[{
				name:'value'
			},{
				name:'display'
			}]
		});
	}
	
	this.ss.fields.state = new Ext.form.ComboBox(Ext.applyIf({
		fieldLabel: 'State',
		hiddenName: 'state',
		store:stateStore,
		mode:'local',
		triggerAction:'all',
		editable:false,
		forceSelection:true,
		valueField:'value',
		displayField:'display',
		width: 200
	}, defaults));
	
	this.ss.fields.corrosive = new Ext.form.Checkbox(Ext.applyIf({
		boxLabel:'Corrosive',
		name:'corrosive',
		inputValue:'true'
	}, defaults));
	
	this.ss.fields.flammable = new Ext.form.Checkbox(Ext.applyIf({
		boxLabel:'Flammable',
		name:'flammable',
		inputValue:'true'
	}, defaults));
	
	this.ss.fields.reactive = new Ext.form.Checkbox(Ext.applyIf({
		boxLabel:'Reactive',
		name:'reactive',
		inputValue:'true'
	}, defaults));

	this.ss.fields.oxidizer = new Ext.form.Checkbox(Ext.applyIf({
		boxLabel:'Oxidizer',
		name:'oxidizer',
		inputValue:'true'
	}, defaults));

	this.ss.fields.toxic = new Ext.form.Checkbox(Ext.applyIf({
		boxLabel:'Toxic',
		name:'toxic',
		inputValue:'true'
	}, defaults));

	this.ss.fields.other = new Ext.form.Checkbox(Ext.applyIf({
		boxLabel:'Other',
		name:'other',
		inputValue:'true'
	}, defaults));

	this.ss.fields.otherHazards = new Ext.form.TextField(Ext.applyIf({
		fieldLabel: 'Other Hazards',
		name: 'otherHazards',
		width: 200
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
			waitMsg:waitMsg,
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
	     this.ss.fields.casNumber,
	     this.ss.fields.state,
	     this.ss.fields.quantity,
	     {
	    	 xtype:'checkboxgroup',
	    	 fieldLabel: 'Hazards',
	    	 name:'hazards',
	    	 columns: 2,
	    	 items: [
	    	      this.ss.fields.corrosive,
	    	      this.ss.fields.flammable,
	    	      this.ss.fields.reactive,
	    	      this.ss.fields.oxidizer,
	    	      this.ss.fields.toxic,
	    	      this.ss.fields.other
	    	 ]
	 	 },
	     this.ss.fields.otherHazards,
	     messagePanel
	];
	
	formPanelConfig.buttons = [
	     this.ss.buttons.submit
	];
		
	Ext.ss.core.SampleFormPanel.superclass.constructor.call(this, formPanelConfig);
};

Ext.extend(Ext.ss.core.SampleFormPanel, Ext.form.FormPanel, {
	
	ss: {
		fields: {},
		buttons: {}
	}
});
