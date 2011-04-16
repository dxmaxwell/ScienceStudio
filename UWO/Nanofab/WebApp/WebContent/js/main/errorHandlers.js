/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *  	errorHandlers.js
 *
 */

function dataStoreExceptionHandler(dataProxy, type, action, options, response, arg) {
	
	if(type == 'response') {
		Ext.Msg.alert('Error', 'Network problem or unexpected response format.');
	}
	else if(response.globalErrors && response.globalErrors[0]) {
		Ext.Msg.alert('Error', response.globalErrors[0]);
	}
	else {
		Ext.Msg.alert('Error', 'An unspecified error has occurred.');
	}
	
	return true;
};

function ajaxRequestCallbackHandler(options, success, response) {
	if(success) {
		return ajaxRequestSuccessHandler(response, options);
	}
	else {
		return ajaxRequestFailureHandler(response, options);
	}
}

function ajaxRequestSuccessHandler(response, options) {
	var json = Ext.decode(response.responseText, true);
	if(!json) {
		Ext.Msg.alert('Error', 'Unexpected response format. (1)');
		return true;
	}
		
	if(json.success == undefined) {
		Ext.Msg.alert('Error', 'Unexpected response format. (2)');
		return true;
	}
	
	if(json.success == true) {
		return false;
	}
	
	if(json.globalErrors && json.globalErrors[0]) {
		Ext.Msg.alert('Error', json.globalErrors[0]);
		return true;
	}
	
	Ext.Msg.alert('Error', 'An unspecified error has occurred.');
	return true;
};

function ajaxRequestFailureHandler(response, options) {
	Ext.Msg.alert('Error', 'Network problem has occurred');
	return true;
};
