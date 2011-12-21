/**
 * Copyright (c) Canadian Light Source, Inc. All rights reserved. - see
 * license.txt for details.
 *
 * Description: ccdDetectorPanels.js
 *
 */



/**
 *******************************************
 * CCD Mode Panel
 *******************************************
 */
var oldCcdMode = "init";

defaultFieldWidth = 50;
defaultLabelWidth = 80;


var ccdMode = new Ext.form.TextField({
	name : 'xrdMode',
	fieldLabel: 'Mode',
	width : defaultFieldWidth,
    readOnly : true,
    cls : 'ss-form-field-label',
    value : 'init'
});

var switchToFocusModeBtn = new Ext.Button( {
    text : 'Focus'
});

switchToFocusModeBtn.on('click', function() {
	if (ccdMode.getValue() != 'focus')
		switchCcdMode('focus');
});

var switchToScanModeBtn = new Ext.Button( {
	text : 'Scan'
});

switchToScanModeBtn.on('click', function() {
	if (ccdMode.getValue() != 'scan')
		switchCcdMode('scan');
});

function switchCcdMode(target) {
	Ext.Ajax.request({
				url : 'ccdmode.json',
				method : 'POST',
				async : false,
				disableCaching : false,
				params : {
					'mode' : target
				},
				success : function(response) {
					ccdMode.setValue('switching to ' + target);
				},
				failuer : function(response) {
					ccdMode.setValue('switch failed');
				}
			});

}


var ccdModeForm = new Ext.FormPanel( {
    border : false,
    labelPad: 5,
	labelWidth: defaultLabelWidth,
    layout : 'table',
    layoutConfig : {
        columns : 2
    },
    defaults: {
		layout: 'form',
		border: false,
		bodyStyle: 'padding: 2px'
    },
	items: [{colspan: 2,
		items:[ccdMode]
    }],
    buttons : {formBind: true, items: [ switchToFocusModeBtn, switchToScanModeBtn ]} ,
    buttonAlign : 'left'
});


var ccdModeLoad = function() {
    if (heartbeatData && heartbeatData.beamlineSession && heartbeatData.beamlineSession.xrdMode) {
        ccdModeForm.getForm().setValues(heartbeatData.beamlineSession);
        var currentMode = ccdMode.getValue();
        if (currentMode !== oldCcdMode) {
        	oldCcdMode = currentMode;
        	if (currentMode === 'focus') {
        		ccdAcquireForm.enable();
        		ccdAcquirePanel.expand(true);
        		ccdFilePanel.collapse(true);
        		ccdFileForm.disable();
        		ccdCheckPanel.collapse(true);
        		ccdCheckForm.disable();

        	} else if (currentMode === 'scan') {
        		ccdAcquirePanel.collapse(true);
        		ccdAcquireForm.disable();
        		ccdFileForm.enable();
        		ccdFilePanel.expand(true);
        		ccdCheckForm.enable();
        		ccdCheckPanel.expand(true);
        	}
        }
	}
};

var ccdModeTask = {
		run: ccdModeLoad,
		interval: heartbeatInterval * 2
	};

xrdTasksToStart.push(ccdModeTask);

var ccdModePanel = new Ext.Panel( {
    title : 'CCD Mode',
    width : 'auto',
    collapsible : true,
    items : [ ccdModeForm ]
});


/** ************************ CCD Setup Panel ************************* */

var ccdTempRBVFld = new Ext.form.NumberField( {
    name : 'temperatureRBV',
    fieldLabel: 'Temperature',
    width : defaultFieldWidth,
    readOnly : true,
    cls : 'ss-form-field-label'
});

var ccdImageSizeRBVFld = new Ext.form.NumberField( {
    name : 'imageSizeRBV',
    fieldLabel: 'Image size',
    width : defaultFieldWidth,
    readOnly : true,
    allowDecimals: false,
    cls : 'ss-form-field-label'
});

var ccdBinXFld = new Ext.form.NumberField( {
    name : 'setbinX',
    fieldLabel: 'X binning',
    width : defaultFieldWidth,
    minValue : 1,
    allowDecimals: false
});

var ccdBinYFld = new Ext.form.NumberField( {
    name : 'setbinY',
    width : defaultFieldWidth,
    fieldLabel: 'Y binning',
    value : 'N/A',
    minValue : 1,
    allowDecimals: false

});

var ccdBinXRBVFld = new Ext.form.NumberField( {
    name : 'binXRBV',
    hideLabel: true,
    width : defaultFieldWidth,
    readOnly : true,
    cls : 'ss-form-field-readonly'
});

var ccdBinYRBVFld = new Ext.form.NumberField( {
    name : 'binYRBV',
    hideLabel: true,
    width : defaultFieldWidth,
    readOnly : true,
    cls : 'ss-form-field-readonly'
});

var ccdRoiStartXFld = new Ext.form.NumberField( {
    name : 'regionStartX',
    fieldLabel: 'ROI X start',
    width : defaultFieldWidth,
    allowDecimals: false,
    minValue : 0
});

/*var ccdRoiStartXRBVFld = new Ext.form.NumberField( {
    name : 'ccdRoiStartXRBV',
    width : defaultFieldWidth,
    readOnly : true,
    cls : 'ss-form-field-readonly'
});*/

var ccdRoiStartYFld = new Ext.form.NumberField( {
    name : 'regionStartY',
    fieldLabel: 'ROI Y start',
    width : defaultFieldWidth,
    allowDecimals: false,
    minValue : 0
});

/*var ccdRoiStartYRBVFld = new Ext.form.NumberField( {
    name : 'ccdRoiStartYRBV',
    width : defaultFieldWidth,
    readOnly : true,
    cls : 'ss-form-field-readonly'
});*/

var ccdRoiSizeXFld = new Ext.form.NumberField( {
    name : 'setregionSizeX',
    fieldLabel: 'ROI X size',
    width : defaultFieldWidth,
    allowDecimals: false,
    minValue : 0
});

var ccdRoiSizeXRBVFld = new Ext.form.NumberField( {
    name : 'regionSizeXRBV',
    hideLabel: true,
    width : defaultFieldWidth,
    readOnly : true,
    cls : 'ss-form-field-readonly'
});

var ccdRoiSizeYFld = new Ext.form.NumberField( {
    name : 'setregionSizeY',
    fieldLabel: 'ROI Y size',
    width : defaultFieldWidth,
    allowDecimals: false,
    minValue : 0
});

var ccdRoiSizeYRBVFld = new Ext.form.NumberField( {
    name : 'regionSizeYRBV',
    hideLabel: true,
    width : defaultFieldWidth,
    readOnly : true,
    cls : 'ss-form-field-readonly'
});

var ccdSetupBtn = new Ext.Button( {
    text : 'set'
});

ccdSetupBtn.on('click', function() {
    ccdSetup();
    });

function ccdSetup() {
	var binX = ccdBinXFld.getValue();
	var binY = ccdBinYFld.getValue();
	var startX = ccdRoiStartXFld.getValue();
	var startY = ccdRoiStartYFld.getValue();
	var sizeX = ccdRoiSizeXFld.getValue();
	var sizeY = ccdRoiSizeYFld.getValue();

	Ext.Ajax.request({
				url : 'ccdsetup.json',
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
			});
}


var ccdSetupForm = new Ext.FormPanel( {
	border : false,
    labelPad: 5,
	labelWidth: defaultLabelWidth,
    layout : 'table',
    layoutConfig : {
        columns : 2
    },
    defaults: {
		layout: 'form',
		border: false,
		bodyStyle: 'padding: 2px'
    },
	items: [{colspan: 2,
		items:[ccdTempRBVFld]},
		{colspan: 2,
			items:[ccdImageSizeRBVFld]},
		{items:[ccdBinXFld]}, 
		{items:[ccdBinXRBVFld]}, 
		{items:[ccdBinYFld]}, 
		{items:[ccdBinYRBVFld]}, 
		{colspan: 2,
			items:[ccdRoiStartXFld]}, 
		{colspan: 2,
				items:[ccdRoiStartYFld]}, 
		{items:[ccdRoiSizeXFld]}, 
		{items:[ccdRoiSizeXRBVFld]},
		{items:[ccdRoiSizeYFld]}, 
		{items:[ccdRoiSizeYRBVFld]},
		],
    buttons : [ ccdSetupBtn ],
    buttonAlign : 'left'
});

var ccdSetupPanel = new Ext.Panel( {
    title : 'CCD Setup',
    width : 'auto',
    collapsible : true,
    items : [ ccdSetupForm ]
});

var ccdSetupLoad = function() {
    if (heartbeatData && heartbeatData.ccdSetup) {
        ccdSetupForm.getForm().setValues(heartbeatData.ccdSetup);
    }
};

var ccdSetupTask = {
		run: ccdSetupLoad,
		interval: heartbeatInterval * 3
	};

xrdTasksToStart.push(ccdSetupTask);



/** ************************ CCD Stage Panel ************************* */
var ccdStageFld = new Ext.form.NumberField( {
//    id : 'ccdPositionZNF',
    name : 'positionZ',
    fieldLabel: 'Position Z',
    width : defaultFieldWidth,
    readOnly : true,
    cls : 'ss-form-field-readonly'
});

var ccdStageForm = new Ext.FormPanel( {
	border : false,
    labelPad: 5,
	labelWidth: defaultLabelWidth,
    layout : 'table',
    layoutConfig : {
        columns : 1
    },
    defaults: {
		layout: 'form',
		border: false,
		bodyStyle: 'padding: 2px'
    },
    items : [{
    	items:[ccdStageFld]
    }]
});

var ccdStagePanel = new Ext.Panel( {
    title : 'CCD Stage Position (mm)',
    width : 'auto',
    collapsible : true,
    items : [ ccdStageForm ]
});

var ccdStageLoad = function() {
    if (heartbeatData && heartbeatData.ccdStage) {
        ccdStageForm.getForm().setValues(heartbeatData.ccdStage);
    }
};

var ccdStageTask = {
		run: ccdStageLoad,
		interval: heartbeatInterval * 3
	};

xrdTasksToStart.push(ccdStageTask);

/** ************************ CCD Acquisition Panel ************************* */

// number of exposures per image
var ccdNELbl = new Ext.form.TextField( {
    name : 'numExposuresRBV',
    fieldLabel: 'Exposures per image',
    width : defaultFieldWidth,
    readOnly : true,
    cls : 'ss-form-field-label',
    value : 'N/A'
});

// exposure counter
var ccdECLbl = new Ext.form.TextField( {
    name : 'exposureCounter',
    fieldLabel: 'Exposures completed',
    width : defaultFieldWidth,
    readOnly : true,
    cls : 'ss-form-field-label',
    value : 'N/A'
});

// number of images per acquisition
var ccdNILbl = new Ext.form.TextField( {
    name : 'numImagesRBV',
    fieldLabel: 'Images per acquistion',
    width : defaultFieldWidth,
    readOnly : true,
    cls : 'ss-form-field-label',
    value : 'N/A'
});

// image counter
var ccdICLbl = new Ext.form.TextField( {
    name : 'imageCounter',
    fieldLabel: 'Images completed',
    width : defaultFieldWidth,
    readOnly : true,
    cls : 'ss-form-field-label',
    value : 'N/A'
});

// number of acquisition to do
var ccdNALbl = new Ext.form.TextField( {
    name : 'numAcquisitionsRBV',
    fieldLabel: 'Acquisitions to do',
    width : defaultFieldWidth,
    readOnly : true,
    cls : 'ss-form-field-label',
    value : 'N/A'
});

// acquisitition counter
var ccdACLbl = new Ext.form.TextField( {
    name : 'acquisitionCounter',
    fieldLabel: 'Acquisitions completed',
    width : defaultFieldWidth,
    readOnly : true,
    cls : 'ss-form-field-label',
    value : 'N/A'
});

// image mode
var ccdIMLbl = new Ext.form.TextField( {
    name : 'imageModeRBV',
    fieldLabel: 'Collection mode',
    width : 100,
    readOnly : true,
    cls : 'ss-form-field-label',
    value : 'N/A'
});

// trigger mode
var ccdTMLbl = new Ext.form.TextField( {
    name : 'triggerModeRBV',
    fieldLabel: 'Trigger mode',
    width : 100,
    readOnly : true,
    cls : 'ss-form-field-label',
    value : 'N/A'
});

// time remaining
var ccdTRLbl = new Ext.form.TextField( {
    name : 'timeRemainingRBV',
    fieldLabel: 'Remaining time (sec)',
    width : defaultFieldWidth,
    readOnly : true,
    cls : 'ss-form-field-label',
    value : 'N/A'
});

// exposure time
var ccdETFld = new Ext.form.NumberField( {
    name : 'setexposureTime',
    fieldLabel: 'Exposure time (sec)',
    width : defaultFieldWidth,
    minValue : 0,
    emptyText : 0.5
});

// exposure time RBV
var ccdETRBVFld = new Ext.form.NumberField( {
    name : 'exposureTimeRBV',
//    fieldLabel: 'Exposure time (sec)',
    hideLabel: true,
    width : defaultFieldWidth,
    readOnly : true,
    cls : 'ss-form-field-readonly'
});

var setCcdETBtn = new Ext.Button( {
    text : 'set'
});

setCcdETBtn.on('click', function() {
    setCcdExposureTime(ccdETFld.getValue());
    });

function setCcdExposureTime(time) {
	Ext.Ajax.request({
				url : 'ccdexposuretime.json',
				method : 'POST',
				async : false,
				disableCaching : false,
				params : {
					'exposureTime' : time
				}
			});
}

var ccdAcquisitionForm = new Ext.FormPanel( {
	border : false,
    labelPad: 5,
	labelWidth: 120,
    layout : 'table',
    layoutConfig : {
        columns : 2
    },
    defaults: {
		layout: 'form',
		border: false,
		bodyStyle: 'padding: 2px'
    },
    items : [ {colspan: 2, items: [ccdNELbl]},
              {colspan: 2, items: [ccdECLbl]},
              {colspan: 2, items: [ccdNILbl]},
              {colspan: 2, items: [ccdICLbl]},
              {colspan: 2, items: [ccdACLbl]},
              {colspan: 2, items: [ccdIMLbl]},
              {colspan: 2, items: [ccdTMLbl]},
              {colspan: 2, items: [ccdTRLbl]},
              {items: [ccdETFld]}, 
              {items: [ccdETRBVFld]}
              ], 
              buttons : [ setCcdETBtn ],
              buttonAlign : 'left'
});

var ccdAcquisitionPanel = new Ext.Panel( {
    title : 'Acquisition configuration',
    width : 'auto',
    collapsible : true,
    items : [ ccdAcquisitionForm ]
});

var ccdAcquisitionLoad = function() {
    if (heartbeatData && heartbeatData.ccdCollection) {
        ccdAcquisitionForm.getForm().setValues(heartbeatData.ccdCollection);
    }
};

var ccdAcquisitionTask = {
		run: ccdAcquisitionLoad,
		interval: heartbeatInterval * 3
	};

xrdTasksToStart.push(ccdAcquisitionTask);



/** ************************ CCD File Panel ************************* */

// For focus mode, maybe it is better not to show this panel. The users
// do not need to decide where the file should be saved. They can still,
// however, choose to download the images. The file will be saved at
// a default folder with machine-generated names.

var ccdFilePathRBV = new Ext.form.TextField( {
    name : 'filePathRBV',
    fieldLabel: 'File path',
    width : 200,
    readOnly : true,
    cls : 'ss-form-field-label',
    value : 'N/A'
});

var ccdFileName = new Ext.form.TextField( {
    name : 'setfileName',
    fieldLabel: 'Name prefix',
//    id : 'ccdFileName',
    width : 100
});

var ccdFileNameRBV = new Ext.form.TextField( {
    name : 'fileNameRBV',
//    id : 'ccdFileNameRBV',
    hideLabel: true,
    width : 100,
    readOnly : true,
    cls : 'ss-form-field-readonly',
    value : 'N/A'
});

var ccdFileNum = new Ext.form.NumberField( {
    name : 'setfileNumber',
    fieldLabel: 'Start number',
//    id : 'ccdFileNumber',
    width : defaultFieldWidth,
    minValue : 0,
	allowDecimals: false
});

var ccdFileNumRBV = new Ext.form.NumberField( {
    name : 'fileNumberRBV',
//    id : 'ccdFileNumberRBV',
    hideLabel: true,
    width : defaultFieldWidth,
    readOnly : true,
    cls : 'ss-form-field-readonly'
});


var ccdAutoIncRBV = new Ext.form.TextField( {
    name : 'autoIncrementRBV',
    fieldLabel: 'Auto increment',
    width : defaultFieldWidth,
    readOnly : true,
    cls : 'ss-form-field-label',
    value : 'N/A'
});


var ccdFileTempRBV = new Ext.form.TextField( {
    name : 'fileTemplateRBV',
    fieldLabel: 'File template',
    width : 200,
    readOnly : true,
    cls : 'ss-form-field-label',
    value : 'N/A'
});

var ccdFullNameRBV = new Ext.form.TextField( {
    name : 'fullfilenameRBV',
    fieldLabel: 'Full file name',
    width : 200,
    readOnly : true,
    cls : 'ss-form-field-label',
    value : 'N/A'
});

var ccdFileFormatRBV = new Ext.form.TextField( {
    name : 'fileFormatRBV',
    fieldLabel: 'File format',
    width : defaultFieldWidth,
    readOnly : true,
    cls : 'ss-form-field-label',
    value : 'N/A'
});

var setCcdFileBtn = new Ext.Button( {
	formBind: true,
    text : 'set'
});

setCcdFileBtn.on('click', function() {
	setCcdFile();
});

function setCcdFile(){
	var fileName = ccdFileName.getValue();
	var fileNumber = ccdFileNum.getValue();

	Ext.Ajax.request({
				url : 'ccdfile.json',
				method : 'POST',
				async : false,
				disableCaching : false,
				params : {
					'fileName' : fileName,
					'fileNumber' : fileNumber
				}
			});
}

var ccdFileForm = new Ext.FormPanel( {
	monitorValid: true,
	disabled: true,
	border : false,
    labelPad: 5,
	labelWidth: 100,
    layout : 'table',
    layoutConfig : {
        columns : 2
    },
    defaults: {
		layout: 'form',
		border: false,
		bodyStyle: 'padding: 2px'
    }, 
    items :[ {colspan: 2, items: [ccdFilePathRBV]},
             {colspan: 2, items: [ccdAutoIncRBV]},
             {colspan: 2, items: [ccdFileTempRBV]},
             {colspan: 2, items: [ccdFullNameRBV]},
             {colspan: 2, items: [ccdFileFormatRBV]},
             {items: [ccdFileName]},
             {items: [ccdFileNameRBV]},
             {items: [ccdFileNum]},
             {items: [ccdFileNumRBV]}
    ],
    buttons : [ setCcdFileBtn ],
    buttonAlign : 'left'
});

var ccdFilePanel = new Ext.Panel( {
    title : 'File configuration',
    width : 'auto',
    collapsible : true,
	collapsed : true,
    items : [ ccdFileForm ]
});

var ccdFileLoad = function() {
    if (heartbeatData && heartbeatData.ccdFile) {
        ccdFileForm.getForm().setValues(heartbeatData.ccdFile);
    }
};

var ccdFileTask = {
	run : ccdFileLoad,
	interval : heartbeatInterval * 3
};

xrdTasksToStart.push(ccdFileTask);


/** ************************ CCD Acquire Panel ************************* */

var ccdAquireLbl = new Ext.form.TextField({
	name : 'acquire',
	fieldLabel: 'Acquiring',
	width : 100,
	readOnly : true,
	cls : 'ss-form-field-label'
});

var ccdFocusStartBtn = new Ext.Button( {
	formBind: true,
    text : 'Focus'
});

ccdFocusStartBtn.on('click', function() {
    ccdFocusStopBtn.enable();
    ccdFocus('start');
});

var ccdFocusStopBtn = new Ext.Button( {
	formBind: true,
    text : 'Stop',
    disabled : true
});

ccdFocusStopBtn.on('click', function() {
    ccdFocusStartBtn.enable();
    ccdFocus('stop');
});

function ccdFocus(action) {
	Ext.Ajax.request({
				url : 'ccdfocus.json',
				method : 'POST',
				async : false,
				disableCaching : false,
				params : {
					'acquire' : action
				}
			});
}

var saveSPEBtn = new Ext.Button( {
	formBind: true,
    text : 'Save SPE'
});

saveSPEBtn.on('click', function() {
    if (heartbeatData.ccdFile.fileNameRBV) {
        saveFocusImage(heartbeatData.ccdFile.fileNameRBV, 'spe');
    }
});

var savePNGBtn = new Ext.Button( {
	formBind: true,
    text : 'Save PNG'
});

savePNGBtn.on('click', function() {
	 if (heartbeatData.ccdFile.fileNameRBV) {
	        saveFocusImage(heartbeatData.ccdFile.fileNameRBV, 'png');
    }
});

function saveFocusImage(fileName, type) {
	window.location.href = 'ccdFocusImage?type=' + type
			+ '&file=' + fileName + '&download';
}


var ccdAcquireForm = new Ext.FormPanel( {
	monitorValid: true,
	disabled: true,
	border : false,
    labelPad: 5,
	labelWidth: 100,
    layout : 'table',
    layoutConfig : {
        columns : 2
    },
    defaults: {
		layout: 'form',
		border: false,
		bodyStyle: 'padding: 2px'
    }, 
    items : [{colspan: 2, items: [ccdAquireLbl]},
             {items: [ccdFocusStartBtn]},
             {items: [ccdFocusStopBtn]},
             {items: [saveSPEBtn]},
             {items: [savePNGBtn]}
    ]
});

var ccdAcquirePanel = new Ext.Panel( {
    title : 'Focus',
    width : 'auto',
    collapsible : true,
    collapsed: true,
    items : [ ccdAcquireForm ]
});

var ccdAcquireLoad = function() {
    if (heartbeatData && heartbeatData.ccdCollection) {
        ccdAcquireForm.getForm().setValues(heartbeatData.ccdCollection);

    }
};

var ccdAcquireTask = {
		run: ccdAcquireLoad,
		interval: heartbeatInterval * 3
	};

xrdTasksToStart.push(ccdAcquireTask);

/** ************************ Check latest scan image Panel ************************* */


var ccdChecktBtn = new Ext.Button( {
	formBind: true,
    text : 'Check image'
});

ccdChecktBtn.on('click', function() {
    ccdCheck();
});

function ccdCheck(){
	if (heartbeatData.beamlineSession && heartbeatData.ccdFile && heartbeatData.ccdCollection) {
		var fileName = heartbeatData.ccdFile.fileNameRBV;
		var fileNumber = heartbeatData.ccdFile.fileNumberRBV;
		var requestNumber = imageNumber.getValue();
		if (requestNumber >= fileNumber || requestNumber <= 0) {
			Ext.Msg.alert('Warning','the image is not available');
			return;
		}
		ccdImagePanel.getComponent('ccdImg').fill();
		ccdImagePanel.getComponent('ccdImg').mark(fileName+'_'+requestNumber);
		ccdImagePanel.getComponent('ccdImg').setImageURL('ccdImage?type=png&file='
					+ fileName + '&number=' + requestNumber);
	}
}

var saveSPEBtn_scan = new Ext.Button( {
	formBind: true,
    text : 'Save SPE'
});


saveSPEBtn_scan.on('click', function() {
	if (heartbeatData.ccdFile.fileNameRBV) {
        saveCCDImage(heartbeatData.ccdFile.fileNameRBV, 'spe');
    }
});

var savePNGBtn_scan = new Ext.Button( {
	formBind: true,
    text : 'Save PNG'
});

savePNGBtn_scan.on('click', function() {
	if (heartbeatData.ccdFile.fileNameRBV) {
        saveCCDImage(heartbeatData.ccdFile.fileNameRBV, 'png');
    }
});

function saveCCDImage(fileName, type) {
	var fileNumber = heartbeatData.ccdFile.fileNumberRBV;
	var requestNumber = imageNumber.getValue();
	if (requestNumber >= fileNumber || requestNumber <= 0) {
		Ext.Msg.alert('Warning','the image is not available');
		return;
	}
	window.location.href = 'ccdImage?type=' + type
			+ '&file=' + fileName + '&number=' + requestNumber + '&download';
}


var ccdAquireStateLbl = new Ext.form.TextField({
			name : 'detectorStateRBV', 
			fieldLabel: 'CCD state',
			width : defaultFieldWidth,
			readOnly : true, 
			cls : 'ss-form-field-label'
		});

var currentImageNumber = new Ext.form.NumberField( {
    name : 'fileNumberRBV',
    fieldLabel: 'Max number',
    width : defaultFieldWidth,
    cls : 'ss-form-field-readonly',
    readOnly: true
});

var imageNumber = new Ext.form.NumberField( {
    name : 'imageNumber',
    fieldLabel: 'Check number',
    width : defaultFieldWidth,
    minValue : 1,
    allowDecimals: false
});

var ccdCheckForm = new Ext.FormPanel( {
	monitorValid: true,
	disabled: true,
	border : false,
    labelPad: 5,
	labelWidth: 100,
    layout : 'table',
    layoutConfig : {
        columns : 2
    },
    defaults: {
		layout: 'form',
		border: false,
		bodyStyle: 'padding: 2px'
    }, 
    items : [{colspan: 2, items: [ccdAquireStateLbl]},
             {colspan: 2, items: [currentImageNumber]},
             {colspan: 2, items: [imageNumber]},
    		 {colspan: 2, items: [ccdChecktBtn]},
    		 {items: [saveSPEBtn_scan]},
    		 {items: [savePNGBtn_scan]}
    ]
});

var ccdCheckPanel = new Ext.Panel( {
    title : 'Check image',
    width : 'auto',
    collapsible : true,
    collapsed: true,
    items : [ ccdCheckForm ]
});

var ccdCheckLoad = function() {
    if (heartbeatData && heartbeatData.ccdCollection) {
        ccdCheckForm.getForm().setValues(heartbeatData.ccdCollection);
        ccdCheckForm.getForm().setValues(heartbeatData.ccdFile);
    }
};

var ccdCheckTask = {
		run: ccdCheckLoad,
		interval: heartbeatInterval * 3
	};

xrdTasksToStart.push(ccdCheckTask);


/************************** CCD Layout **************************/

/**
 * Detector configuration panel
 */
var ccdConfigPanel = new Ext.Panel( {
    region : 'west',
    split : true,
    collapsible : true,
    autoScroll : true,
    width : 'auto',
    title : 'CCD Detector configuration',
    layout : 'table',
    defaults : {
        bodyStyle : 'padding: 2px'
    },
    layoutConfig : {
        columns : 1
    },
    items : [ ccdModePanel, ccdSetupPanel, ccdStagePanel
    ]
});

/**
 * Image panel
 */
var ccdImagePanel = new Ext.Panel( {
    region : 'center',
    title : 'CCD Image',
    autoScroll : true,
    width : 'auto'
    ,items : new Ext.ux.CCDImageCanvas( {
        id : 'ccdImg'
    })
});

var ccdFocusImageLoad = function(){
	if (heartbeatData.beamlineSession.xrdMode && heartbeatData.ccdFile && heartbeatData.ccdCollection) {
		var mode = heartbeatData.beamlineSession.xrdMode;
		var fileName = heartbeatData.ccdFile.fileNameRBV;
		var fullFileName = heartbeatData.ccdFile.fullfilenameRBV;
		var acquire = heartbeatData.ccdCollection.acquire;
		if (mode == 'focus') {
			ccdImagePanel.getComponent('ccdImg').setText(acquire);
			if ((acquire == 'Done') && (fullFileName.indexOf(fileName) != -1)) {
			ccdImagePanel.getComponent('ccdImg').setImageURL('ccdFocusImage?type=png&file='
					+ fileName);
			}
		}
	}
};

var ccdFocusImageTask = {
		run: ccdFocusImageLoad,
		interval: heartbeatInterval * 2
	};

xrdTasksToStart.push(ccdFocusImageTask);


/**
 * Collection panel
 */
var ccdCollectionPanel = new Ext.Panel( {
    region : 'east',
    split : true,
    collapsible : true,
    autoScroll : true,
    width : 'auto',
    title : 'CCD Collection configuration',
    layout : 'table',
    defaults : {
        bodyStyle : 'padding: 2px'
    },
    layoutConfig : {
        columns : 1
    },
    items : [ ccdAcquisitionPanel, ccdFilePanel, ccdAcquirePanel, ccdCheckPanel ]
});


/*the whole thing*/

var xrdPanel = new Ext.Panel({
	id : 'XRD_TAB',
	title : 'XRD',
	closable : false,
	autoScroll : true,
	disabled: true,
	layout : 'border',
	items : [ccdConfigPanel, ccdImagePanel, ccdCollectionPanel]
});