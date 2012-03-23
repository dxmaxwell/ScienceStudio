/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *    heartbeat.js
 *    
 */

var heartbeatData = {};
var heartbeatDataInitialized = false;

var heartbeatTasksToStart = [];

var xrfTasksToStart = [];
var xrdTasksToStart = [];

var heartbeatInterval = 1000; /* milliseconds */

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
			
			// TODO more technique cases need to be considered
			var beamlineSession = heartbeatData.beamlineSession;
				if(beamlineSession && beamlineSession.technique && beamlineSession.techniqueChanged === 'Yes') {
					if (beamlineSession.technique === 'XRF') {
						if (fourElementDetectorPanel && fourElementDetectorPanel.disabled) {
							fourElementDetectorPanel.enable();
							for(var idx in xrfTasksToStart) {
								Ext.TaskMgr.start(xrfTasksToStart[idx]);
							}
						}
						if (xrdPanel && !xrdPanel.disabled) {
							xrdPanel.disable();
							for(var idx in xrdTasksToStart) {
								Ext.TaskMgr.stop(xrdTasksToStart[idx]);
							}
						}	
						
					} else if (beamlineSession.technique === ('XRD')) {
						if (xrdPanel && xrdPanel.disabled) {
							xrdPanel.enable();
							for(var idx in xrdTasksToStart) {
								Ext.TaskMgr.start(xrdTasksToStart[idx]);
							}
						}
						if (fourElementDetectorPanel && !fourElementDetectorPanel.disabled) {
							fourElementDetectorPanel.disable();
							for(var idx in xrfTasksToStart) {
								Ext.TaskMgr.stop(xrfTasksToStart[idx]);
							}
						}
							
					} else if (beamlineSession.technique.indexOf('XRF') != -1 && beamlineSession.technique.indexOf('XRD') != -1) {
						if (fourElementDetectorPanel && fourElementDetectorPanel.disabled) {
							fourElementDetectorPanel.enable();
							for(var idx in xrfTasksToStart) {
								Ext.TaskMgr.start(xrfTasksToStart[idx]);
							}
						}
						if (xrdPanel && xrdPanel.disabled) {
							xrdPanel.enable();
							for(var idx in xrdTasksToStart) {
								Ext.TaskMgr.start(xrdTasksToStart[idx]);
							}
						}
					}
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
	Ext.Msg.wait('Loading VESPERS session...');
	Ext.TaskMgr.start(heartbeatLoadTask);
};
