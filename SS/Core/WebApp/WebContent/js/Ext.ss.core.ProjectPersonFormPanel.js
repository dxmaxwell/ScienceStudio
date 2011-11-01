/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *     ProjectPersonFormPanel class.
 *     
 */
Ext.namespace('Ext.ss.core');

Ext.ss.core.ProjectPersonFormPanel = function(config) {
	
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
	
	var projectPersonStore;
	if(formPanelConfig.projectPersonStore) {
		projectPersonStore  = formPanelConfig.projectPersonStore;
		delete formPanelConfig.projectPersonStore;
	} 
	else {
		projectPersonStore = new Ext.data.JsonStore({
			autoDestroy:true,
			fields: [{
				name:'gid'
			},{
				name:'personGid'
			},{
				name:'projectGid'
			},{
				name:'fullName'
			},{
				name:'emailAddress'
			},{
				name:'phoneNumber'
			},{
				name:'mobileNumber'
			},{
				name:'role'
			}]
		});
	}
	
	this.ss.fields.personGid = new Ext.form.ComboBox(Ext.applyIf({
		fieldLabel:'Name',
		hiddenName:'personGid',
		store:projectPersonStore,
		mode:'remote',
		triggerAction:'all',
		forceSelection:true,
		editable:true,
		minChars:2,
		queryParam:'name',
		valueField:'personGid',
		displayField:'fullName',
		emptyText:'Enter first or last name',
		width: 200
	}, defaults));
	
	this.ss.fields.personGid.on('select', function(combo, record, idx) {
		this.getForm().loadRecord(record);
	}, this);
	
	this.ss.fields.fullName = new Ext.form.Hidden(Ext.applyIf({
		name:'fullName'
	}, defaults));
	
	this.ss.fields.emailAddress = new Ext.form.TextField(Ext.applyIf({
		name:'emailAddress',
		fieldLabel:'Email',
		width: 200
	}, defaults));
	
	this.ss.fields.phoneNumber = new Ext.form.TextField(Ext.applyIf({
		name:'phoneNumber',
		fieldLabel:'Phone',
		width: 200
	}, defaults));
	
	this.ss.fields.mobileNumber = new Ext.form.TextField(Ext.applyIf({
		name:'mobileNumber',
		fieldLabel:'Mobile',
		width: 200
	}, defaults));
	
	var roleStore;
	if(formPanelConfig.roleStore) {
		roleStore  = formPanelConfig.roleStore;
		delete formPanelConfig.roleStore;
	} 
	else {
		roleStore = new Ext.data.JsonStore({
			autoDestroy:true,
			fields: [{ 
				name:'value'
			},{
				name:'display'
			}]
		});
	}
	
	this.ss.fields.role = new Ext.form.ComboBox(Ext.applyIf({
		name:'role',
		fieldLabel:'Role',
		store:roleStore,
		mode:'local',
		triggerAction:'all',
		forceSelection:true,
		editable:false,
		valueField:'value',
		displayField:'display',
		emptyText:'Select a role',
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
	
	this.ss.buttons.submit.on('click', function(button, event) {
		messagePanel.removeAll();
		messagePanel.doLayout();
		this.getForm().submit({
			clientValidation: false,
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
	     this.ss.fields.personGid,
	     this.ss.fields.projectGid,
	     this.ss.fields.fullName,
	     this.ss.fields.phoneNumber,
	     this.ss.fields.mobileNumber,
	     this.ss.fields.emailAddress,
	     this.ss.fields.role,
	     messagePanel
	];
	
	formPanelConfig.buttons = [
	     this.ss.buttons.submit                    
	];
	
	Ext.ss.core.ProjectPersonFormPanel.superclass.constructor.call(this, formPanelConfig);
};

Ext.extend(Ext.ss.core.ProjectPersonFormPanel, Ext.form.FormPanel, {
	
	ss: {
		fields: {},
		buttons: {}
	}
});
