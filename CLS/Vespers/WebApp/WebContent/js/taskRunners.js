/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *     taskRunners.js
 *
 */

/**
 *
 * @include "include.js"
 */

/* Vortex detector removed for production release *

var vtxAcqTask = {
	run:vtxAcqLoad,
	interval:1000
};

var vtxSetupTask = {
	run:vtxSetupLoad,
	interval:1000
};

var vtxCountTask = {
	run:vtxCountLoad,
	interval:1000
};

var vtxDataTask = {
	//run:getVortexData,
	run:getVortexDataCSV,
	interval:3000
};

*/

/* CCD detector removed for production release *

var ccdModeTask = {
	run: ccdModeLoad,
	interval: 1000
};

var ccdSetupTask = {
	run: ccdSetupLoad,
	interval: 1000
};

var ccdStageTask = {
	run: ccdStageLoad,
	interval: 1000
};

var ccdAcquireTask = {
	run: ccdAcquireLoad,
	interval: 1000
};

var ccdCheckTask = {
	run: ccdCheckLoad,
	interval: 1000
};

var ccdAcquisitionTask = {
	run: ccdAcquisitionLoad,
	interval: 1000
};

var ccdFileTask = {
	run: ccdFileLoad,
	interval: 1000
};

var ccdFocusImageTask = {
	run: ccdFocusImageLoad,
	interval: 1000
};

*/

// Vortex detector removed for production release.
//var vtxAcqRunner = new Ext.util.TaskRunner();
//var vtxSetupRunner = new Ext.util.TaskRunner();
//var vtxCountRunner = new Ext.util.TaskRunner();
//var vtxDataRunner = new Ext.util.TaskRunner();

// CCD detector removed for production release.
//var ccdModeRunner = new Ext.util.TaskRunner();
//var ccdSetupRunner = new Ext.util.TaskRunner();
//var ccdStageRunner = new Ext.util.TaskRunner();
//var ccdAcquisitionRunner = new Ext.util.TaskRunner();
//var ccdFileRunner = new Ext.util.TaskRunner();
//var ccdAcquireRunner = new Ext.util.TaskRunner();
//var ccdCheckRunner = new Ext.util.TaskRunner();
//var ccdFocusImageRunner = new Ext.util.TaskRunner();

function startRunners() {
	//vtxAcqRunner.start(vtxAcqTask);
	//vtxSetupRunner.start(vtxSetupTask);
	//vtxEnergyRunner.start(vtxEnergyTask);
	//vtxCountRunner.start(vtxCountTask);
	//vtxDataRunner.start(vtxDataTask);
	
	// shift most jobs to panel itself
	//ccdModeRunner.start(ccdModeTask);
}

function stopRunners() {
	//vtxAcqRunner.stop(vtxAcqTask);
	//vtxSetupRunner.stop(vtxSetupTask);
	//vtxEnergyRunner.stop(vtxEnergyTask);
	//vtxCountRunner.stop(vtxCountTask);
	//vtxDataRunner.stop(vtxDataTask);
	
	//ccdSetupRunner.stop(ccdSetupTask);
	//ccdStageRunner.stop(ccdStageTask);
	//ccdAcquisitionRunner.stop(ccdAcquisitionTask);
	//ccdAcquireRunner.stop(ccdAcquireTask);
	//ccdCheckRunner.stop(ccdCheckTask);
	//ccdFocusImageRunner.stop(ccdFocusImageTask);
	//ccdFileRunner.stop(ccdFileTask);
	//ccdModeRunner.stop(ccdModeTask);
}

//Ext.onReady(startRunners);
