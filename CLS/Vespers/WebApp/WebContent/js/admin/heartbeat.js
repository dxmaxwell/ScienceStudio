/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *    heartbeat.js
 *    
 */

/**
 *
 * @include "include.js"
 */

var heartbeatData = {};
var heartbeatDataInitialized = false;

var heartbeatTasksToStart = [];

var heartbeatInterval = 2000; /* milliseconds */

var heartbeatLoading = false;

var heartbeatActivityMeter = new ActivityMeter({
	url:'/ssstatic/img/activity-orange-purple.gif',
	applyTo:'HEARTBEAT_ACTIVITY_METER',
	replace:true
});

var heartbeatLoad = function() {
	if(!heartbeatLoading) {
		heartbeatLoading = true;
		Ext.Ajax.request({
			url: 'heartbeat.json',
			callback: heartbeatLoadCallback,
			success: heartbeatLoadSuccess
		});
	}
};

var heartbeatLoadCallback = function(options, success, response) {
	heartbeatLoading = false;
};

var heartbeatLoadSuccess = function(response, options) {
	if(response) {
		var json = response.responseJson||Ext.decode(response.responseText, true);
		if(json) {
			heartbeatData = json;
			if(!heartbeatDataInitialized) {
				heartbeatDataInitialized = true;
				for(var idx in heartbeatTasksToStart) {
					Ext.TaskMgr.start(heartbeatTasksToStart[idx]);
				}
				Ext.Msg.hide();
			}
			if(heartbeatActivityMeter) {
				heartbeatActivityMeter.showActivity();
			}
		}
	}
};

var heartbeatLoadTask = {
	run: heartbeatLoad,
	interval: heartbeatInterval,
	scope: this
};

function startHeartbeat() {
	//Ext.Msg.wait('Loading VESPERS session...');
	Ext.TaskMgr.start(heartbeatLoadTask);
};

