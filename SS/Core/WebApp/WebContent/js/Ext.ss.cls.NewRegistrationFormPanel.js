/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *     NewRegistrationFormPanel class.
 *     
 *     Depreciated, but useful for future development.
 */

Ext.namespace('Ext.ss.cls');

Ext.ss.cls.NewRegistrationFormPanel = function(successURL, config) {
	
	Ext.form.Field.prototype.msgTarget = 'side';
	
	var accountBaseURL = '/ss/account';
	var required = '<span style="color:red">*</span>';
	
	var titleStore = new Ext.data.SimpleStore({
		fields: ['value', 'text'],
		data: [
		    ['Prof.', 'Prof.'],
			['Dr.', 'Dr.'],
			['Mr.', 'Mr.'],
			['Ms.', 'Ms.'],
			['Miss', 'Miss']
		]
	});
	
	var titleCmbx = new Ext.form.ComboBox({
		fieldLabel: 'Title',
		name: 'title',
		width: 100,
		store: titleStore,
		mode: 'local',
		triggerAction: 'all',
		forceSelection: true,
		valueField: 'value',
		displayField: 'text'
	});
	
	var firstNameTxtFld = new Ext.form.TextField({
		fieldLabel: 'First name' + required,
		name: 'firstName',
		width: 200
	});
	
	var middleInitialTxtFld = new Ext.form.TextField({
		fieldLabel: 'Middle initial',
		name: 'middleInitial',
		width: 100
	});
	
	var lastNameTxtFld = new Ext.form.TextField({
		fieldLabel: 'Last name' + required,
		name: 'lastName',
		width: 200
	});
	
	var phoneTxtFld = new Ext.form.TextField({
		fieldLabel: 'Phone' + required,
		name: 'telephoneNumber',
		width: 200
	});
	
	var faxTxtFld = new Ext.form.TextField({
		fieldLabel: 'Fax',
		name: 'facsimileNumber',
		width: 200
	});
	
	var emailTxtFld = new Ext.form.TextField({
		fieldLabel: 'Email' + required,
		name: 'emailAddress',
		width: 200
	});
	
	var confirmEmailTxtFld = new Ext.form.TextField({
		fieldLabel: 'Confirm email' + required,
		name: 'confirmEmailAddress',
		width: 200
	});
	
	var fieldset = new Ext.form.FieldSet({
		title: 'Contact',
		items: [ titleCmbx,
		         firstNameTxtFld,
		         middleInitialTxtFld,
		         lastNameTxtFld,
		         phoneTxtFld,
		         faxTxtFld,
		         emailTxtFld,
		         confirmEmailTxtFld
		       ]
	});
	
	var submitBtn = new Ext.Button({
		text: 'Submit'
	});
	
	var formPanel = new Ext.form.FormPanel({
		frame: true,
		border: true,
        labelAlign: 'right',
        labelWidth: 85,
        waitMsgTarget: true,
        labelPad: 10,
        layoutConfig: { labelSeparator: ':' },
        defaults:{
			width:'auto',
			height:'auto'
		},
		errorReader: new Ext.ss.cls.XmlErrorReader(),
		
		items:[ fieldset ],
		buttons:[ submitBtn ],
		buttonAlign:'left'
		
	});
	
	var successCallback = function(form, action) {
		var xml = action.response.responseXML;
		if(xml) {
			var msg = Ext.DomQuery.selectValue('response/message', xml);
			if(msg) {
				Ext.Msg.alert('Message', msg, function(){
					window.location = successURL;
				}, this);
			}
		}
	};
	
	var failureCallback = function(form, action) {
		var xml = action.response.responseXML;
		if(xml) {
			var msg = Ext.DomQuery.selectValue('response/message', xml);
			if(msg) { 
				Ext.Msg.alert('Error', msg);
			}			
			var act = Ext.DomQuery.selectValue('response/action', xml);		
			if(act == 'reload') {
				window.location.reload(); 
			}	
		} else {
			Ext.Msg.alert('Error', 'Invalid Server Response!');
		}
	};
	
	submitBtn.on('click', function() {
		formPanel.getForm().submit({
			method: 'POST',
			url: accountBaseURL + '/submitRegistration.xml',
			waitMsg:'Submitting...',
			success: successCallback,
			failure: failureCallback
		});
	});
	
	config.items = [ formPanel ];
	
	Ext.ss.cls.NewRegistrationFormPanel.superclass.constructor.call(this, config);
};

Ext.extend(Ext.ss.cls.NewRegistrationFormPanel, Ext.Panel);
