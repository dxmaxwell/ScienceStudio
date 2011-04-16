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
	
	this.ss.fields.id = new Ext.form.Hidden(Ext.applyIf({
		name:'id'
	}, defaults));
	
	this.ss.fields.projectId = new Ext.form.Hidden(Ext.applyIf({
		name:'projectId'
	}, defaults));
	
	var projectPersonStore;
	if(formPanelConfig.projectPersonStore) {
		projectPersonStore  = formPanelConfig.projectPersonStore;
		delete formPanelConfig.projectPersonStore;
	} 
	else {
		projectPersonStore = new Ext.data.JsonStore({
			autoDestroy:true,
			root:'response',
			fields: [{
				name:'id', mapping:'projectPersonFormBacker.id'
			},{
				name:'personUid', mapping:'projectPersonFormBacker.personUid'
			},{
				name:'projectId', mapping:'projectPersonFormBacker.projectId'			
			},{
				name:'fullName', mapping:'projectPersonFormBacker.fullName'
			},{
				name:'emailAddress', mapping:'projectPersonFormBacker.emailAddress'
			},{
				name:'phoneNumber', mapping:'projectPersonFormBacker.phoneNumber'
			},{
				name:'mobileNumber', mapping:'projectPersonFormBacker.mobileNumber'
			},{
				name:'projectRole', mapping:'projectPersonFormBacker.projectRole'
			}]
		});
	}
	
	this.ss.fields.personUid = new Ext.form.ComboBox(Ext.applyIf({
		fieldLabel:'Name',
		hiddenName:'personUid',
		store:projectPersonStore,
		mode:'remote',
		triggerAction:'all',
		forceSelection:true,
		editable:true,
		minChars:2,
		valueField:'personUid',
		displayField:'fullName',
		emptyText:'Enter first or last name',
		width: 200
	}, defaults));
	
	this.ss.fields.personUid.on('select', function(combo, record, idx) {
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
	
	var projectRoleStore;
	if(formPanelConfig.projectRoleStore) {
		projectRoleStore  = formPanelConfig.projectRoleStore;
		delete formPanelConfig.projectRoleStore;
	} 
	else {
		projectRoleStore = new Ext.data.JsonStore({
			root:'response',
			success:'success',
			fields: [{ 
				name:'name', mapping:'projectRole.name'
			},{
				name:'longName', mapping:'projectRole.longName'
			}]
		});
	}
	
	this.ss.fields.projectRole = new Ext.form.ComboBox(Ext.applyIf({
		name:'projectRole',
		fieldLabel:'Role',
		store:projectRoleStore,
		mode:'local',
		triggerAction:'all',
		forceSelection:true,
		editable:false,
		valueField:'name',
		displayField:'longName',
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
	     this.ss.fields.id, 
	     this.ss.fields.personUid,
	     this.ss.fields.projectId,
	     this.ss.fields.fullName,
	     this.ss.fields.phoneNumber,
	     this.ss.fields.mobileNumber,
	     this.ss.fields.emailAddress,
	     this.ss.fields.projectRole,
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
