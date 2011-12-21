/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *     sampleImageCanvas.js
 *     
 */

//var width = 640;
//var height = 480;
//var baseScale = .4;

var siCanvas;
var siContext;

var siCoordTooltip;
var siCurrentScale = 0.0000001; /* almost zero */

var siLocationSpotRadius = 4.5;
var siLocationSpotLineWidth = 2.5;

var siScanRegionDefining = false;
var siScanRegionOffsetH = 0.0;
var siScanRegionOffsetV = 0.0;
var siScanRegionStartH = 0;
var siScanRegionStartV = 0;
var siScanRegionEndH = 0;
var siScanRegionEndV = 0;

var siUpdateScanPending = false;
var siUpdateScanTimestamp = 0;
var siUpdateLocationTask = {};
var siUpdateImageTimestamp = 0;
var siUpdateImageQueryTask = {};

function initSampleImageCanvas(width, height, parent) {
	
	siCanvas = document.createElement('canvas');
	siCanvas.height = height;
	siCanvas.width = width;
	
	siContext = siCanvas.getContext('2d');
	
	siCoordTooltip = new Ext.ToolTip({
		target: siCanvas,
		title: 'x, y',
		plain: true,
		showDelay: 0,
		hideDelay: 0,
		dismissDelay: 0,
		trackMouse: true
	});
	
	siCanvas.ondblclick = function(event) {
		event = event ? event : window.event;
		var position = sampleImageRelativePosition(event);
		hSetPointFld.setValue(position.left);
		vSetPointFld.setValue(position.top);
		moveHVStage();
	};
	
	siCanvas.onmousedown = function(event) {
		event = event ? event : window.event;
		if(!acquiringScanData && event.shiftKey) {
			siScanRegionDefining = true;
			var position = sampleImageRelativePosition(event);
			siScanRegionEndH = siScanRegionStartH = position.left;
			siScanRegionEndV = siScanRegionStartV = position.top;
			sampleImageDraw();
		}
	};
	
	siCanvas.onmousemove = function (event) {
		event = event ? event : window.event;
		var position = sampleImageRelativePosition(event);
		var left = position.left;
		var top = position.top;
		
		var x = event.clientX;
		x += 12;
						
		var y = event.clientY;
		y += 12;
		
		siCoordTooltip.setTitle('(' + left + ',' + top + ')');
		siCoordTooltip.show();
		
		if(siScanRegionDefining) {
			siScanRegionEndH = left;
			siScanRegionEndV = top;
			sampleImageDraw();
		}
	};
	
	siCanvas.onmouseout = function onMouseOut() {
		siCoordTooltip.hide();
	};
	
	siCanvas.onmouseup = function (event) {
		event = event ? event : window.event;
		if(siScanRegionDefining) {
			var position = sampleImageRelativePosition(event);
			siScanRegionEndH = position.left;
			siScanRegionEndV = position.top;
			siScanRegionDefining = false;
			siUpdateScanPending = true;
			scanSetupFormSubmit();
			sampleImageDraw();
		}
	};
	
	if(parent && parent.appendChild) {
		parent.appendChild(siCanvas);
		
		siUpdateLocationTask = {
			run: sampleImageUpdateLocation,
			interval: heartbeatInterval
		};
		
		heartbeatTasksToStart.push(siUpdateLocationTask);
		
		siUpdateImageQueryTask = {
			run: sampleImageQuery,
			interval: heartbeatInterval * 2
		};
		
		Ext.TaskMgr.start(siUpdateImageQueryTask);
	}
}

function sampleImageRelativePosition(event) {
	
	var offsetTop = 0;
	var offsetLeft = 0;
	
	var parent = siCanvas;
	if(parent.offsetParent) {
		offsetTop = parent.offsetTop;
		offsetLeft = parent.offsetLeft;
		while(parent = parent.offsetParent) {
			offsetTop += parent.offsetTop;
			offsetLeft += parent.offsetLeft;
		}
	}
	
	var scrollOffsetTop = siCanvas.offsetParent.scrollTop;
	var scrollOffsetLeft = siCanvas.offsetParent.scrollLeft;
	
	var top = ((event.pageY - offsetTop) + scrollOffsetTop) - (siCanvas.height / 2);
	var left = ((event.pageX - offsetLeft) + scrollOffsetLeft) - (siCanvas.width / 2);
	
	top = top * siCurrentScale - siScanRegionOffsetV; 
	left = (left * siCurrentScale) + siScanRegionOffsetH;
	
	return { top: -round(top, 8), left: round(left, 8) }; 	
}

function sampleImageDrawLocation() {
	
	var h = parseFloat(hPositionFld.getValue());
	var v = parseFloat(vPositionFld.getValue());
	if(!isNaN(h) && !isNaN(v)) {
		siContext.beginPath();
		siContext.arc(h, v, siLocationSpotRadius * siCurrentScale, 0, Math.PI * 2, false);
		siContext.strokeStyle = 'rgba(255, 255, 255, 0.50)';
		siContext.lineWidth = siLocationSpotLineWidth * siCurrentScale;
		siContext.fillStyle = 'rgba(0,255,0, 0.65)';
		siContext.stroke();
		siContext.fill();
	}
}

function sampleImageDrawScanRegion() {
	
	var stepSize;
	
	var startPositionH;
	var startPositionV;
	
	var endPositionH;
	var endPositionV;
	
	var fillColor;
	var strokeColor;
	
	if(siScanRegionDefining || siUpdateScanPending) {
		
		stepSize = parseFloat(stepSizeFld.getValue());	
		if(isNaN(stepSize) || (stepSize <= 0.0)) {
			stepSize = parseFloat(stepSizeFbkFld.getValue());
			if(isNaN(stepSize) || (stepSize <= 0.0)) {
				Ext.Msg.alert('Warning', 'A valid Step Size, greater than zero, must be specified.');
				siScanRegionDefining = false;
				return;
			}
		}
			
		// Compute scan region rounded to nearest step. //
		startPositionH = siScanRegionStartH;
		startPositionV = siScanRegionStartV;
		
		var nStepsH = Math.round((siScanRegionEndH - siScanRegionStartH) / stepSize);
		var nStepsV = Math.round((siScanRegionEndV - siScanRegionStartV) / stepSize);
				
		endPositionH = convertToFixed6(startPositionH + (stepSize * nStepsH));
		endPositionV = convertToFixed6(startPositionV + (stepSize * nStepsV));
		
		// Fill scan setup fields. //
		if(startPositionH <= endPositionH) {
			startPositionHFld.setValue(startPositionH);
			endPositionHFld.setValue(endPositionH);
		} else {
			startPositionHFld.setValue(endPositionH);
			endPositionHFld.setValue(startPositionH);
		}
		
		if(startPositionV <= endPositionV) {
			startPositionVFld.setValue(startPositionV);
			endPositionVFld.setValue(endPositionV);
		} else {
			startPositionVFld.setValue(endPositionV);
			endPositionVFld.setValue(startPositionV);
		}
		
		nPointsHFld.setValue(Math.abs(nStepsH) + 1);
		nPointsVFld.setValue(Math.abs(nStepsV) + 1);

		stepSizeFld.setValue(stepSize);
		
		// Set the scan region color. //
		fillColor = 'rgba(255, 255, 0, 0.0)';
		strokeColor = 'rgba(255, 255, 0, 0.80)';
	}
	else {
		
		stepSize = parseFloat(stepSizeFbkFld.getValue());
		startPositionH = parseFloat(startPositionHFbkFld.getValue());
		startPositionV = parseFloat(startPositionVFbkFld.getValue());
		endPositionH = parseFloat(endPositionHFbkFld.getValue());
		endPositionV = parseFloat(endPositionVFbkFld.getValue());
		
		fillColor = 'rgba(255, 0, 0, 0.0)';
		strokeColor = 'rgba(255, 0, 0, 0.80)';
	}	
		
	if(!(isNaN(stepSize) || isNaN(startPositionH) || isNaN(startPositionV) || isNaN(endPositionH) || isNaN(endPositionV))) {
		
		var sizeH = endPositionH - startPositionH;
		var sizeV = endPositionV - startPositionV;
		
		var boxSizeH;
		var boxStartH;
		if(sizeH >= 0.0) {
			boxSizeH = sizeH + stepSize;
			boxStartH = startPositionH - (0.5 * stepSize);
		}
		else {
			boxSizeH = sizeH - stepSize;
			boxStartH = startPositionH + (0.5 * stepSize);
		}
		
		var boxSizeV;
		var boxStartV;
		if(sizeV >= 0.0) {
			boxSizeV = sizeV + stepSize;
			boxStartV = startPositionV - (0.5 * stepSize);
		}
		else {
			boxSizeV = sizeV - stepSize;
			boxStartV = startPositionV + (0.5 * stepSize);
		}
		
		siContext.beginPath();
		siContext.rect(boxStartH, boxStartV, boxSizeH, boxSizeV);
		siContext.strokeStyle = strokeColor;
		siContext.lineWidth = siLocationSpotLineWidth * siCurrentScale;
		siContext.fillStyle = fillColor;
		siContext.stroke();
		siContext.fill();
	}
}

function sampleImageDraw() {	
	siContext.clearRect(0, 0, siCanvas.width, siCanvas.height);
	if (siCanvas.img && siCanvas.img.complete) {
		siContext.drawImage(siCanvas.img, 0, 0);
	}
	siContext.strokeStyle = "black";
	siContext.strokeRect(0, 0, siCanvas.width, siCanvas.height);
	
	siContext.save();
	siContext.translate(siCanvas.width / 2, siCanvas.height / 2);
	siContext.scale((1.0 / siCurrentScale), -(1.0 / siCurrentScale));
	siContext.translate(-1.0 * siScanRegionOffsetH, -1.0 * siScanRegionOffsetV);
		
	sampleImageDrawScanRegion();
	sampleImageDrawLocation();
					
	siContext.restore();
}

function sampleImageUpdate() {
	var h = parseFloat(hPositionFld.getValue());
	var v = parseFloat(vPositionFld.getValue());
	if(h && v) {
		siScanRegionOffsetH = h * 1.0;
		siScanRegionOffsetV = v * 1.0;
	} else {
		siScanRegionOffsetH = 0.0;
		siScanRegionOffsetV = 0.0;
	}
	
	siCanvas.img = new Image();
	siCanvas.img.src = 'sample/camera/image.png?_dc=' + siUpdateImageTimestamp;
	siCanvas.img.onload = sampleImageDraw;
};

function sampleImageQuery() {
	Ext.Ajax.request({
		url: 'sample/camera/query.json',
		success: sampleImageQuerySuccess,
		params: {
			'timestamp': siUpdateImageTimestamp
		}
	});
}

function sampleImageQuerySuccess(response, options) {
	var json = Ext.decode(response.responseText, true);
	if(json && json.timestamp) {
		var timestamp = json.timestamp;
		if(timestamp > siUpdateImageTimestamp) {
			siUpdateImageTimestamp = timestamp;
			sampleImageUpdate();
		}
	}
}

function sampleImageUpdateLocation() {
	var scanDeviceTimestamp = 0;
	if(heartbeatData.scanDevice && heartbeatData.scanDevice.timestamp) {
		scanDeviceTimestamp = heartbeatData.scanDevice.timestamp;
	}
	
	if(siUpdateScanTimestamp < scanDeviceTimestamp) {
		siUpdateScanTimestamp = scanDeviceTimestamp;
		if(siUpdateScanPending) {
			siUpdateScanPending = false;
			scanSetupLoad();
		}
	}
	
	sampleImageDraw();
}
