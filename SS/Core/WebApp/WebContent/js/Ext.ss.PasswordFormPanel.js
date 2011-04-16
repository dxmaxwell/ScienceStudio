/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *      PasswordFormPanel class.
 *      
 *      Depreciated, but useful for future development.
 */
Ext.namespace('Ext.ss.cls');

Ext.ss.cls.PasswordFormPanel = function(config) {
	
	Ext.form.Field.prototype.msgTarget = 'side';
	
	this.ss = {
		submitUrl: accountBaseURL + '/submitPassword.xml',
		submitParams:{},
		fieldWidth:150
	};
	
	/* Current Password Field */
	this.ss.password = new Ext.form.TextField({
		name:'password',
		fieldLabel:'Current Password',
		inputType:'password',
		width:this.ss.fieldWidth
	});
	this.ss.password.on('focus', this.handlers.onFocus, this);
	
	/* New Password Field */
	this.ss.newPassword = new Ext.form.TextField({
		name:'newPassword',
		fieldLabel:'New Password',
		inputType:'password',
		width:this.ss.fieldWidth
	});
	this.ss.newPassword.on('focus', this.handlers.onFocus, this);
	
	/* New Password Confirmation Field */
	this.ss.newPasswordConfirm = new Ext.form.TextField({
		name:'newPasswordConfirm',
		fieldLabel:'Retype New Password',
		inputType:'password',
		width:this.ss.fieldWidth
	});
	this.ss.newPasswordConfirm.on('focus', this.handlers.onFocus, this);
	
	this.ss.passwordFormatPanel = new Ext.Panel({
		html: '<p>- password must be at least 7 characters long<br/>- password must contain at least one capital letter<br/>- password must contain at least one digit</p>'
	});
	
	/* Buttons */	
	this.ss.cancelButton = new Ext.Button({
		text:'Cancel',
		handler:this.handlers.clickCancelButton,
		scope:this
	});

	this.ss.saveButton = new Ext.Button({
		text:'Save',
		handler:this.handlers.clickSaveButton,
		scope:this
	});
	
	this.ss.editPasswordSubmitReader = new Ext.ss.cls.XmlErrorReader(); 
	
	this.ss.editPasswordFormPanel = new Ext.FormPanel({
		formId:'editPassord',
		//reader:this.ss.editPersonLoadReader,
		errorReader:this.ss.editPasswordSubmitReader,
		//labelAlign:'top',
		labelWidth: 150,
		items:[{
			autoHeight:true,
			layout:'form',
			items:[
				this.ss.password,
				this.ss.newPassword,
				this.ss.newPasswordConfirm,
				this.ss.passwordFormatPanel
			]
		}],
		buttons:[
			this.ss.saveButton,
			this.ss.cancelButton
		],
		buttonAlign:'right',
		frame:true,
		height: 'auto',
		width: 'auto'
	});
	
	config.items = [
			this.ss.editPasswordFormPanel
	];
	
	Ext.ss.cls.PasswordFormPanel.superclass.constructor.call(this, config);
	
	this.handlers.clickCancelButton.call(this);
};

Ext.extend(Ext.ss.cls.PasswordFormPanel, Ext.Panel, {
	
	handlers:{
	
		clickCancelButton:function() {
			this.ss.cancelButton.hide();
			this.ss.saveButton.hide();
			this.ss.password.setValue('');
			this.ss.newPassword.setValue('');
			this.ss.newPasswordConfirm.setValue('');
		},
		
		clickSaveButton:function() {
			this.ss.editPasswordFormPanel.getForm().submit({
				method:'POST',
				url:this.ss.submitUrl,
				params:this.ss.submitParams,
				success:this.handlers.submitSuccess,
				failure:this.handlers.submitFailure,
				scope:this
			});
		},
		
		onFocus:function() {
			this.ss.cancelButton.show();
			this.ss.saveButton.show();
		},
		
		submitSuccess:function(form, action) {
			this.handlers.clickCancelButton.call(this);
			var xml = action.response.responseXML;
			if(xml) {
				var msg = Ext.DomQuery.selectValue('response/message', xml);
				if(msg) { 
					Ext.MessageBox.alert('Message', msg); 
				}			
			} else {
				Ext.MessageBox.alert('Message', 'Password Submission Success!');
			}
		},
		
		submitFailure:function(form, action) {
			var xml = action.response.responseXML;
			if(xml) {
				var msg = Ext.DomQuery.selectValue('response/message', xml);
				if(msg) { 
					Ext.MessageBox.alert('Error', msg); 
				}
			} else {
				Ext.MessageBox.alert('Error', 'Password Submission Failure!');
			}
		}
	}
});
