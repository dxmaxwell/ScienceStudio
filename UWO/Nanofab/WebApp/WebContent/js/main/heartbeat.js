/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *  	heartbeat.js
 *
 */

var heartbeatData = {};
var heartbeatDataInitialized = false;

var heartbeatTasksToStart = [];

var heartbeatInterval = 4000; /* milliseconds */

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
		if(json && json.response) {
			heartbeatData = json.response;
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
	Ext.Msg.wait('Loading LEO1540XB session...');
	Ext.TaskMgr.start(heartbeatLoadTask);
};

//var laboratoryHeartbeatFields = [               
//	 // Laboratory Session //
//    { name:'lsProjectName', mapping:'laboratorySession/projectName' },
//    { name:'lsSessionName', mapping:'laboratorySession/sessionName' },
//    { name:'lsSessionId', mapping:'laboratorySession/sessionId' },
//    //{ name:'lsProposalId', mapping:'laboratorySession/proposalId' },
//    { name:'lsSessionTimeout', mapping:'laboratorySession/sessionTimeout', convert:convertSecondsToHHMM },
//    { name:'lsExperimentId', mapping:'laboratorySession/experimentId' },
//    { name:'lsExperimentName', mapping:'laboratorySession/experimentName' },
//    //{ name:'lsScanId', mapping:'laboratorySession/scanId' },
//    //{ name:'lsScanName', mapping:'laboratorySession/scanName' },
//    { name:'lsPersonKey', mapping:'laboratorySession/personKey' },
//    { name:'lsPersonName', mapping:'laboratorySession/personName' }
//];

//var laboratoryHeartbeatReader = new Ext.data.XmlReader(
//	{
//		record: 'devices',
//		success: '@success'
//	},
//	laboratoryHeartbeatFields
//);
//
//var laboratoryHeartbeatRecord = false;

//beamlineHeartbeatStore.on('beforeload', function(store, options) {
//	if(beamlineHeartbeatLoading) {
//		return false;
//	}
//	else {
//		beamlineHeartbeatLoading = true;
//		return true;
//	}	
//});

//beamlineHeartbeatStore.on('load', function(store, records, options) {
//	beamlineHeartbeatLoading = false;
//	parent.progressMeter.showProgress();
//});

//beamlineHeartbeatStore.on('loadexception', function() {
//	beamlineHeartbeatLoading = false;
//});

//var laboratoryHeartbeatLoading = false;
//
//var laboratoryHeartbeatSuccess = function(response, opts) {
//	var records = laboratoryHeartbeatReader.read(response);
//	if(records.success && (records.records.length > 0)) {
//		laboratoryHeartbeatRecord = records.records[0];
//		//parent.progressMeter.showProgress();
//	}
//	
//	laboratorySessionFormLoad();
//	
//	laboratoryHeartbeatLoading = false;	
//};

//var laboratoryHeartbeatFailure = function(response, opts) {
//	laboratoryHeartbeatLoading = false;
//};
//
//var laboratoryHeartbeatLoad = function() {
//	if(!laboratoryHeartbeatLoading) {
//		laboratoryHeartbeatLoading = true;
//		Ext.Ajax.request({
//			method: 'GET',
//			url: 'heartbeat.xml',
//			success: laboratoryHeartbeatSuccess,
//			failure: laboratoryHeartbeatFailure
//		});
//	}
//};
//
//var laboratoryHeartbeatTask = {
//	run: laboratoryHeartbeatLoad,
//	interval: 1000
//};
//
//Ext.TaskMgr.start(laboratoryHeartbeatTask);