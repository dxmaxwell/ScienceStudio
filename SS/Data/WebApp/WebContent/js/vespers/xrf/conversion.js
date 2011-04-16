/**
 * Copyright (c) Canadian Light Source, Inc. All rights reserved. - see
 * license.txt for details.
 *
 * Description:
 * 		conversion.js
 *
 */

function convert(fromFormat, toFormat, complete, scope) {
	
	var conversionDefaultMessage = 'Please wait for conversion...';
	var conversionRequestPending = false;
	var conversionRequestProgress = 0.0;

	Ext.Msg.progress(conversionDefaultMessage);
		
	function conversionRequest() {
		if(!conversionRequestPending) {
			Ext.Ajax.request({
				url:'../../scan/' + scanId + '/convert/' + fromFormat + '/' + toFormat + '.json',
				callback:conversionRequestCallback,
				disableCaching:false,
				scope:this
			});
			conversionRequestPending = true;
		}
	}
	
	function conversionRequestCallback(options, success, response) {
		conversionRequestPending = false;
		if(success) {
			var json = response.responseJson||Ext.decode(response.responseText, true);
			if(json) {
				if(json.success) {
					if(json.response.complete) {
						Ext.TaskMgr.stop(conversionRequestTask);
						Ext.Msg.hide();
						if(complete) {
							if(scope) {
								complete.call(scope);
							} else {
								complete.call(this);
							}
						}			
						return;
					}
						
					conversionRequestProgress += 0.1;
					if(conversionRequestProgress > 1.0) {
						conversionRequestProgress = 0.0;
					}
					
					if(json.response.message) {
						Ext.Msg.updateProgress(conversionRequestProgress, '', json.response.message);
					} else {
						Ext.Msg.updateProgress(conversionRequestProgress, '', conversionDefaultMessage);
					}
				}
				else {
					Ext.TaskMgr.stop(conversionRequestTask);
					Ext.Msg.hide();
					
					if(json.globalErrors && json.globalErrors[0]) {
						Ext.Msg.alert('Error', json.globalErrors[0]);
					} else {
						Ext.Msg.alert('Error', 'An unknown error has occurred.');
					}
				}
			}
		}
	}
	
	var conversionRequestTask = {
		run:conversionRequest,
		interval:1000,
		scope:this
	};
	
	Ext.TaskMgr.start(conversionRequestTask);
}
