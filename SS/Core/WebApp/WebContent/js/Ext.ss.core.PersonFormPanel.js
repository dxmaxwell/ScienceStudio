/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *      PersonFormPanel class.
 *     
 */
Ext.namespace('Ext.ss.core');

Ext.ss.core.PersonFormPanel = function(config) {
	
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
	
	var titleStore;
	if(formPanelConfig.statusStore) {
		titleStore  = formPanelConfig.titleStore;
		delete formPanelConfig.titleStore;
	}
	else {
		titleStore = new Ext.data.ArrayStore({
			autoDestroy:true,
			fields:[ 'value', 'text'],
			data:[
				['Prof.', 'Prof.'],
				['Dr.',   'Dr.'  ],
				['Mr.',   'Mr.'  ],
				['Mrs.',  'Mrs.' ],
				['Ms.',   'Ms.'  ],
				['Miss',  'Miss' ]
			]
		});
	}
	
	this.ss.fields.title = new Ext.form.ComboBox(Ext.applyIf({
		name:'title',
		fieldLabel:'Title',
		store:titleStore,
		valueField:'value',
		displayField:'text',
		mode:'local',
		triggerAction:'all',
		editable:false,
		forceSelection:true
	}, defaults));
	
	this.ss.fields.firstName = new Ext.form.TextField(Ext.applyIf({
		name:'firstName',
		fieldLabel:'First Name',
		width:200
	}, defaults));
	
	this.ss.fields.middleName = new Ext.form.TextField(Ext.applyIf({
		name:'middleName',
		fieldLabel:'Middle Initial(s)',
		width:200
	}, defaults));
	
	this.ss.fields.lastName = new Ext.form.TextField(Ext.applyIf({
		name:'lastName',
		fieldLabel:'Last Name',
		width:200
	}, defaults));

	this.ss.fields.phoneNumber = new Ext.form.TextField(Ext.applyIf({
		name:'phoneNumber',
		fieldLabel:'Phone Number',
		width:200
	}, defaults));
	
	this.ss.fields.mobileNumber = new Ext.form.TextField(Ext.applyIf({
		name:'mobileNumber',
		fieldLabel:'Mobile Number',
		width:200
	}, defaults));
	
	this.ss.fields.emailAddress = new Ext.form.TextField(Ext.applyIf({
		name:'emailAddress',
		fieldLabel:'Email Address',
		width:200
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
	
	this.ss.buttons.submit.on('click', function(button, event) {
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
	     this.ss.fields.title,
	     this.ss.fields.firstName,
	     this.ss.fields.middleName,
	     this.ss.fields.lastName,
	     this.ss.fields.phoneNumber,
	     this.ss.fields.mobileNumber,
	     this.ss.fields.emailAddress,
	     messagePanel
	];
	
	formPanelConfig.buttons = [
	     this.ss.buttons.submit
	];
	  	
	Ext.ss.core.PersonFormPanel.superclass.constructor.call(this, formPanelConfig);
};

Ext.extend(Ext.ss.core.PersonFormPanel, Ext.form.FormPanel, {

	ss: {
		fields: {},
		buttons: {}
	}
});
