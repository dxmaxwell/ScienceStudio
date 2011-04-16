/**
 * Copyright (c) Canadian Light Source, Inc. All rights reserved. - see
 * license.txt for details.
 *
 * Description: deviceFunctions.js
 *
 */

/**
 *
 * @include "include.js"
 */

var plotScale = 'linear';
var livePlot;


/**
 *************************** Vortex Detector Functions **************************

	Vortex not supported in initial production release.

function getVortexDataCSV() {
	Ext.Ajax.request({
		url : beamlineBaseURL + '/getVortexDetector.xml?info=data',
		method : 'GET',
		async : true,
		disableCaching : false,
		success : function(response) {
			var xmlDoc = response.responseXML.documentElement;
			var node = xmlDoc.getElementsByTagName('vortexDetector')[0];
			var maxEnergy = 0;
			var specDiv = Ext.getDom('SPECTRA_DIV');
			var data = [];
			maxEnergy = parseFloat((node.getElementsByTagName('maxEnergy')[0]).textContent);
			var spectrum = node.getElementsByTagName('spectrum')[0];
			var size = parseInt(spectrum.getAttribute('size'));
			if (size != 0) {
				var sizeBin = maxEnergy / size;
				var x0 = sizeBin / 2;
				if (maxEnergy == 0) {
					sizeBin = 1;
					x0 = 1;
				}
				var temp = spectrum.getElementsByTagName('data');
				var value;
				var yLabel;
				if (plotScale == 'log') {
					yLabel = 'Count(log)';
					for (var i = 0; i < size; i++) {
						value = parseInt(temp[i].textContent);
						if (value > 0)
							data.push([x0 + i * sizeBin, log10(value)]);
						else
							data.push([x0 + i * sizeBin, 0]);
					}
				} else {
					yLabel = 'Count';
					for (var i = 0; i < size; i++) {
						value = parseInt(temp[i].textContent);
						data.push([x0 + i * sizeBin, value]);
					}
				}

				if (livePlot == null) {
					livePlot = new Dygraph(specDiv, data, {
						drawPoints: true,
						labels: ['Energy', yLabel]
					});
				} else {
					livePlot.updateOptions({'file': data,
						'labels': ['Energy', yLabel]
					});
				}
			}
		}
	});
}

function doAcquireAction(action) {
	Ext.Ajax.request({
				url : beamlineBaseURL + '/startStopVortexDetector.xml',
				method : 'POST',
				async : false,
				disableCaching : false,
				params : {
					'action' : action
				}
			});
}

function saveVortexData(type) {
	window.location.href = beamlineBaseURL + '/saveVortexDetectorData.do?type='
			+ type;
}

function setVortexSetupInfo() {
	var presetTimeSP = Ext.getCmp('vtxPresetTimeSP').getValue();
	var maxEnergySP = Ext.getCmp('vtxMaxEnergySP').getValue();

	Ext.Ajax.request({
				url : beamlineBaseURL + '/updateVortexDetector.xml',
				method : 'POST',
				async : false,
				disableCaching : false,
				params : {
					'presetTime' : presetTimeSP,
					'maxEnergySP' : maxEnergySP
				}
			});
}
*/

function ccdSetup() {
	var binX = Ext.getCmp('ccdBinX').getValue();
	var binY = Ext.getCmp('ccdBinY').getValue();
	var startX = Ext.getCmp('ccdRoiStartX').getValue();
	var startY = Ext.getCmp('ccdRoiStartY').getValue();
	var sizeX = Ext.getCmp('ccdRoiSizeX').getValue();
	var sizeY = Ext.getCmp('ccdRoiSizeY').getValue();

	Ext.Ajax.request({
				url : beamlineBaseURL + '/ccdSetup',
				method : 'POST',
				async : false,
				disableCaching : false,
				params : {
					'binX' : binX,
					'binY' : binY,
					'startX' : startX,
					'startY' : startY,
					'sizeX' : sizeX,
					'sizeY' : sizeY
				}
				// TODO if failed then inform the contoller?
			});
}


function setCcdFile(){
	var fileName = Ext.getCmp('ccdFileName').getValue();
	var fileNumber = Ext.getCmp('ccdFileNumber').getValue();

	Ext.Ajax.request({
				url : beamlineBaseURL + '/ccdFile',
				method : 'POST',
				async : false,
				disableCaching : false,
				params : {
					'fileName' : fileName,
					'fileNumber' : fileNumber
				}
			});
}


function setCcdExposureTime(time) {
	Ext.Ajax.request({
				url : beamlineBaseURL + '/ccdExposureTime',
				method : 'POST',
				async : false,
				disableCaching : false,
				params : {
					'exposureTime' : time
				}
			});
}

function ccdFocus(action) {
	Ext.Ajax.request({
				url : beamlineBaseURL + '/ccdFocus',
				method : 'POST',
				async : false,
				disableCaching : false,
				params : {
					'acquire' : action
				}
			});
}

function ccdCheck(){
	// fetch the latest image and draw it in the canvas
	if (beamlineHeartbeatStore.getCount() > 0) {
		var record = beamlineHeartbeatStore.getAt(0);
		var fileName = record.get('ccdFileNameRBV');
		var fullFileName = record.get('ccdFullfilenameRBV');
		// TODO check if the number is increased during scan
		var fileNumber = record.get('ccdFileNumberRBV');
		var acquire = record.get('ccdAcquire');
		ccdImagePanel.getComponent('ccdImg').setText(acquire);
		if ((fullFileName.indexOf(fileName) != -1)) {
			if (acquire == 'Done'){
				ccdImagePanel.getComponent('ccdImg').setImageURL(beamlineBaseURL + '/ccdImage?type=png&file='
					+ fileName + '&number=' + fileNumber);
				displayImageNumber = fileNumber;
			}
			else {
				ccdImagePanel.getComponent('ccdImg').setImageURL(beamlineBaseURL + '/ccdImage?type=png&file='
					+ fileName + '&number=' + (fileNumber-1));
				displayImageNumber = fileNumber - 1;
			}

		}
	}
}

function saveFocusImage(fileName, type) {
	window.location.href = beamlineBaseURL + '/ccdFocusImage?type=' + type
			+ '&file=' + fileName + '&download';
}

function saveCCDImage(fileName, type) {
	window.location.href = beamlineBaseURL + '/ccdImage?type=' + type
			+ '&file=' + fileName + '&number=' + displayImageNumber + '&download';
}

/*
 * function moveCcdStage() { var z = Ext.getCmp('ccdSetPointZ').getValue();
 *
 * Ext.Ajax.request({ url: beamlineBaseURL + '/updateCCDStage.xml',
 * method:'POST', async:false, disableCaching:false, params: { 'setPointZ': h }
 * }); }
 */
