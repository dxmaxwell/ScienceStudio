/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *  	errorHandlers.js
 *
 */

function dataStoreExceptionHandler(dataProxy, type, action, options, response, arg) {
	
	if(type == 'response') {
		Ext.Msg.alert('Error', 'Network connection problem.');
	}
	else { 
		if(response && response.message) {
			Ext.Msg.alert('Message', response.message);
		}
		else {
			Ext.Msg.alert('Error', 'An unspecified error has occurred.');
		}
	}
};

var formActionFailureHandlerOptions = { 
	messageProperty:'message'
};

function formActionFailureHandler(form, action) {

	if(action.failureType === Ext.form.Action.CONNECT_FAILURE) {
		Ext.Msg.alert('Error', 'Network connection problem.');
	}
	else if(action.failureType === Ext.form.Action.SERVER_INVALID) {
		var messageProperty = formActionFailureHandlerOptions.messageProperty;
		if(action.result && action.result[messageProperty]) {
			Ext.Msg.alert('Message', action.result[messageProperty]);
		}
		else {
			Ext.Msg.alert('Error', 'An unspecified error has occurred.');
		}
	}
};
