/**
 * Copyright (c) Canadian Light Source, Inc. All rights reserved. - see
 * license.txt for details.
 *
 * Description: ccdDetectorPanels.js
 *
 */

/**
 *
 * @include "include.js"
 */

/**
 *******************************************
 * CCD Mode Pabel
 *******************************************
 */
var oldCcdMode = "init";
var displayImageNumber = 0;

fieldWidth = 100;

var ccdMode = new Ext.form.TextField({
	name : 'bsXrdMode',
	width : fieldWidth,
    readOnly : true,
    cls : 'ss-form-field-label',
    value : 'init'
});

var switchCcdModeBtn = new Ext.Button( {
    text : 'switch'
});

switchCcdModeBtn.on('click', function() {
	switchCcdModeBtn.setDisabled(true);
    switchCcdMode((ccdMode.getValue() === 'focus')?'scan':'focus');
});

function switchCcdMode(target) {
	// switch from the current to the opposite. the server knows the state transition
	Ext.Ajax.request({
				url : beamlineBaseURL + '/ccdMode',
				method : 'POST',
				async : false,
				disableCaching : false,
				params : {
					'mode' : target
				},
				success : function(response) {
					// hold on
					ccdMode.setValue('switching ...');
					switchCcdModeBtn.setDisabled(false);
				},
				failuer : function(response) {
					ccdMode.setValue('switch failed');
					switchCcdModeBtn.setDisabled(false);
				}

			});

}


var ccdModeForm = new Ext.FormPanel( {
    border : false,
    layout : 'table',
    defaults : {
        bodyStyle : 'padding: 2px'
    },
    layoutConfig : {
        columns : 2
    },
    items : [ {
        html : "Current mode: ",
        border : false
    }, ccdMode],
    buttons : [ switchCcdModeBtn ],
    buttonAlign : 'left',
    width : 'auto'
});

var ccdModeLoad = function() {
    if (beamlineHeartbeatStore.getCount() > 0) {
        var record = beamlineHeartbeatStore.getAt(0);
        ccdModeForm.getForm().loadRecord(record);
        var currentMode = ccdMode.getValue();
        if (currentMode !== oldCcdMode) {
        	oldCcdMode = currentMode;
        	ccdMode.setValue(currentMode);
        	if (currentMode === 'focus') {
        		switchCcdModeBtn.setText('switch to scan');
        		ccdAcquirePanel.enable();
        		ccdAcquirePanel.expand(true);
        		ccdFilePanel.collapse(true);
        		ccdFilePanel.disable();
        		ccdCheckPanel.collapse(true);
        		ccdCheckPanel.disable();

        	} else {
        		switchCcdModeBtn.setText('switch to focus');
        		ccdAcquirePanel.collapse(true);
        		ccdAcquirePanel.disable();
        		ccdFilePanel.enable();
        		ccdFilePanel.expand(true);
        		ccdCheckPanel.enable();
        		ccdCheckPanel.expand(true);
        	}
        }
	}
};

var ccdModePanel = new Ext.Panel( {
    title : 'CCD Mode',
    width : 'auto',
    collapsible : true,
    items : [ ccdModeForm ]
});


/** ************************ CCD Setup Panel ************************* */

var ccdTempRBVFld = new Ext.form.NumberField( {
    id : 'ccdTemperatureRBVNF',
    name : 'ccdTemperatureRBV',
    width : fieldWidth,
    readOnly : true,
    cls : 'ss-form-field-label'
});

var ccdImageSizeRBVFld = new Ext.form.NumberField( {
    id : 'ccdImageSizeRBV',
    name : 'ccdImageSizeRBV',
    width : fieldWidth,
    readOnly : true,
    allowDecimals: false,
    cls : 'ss-form-field-label'
});

var ccdBinXFld = new Ext.form.NumberField( {
    name : 'ccdBinX',
    id : 'ccdBinX',
    width : fieldWidth,
    minValue : 1,
    allowDecimals: false
});

var ccdBinYFld = new Ext.form.NumberField( {
    name : 'ccdBinY',
    id : 'ccdBinY',
    width : fieldWidth,
    value : 'N/A',
    minValue : 1,
    allowDecimals: false

});

var ccdBinXRBVFld = new Ext.form.NumberField( {
    name : 'ccdBinXRBV',
    width : fieldWidth,
    readOnly : true,
    cls : 'ss-form-field-readonly'
});

var ccdBinYRBVFld = new Ext.form.NumberField( {
    name : 'ccdBinYRBV',
    width : fieldWidth,
    readOnly : true,
    cls : 'ss-form-field-readonly'
});

var ccdRoiStartXFld = new Ext.form.NumberField( {
    name : 'ccdRoiStartX',
    id : 'ccdRoiStartX',
    width : fieldWidth,
    allowDecimals: false,
    minValue : 0
});

var ccdRoiStartXRBVFld = new Ext.form.NumberField( {
    name : 'ccdRoiStartXRBV',
    width : fieldWidth,
    readOnly : true,
    cls : 'ss-form-field-readonly'
});

var ccdRoiStartYFld = new Ext.form.NumberField( {
    name : 'ccdRoiStartY',
    id : 'ccdRoiStartY',
    width : fieldWidth,
    allowDecimals: false,
    minValue : 0
});

var ccdRoiStartYRBVFld = new Ext.form.NumberField( {
    name : 'ccdRoiStartYRBV',
    width : fieldWidth,
    readOnly : true,
    cls : 'ss-form-field-readonly'
});

var ccdRoiSizeXFld = new Ext.form.NumberField( {
    name : 'ccdRoiSizeX',
    id : 'ccdRoiSizeX',
    width : fieldWidth,
    allowDecimals: false,
    minValue : 0
});

var ccdRoiSizeXRBVFld = new Ext.form.NumberField( {
    name : 'ccdRoiSizeXRBV',
    width : fieldWidth,
    readOnly : true,
    cls : 'ss-form-field-readonly'
});

var ccdRoiSizeYFld = new Ext.form.NumberField( {
    name : 'ccdRoiSizeY',
    id : 'ccdRoiSizeY',
    width : fieldWidth,
    allowDecimals: false,
    minValue : 0
});

var ccdRoiSizeYRBVFld = new Ext.form.NumberField( {
    name : 'ccdRoiSizeYRBV',
    width : fieldWidth,
    readOnly : true,
    cls : 'ss-form-field-readonly'
});

var ccdSetupBtn = new Ext.Button( {
    text : 'set'
});

ccdSetupBtn.on('click', function() {
    ccdSetup();
    });

var ccdSetupForm = new Ext.FormPanel( {
    border : false,
    layout : 'table',
    defaults : {
        bodyStyle : 'padding: 2px'
    },
    layoutConfig : {
        columns : 3
    },
    items : [{
        html : "Temperature: ",
        border : false
    }, ccdTempRBVFld, {
        border : false,
        colspan : 1
    },
    {
        html : "Image size: ",
        border : false
    }, ccdImageSizeRBVFld, {
        border : false,
        colspan : 1
    },
    {html : "X binning: ", border : false}, ccdBinXFld, ccdBinXRBVFld,
	{html : "Y binning: ", border : false}, ccdBinYFld, ccdBinYRBVFld,
    {html : "X start: ", border : false}, ccdRoiStartXFld, ccdRoiStartXRBVFld,
	{html : "Y start: ", border : false}, ccdRoiStartYFld, ccdRoiStartYRBVFld,
	{html : "X size: ", border : false}, ccdRoiSizeXFld, ccdRoiSizeXRBVFld,
	{html : "Y size: ", border : false}, ccdRoiSizeYFld, ccdRoiSizeYRBVFld],
    buttons : [ ccdSetupBtn ],
    buttonAlign : 'left',
    width : 'auto'
});

var ccdSetupPanel = new Ext.Panel( {
    title : 'CCD Setup',
    width : 'auto',
    collapsible : true,
    items : [ ccdSetupForm ]
});

var ccdSetupLoad = function() {
    if (beamlineHeartbeatStore.getCount() > 0) {
        var record = beamlineHeartbeatStore.getAt(0);
        ccdSetupForm.getForm().loadRecord(record);
    }
};

/** ************************ CCD Stage Panel ************************* */
var ccdStageFld = new Ext.form.NumberField( {
    id : 'ccdPositionZNF',
    name : 'ccdPositionZ',
    width : fieldWidth,
    readOnly : true,
    cls : 'ss-form-field-readonly'
});

var ccdStageForm = new Ext.FormPanel( {
    border : false,
    layout : 'table',
    defaults : {
        bodyStyle : 'padding: 2px'
    },
    layoutConfig : {
        columns : 1
    },
    items : [ ccdStageFld ],
    width : 'auto'
});

var ccdStagePanel = new Ext.Panel( {
    title : 'CCD Stage Position (mm)',
    width : 'auto',
    collapsible : true,
    items : [ ccdStageForm ]
});

var ccdStageLoad = function() {
    if (beamlineHeartbeatStore.getCount() > 0) {
        var record = beamlineHeartbeatStore.getAt(0);
        ccdStageForm.getForm().loadRecord(record);
    }
};

/** ************************ CCD Acquisition Panel ************************* */

// number of exposures per image
var ccdNELbl = new Ext.form.TextField( {
    name : 'ccdNumExposuresRBV',
    width : fieldWidth,
    readOnly : true,
    cls : 'ss-form-field-label',
    value : 'N/A'
});

// exposure counter
var ccdECLbl = new Ext.form.TextField( {
    name : 'ccdExposureCounter',
    width : fieldWidth,
    readOnly : true,
    cls : 'ss-form-field-label',
    value : 'N/A'
});

// number of images per acquisition
var ccdNILbl = new Ext.form.TextField( {
    name : 'ccdNumImagesRBV',
    width : fieldWidth,
    readOnly : true,
    cls : 'ss-form-field-label',
    value : 'N/A'
});

// image counter
var ccdICLbl = new Ext.form.TextField( {
    name : 'ccdImageCounter',
    width : fieldWidth,
    readOnly : true,
    cls : 'ss-form-field-label',
    value : 'N/A'
});

// number of acquisition to do
var ccdNALbl = new Ext.form.TextField( {
    name : 'ccdNumAcquisitionsRBV',
    width : fieldWidth,
    readOnly : true,
    cls : 'ss-form-field-label',
    value : 'N/A'
});

// acquistition counter
var ccdACLbl = new Ext.form.TextField( {
    name : 'ccdAcquisitionCounter',
    width : fieldWidth,
    readOnly : true,
    cls : 'ss-form-field-label',
    value : 'N/A'
});

// image mode
var ccdIMLbl = new Ext.form.TextField( {
    name : 'ccdImageModeRBV',
    width : fieldWidth,
    readOnly : true,
    cls : 'ss-form-field-label',
    value : 'N/A'
});

// trigger mode
var ccdTMLbl = new Ext.form.TextField( {
    name : 'ccdTriggerModeRBV',
    width : fieldWidth,
    readOnly : true,
    cls : 'ss-form-field-label',
    value : 'N/A'
});

// time remaining
var ccdTRLbl = new Ext.form.TextField( {
    name : 'ccdTimeRemainingRBV',
    width : fieldWidth,
    readOnly : true,
    cls : 'ss-form-field-label',
    value : 'N/A'
});

// exposure time
var ccdETFld = new Ext.form.NumberField( {
    id : 'ccdExposureTimeNF',
    name : 'ccdExposureTime',
    width : fieldWidth,
    minValue : 0,
    emptyText : 0.5
});

// exposure time RBV
var ccdETRBVFld = new Ext.form.NumberField( {
    id : 'ccdExposureTimeRBVNF',
    name : 'ccdExposureTimeRBV',
    width : fieldWidth,
    readOnly : true,
    cls : 'ss-form-field-readonly'
});

var setCcdETBtn = new Ext.Button( {
    text : 'set'
});

setCcdETBtn.on('click', function() {
    setCcdExposureTime(Ext.getCmp('ccdExposureTimeNF').getValue());
    });

var ccdAcquisitionForm = new Ext.FormPanel( {
    border : false,
    layout : 'table',
    defaults : {
        bodyStyle : 'padding: 2px'
    },
    layoutConfig : {
        columns : 3
    },
    items : [ /*{
        html : "Acquire period: ",
        border : false
    }, {
        border : false,
        colspan : 1
    }, ccdAPRBVLbl,*/ {
        html : "Exposures per image: ",
        border : false
    }, {
        border : false,
        colspan : 1
    }, ccdNELbl, {
        html : "Exposures completed:  ",
        border : false
    }, {
        border : false,
        colspan : 1
    }, ccdECLbl, {
        html : "Images per acquisition: ",
        border : false
    }, {
        border : false,
        colspan : 1
    }, ccdNILbl, {
        html : "Images completed: ",
        border : false
    }, {
        border : false,
        colspan : 1
    }, ccdICLbl, {
        html : "Acquisitions to do: ",
        border : false
    }, {
        border : false,
        colspan : 1
    }, ccdNALbl, {
        html : "Acquisitions completed: ",
        border : false
    }, {
        border : false,
        colspan : 1
    }, ccdACLbl, {
        html : "Collection mode: ",
        border : false
    }, {
        border : false,
        colspan : 1
    }, ccdIMLbl, {
        html : "Trigger mode: ",
        border : false
    }, {
        border : false,
        colspan : 1
    }, ccdTMLbl, {
        html : "Time remaining (sec): ",
        border : false
    }, {
        border : false,
        colspan : 1
    }, ccdTRLbl, {
        html : "Exposure time (sec): ",
        border : false
    }, ccdETFld, ccdETRBVFld ],
    buttons : [ setCcdETBtn ],
    buttonAlign : 'left',
    width : 'auto'
});

var ccdAcquisitionPanel = new Ext.Panel( {
    title : 'Acquisition configuration',
    width : 'auto',
    collapsible : true,
    items : [ ccdAcquisitionForm ]
});

var ccdAcquisitionLoad = function() {
    if (beamlineHeartbeatStore.getCount() > 0) {
        var record = beamlineHeartbeatStore.getAt(0);
        ccdAcquisitionForm.getForm().loadRecord(record);
    }
};

/** ************************ CCD File Panel ************************* */

// For focus mode, maybe it is better not to show this panel. The users
// do not need to decide where the file should be saved. They can still,
// however, choose to download the images. The file will be saved at
// a default folder with machine-generated names.

var ccdFilePathRBV = new Ext.form.TextField( {
    name : 'ccdFilePathRBV',
    width : fieldWidth,
    readOnly : true,
    cls : 'ss-form-field-label',
    value : 'N/A'
});

var ccdFileName = new Ext.form.TextField( {
    name : 'ccdFileName',
    id : 'ccdFileName',
    width : fieldWidth
});

var ccdFileNameRBV = new Ext.form.TextField( {
    name : 'ccdFileNameRBV',
    id : 'ccdFileNameRBV',
    width : fieldWidth,
    readOnly : true,
    cls : 'ss-form-field-readonly',
    value : 'N/A'
});

var ccdFileNum = new Ext.form.NumberField( {
    name : 'ccdFileNumber',
    id : 'ccdFileNumber',
    width : fieldWidth,
    minValue : 0,
	allowDecimals: false
});

var ccdFileNumRBV = new Ext.form.NumberField( {
    name : 'ccdFileNumberRBV',
    id : 'ccdFileNumberRBV',
    width : fieldWidth,
    readOnly : true,
    cls : 'ss-form-field-readonly'
});


var ccdAutoIncRBV = new Ext.form.TextField( {
    name : 'ccdAutoIncrementRBV',
    width : fieldWidth,
    readOnly : true,
    cls : 'ss-form-field-label',
    value : 'N/A'
});


var ccdFileTempRBV = new Ext.form.TextField( {
    name : 'ccdFileTemplateRBV',
    width : fieldWidth,
    readOnly : true,
    cls : 'ss-form-field-label',
    value : 'N/A'
});

var ccdFullNameRBV = new Ext.form.TextField( {
    name : 'ccdFullfilenameRBV',
    width : fieldWidth,
    readOnly : true,
    cls : 'ss-form-field-label',
    value : 'N/A'
});

var ccdFileFormatRBV = new Ext.form.TextField( {
    name : 'ccdFileFormatRBV',
    width : fieldWidth,
    readOnly : true,
    cls : 'ss-form-field-label',
    value : 'N/A'
});

var setCcdFileBtn = new Ext.Button( {
    text : 'set'
});

setCcdFileBtn.on('click', function() {
	setCcdFile();
});

var ccdFileForm = new Ext.FormPanel( {
    border : false,
    layout : 'table',
    defaults : {
        bodyStyle : 'padding: 2px'
    },
    layoutConfig : {
        columns : 3
    },
    items : [ {
        html : "File path",
        border : false
    }, {
        border : false,
        colspan : 1
    } , ccdFilePathRBV, {
        html : "Auto increment",
        border : false
    }, {
        colspan : 1,
        border : false
    }, ccdAutoIncRBV,  {
        html : "File template",
        border : false
    }, {
        colspan : 1,
        border : false
    }, ccdFileTempRBV, {
        html : "Full file name",
        border : false
    }, {
        colspan : 1,
        border : false
    }, ccdFullNameRBV,  {
        html : "File format",
        border : false
    }, {
        border : false,
        colspan : 1
    }, ccdFileFormatRBV,
    {
        html : "Name prefix",
        border : false
    }, ccdFileName, ccdFileNameRBV,  {
        html : "Start number",
        border : false
    }, ccdFileNum, ccdFileNumRBV
    ],
    buttons : [ setCcdFileBtn ],
    buttonAlign : 'left',
    width : 'auto'
});

var ccdFilePanel = new Ext.Panel( {
    title : 'File configuration',
    width : 'auto',
    collapsible : true,
	collapsed : true,
    items : [ ccdFileForm ]
});

var ccdFileLoad = function() {
    if (beamlineHeartbeatStore.getCount() > 0) {
        var record = beamlineHeartbeatStore.getAt(0);
        ccdFileForm.getForm().loadRecord(record);
    }
};

/** ************************ CCD Acquire Panel ************************* */


var ccdFocusStartBtn = new Ext.Button( {
    text : 'Focus'
});

ccdFocusStartBtn.on('click', function() {
    ccdFocusStopBtn.enable();
    ccdFocus('start');
});

var ccdFocusStopBtn = new Ext.Button( {
    text : 'Stop',
    disabled : true
});

ccdFocusStopBtn.on('click', function() {
    ccdFocusStartBtn.enable();
    ccdFocus('stop');
});

var saveSPEBtn = new Ext.Button( {
    text : 'Save SPE'
});

saveSPEBtn.on('click', function() {
    var fileName = beamlineHeartbeatStore.getAt(0).get('ccdFileNameRBV');
    if (fileName != null) {
        saveFocusImage(fileName, 'spe');
    }
});

var savePNGBtn = new Ext.Button( {
    text : 'Save PNG'
});

savePNGBtn.on('click', function() {
    var fileName = beamlineHeartbeatStore.getAt(0).get('ccdFileNameRBV');
    if (fileName != null) {
        saveFocusImage(fileName, 'png');
    }
});


var ccdAquireLbl = new Ext.form.TextField({
			name : 'ccdAcquire',
			width : fieldWidth,
			readOnly : true,
			cls : 'ss-form-field-label'
		});

var ccdAcquireForm = new Ext.FormPanel( {
    border : false,
    layout : 'table',
    defaults : {
        bodyStyle : 'padding: 2px'
    },
    layoutConfig : {
        columns : 3
    },
    items : [
    {html : "Acquiring: ", border : false}, {border : false, colspan : 1}, ccdAquireLbl,
    ccdFocusStartBtn, {
        border : false,
        colspan : 1
    }, ccdFocusStopBtn, saveSPEBtn, {
        border : false,
        colspan : 1
    }, savePNGBtn ],
    buttonAlign : 'left',
    width : 'auto'
});

var ccdAcquirePanel = new Ext.Panel( {
    title : 'Focus',
    width : 'auto',
    collapsible : true,
    collapsed: true,
    items : [ ccdAcquireForm ]
});

var ccdAcquireLoad = function() {
    if (beamlineHeartbeatStore.getCount() > 0) {
        var record = beamlineHeartbeatStore.getAt(0);
        ccdAcquireForm.getForm().loadRecord(record);

    }
};


/** ************************ Check latest scan image Panel ************************* */


var ccdChecktBtn = new Ext.Button( {
    text : 'Check image'
});

ccdChecktBtn.on('click', function() {
    ccdCheck();
});


var saveSPEBtn_scan = new Ext.Button( {
    text : 'Save SPE'
});

// TODO fix these two

saveSPEBtn_scan.on('click', function() {
    var fileName = beamlineHeartbeatStore.getAt(0).get('ccdFileNameRBV');
    if (fileName != null) {
        saveCCDImage(fileName, 'spe');
    }
});

var savePNGBtn_scan = new Ext.Button( {
    text : 'Save PNG'
});

savePNGBtn_scan.on('click', function() {
    var fileName = beamlineHeartbeatStore.getAt(0).get('ccdFileNameRBV');
    if (fileName != null) {
        saveCCDImage(fileName, 'png');
    }
});


var ccdAquireStateLbl = new Ext.form.TextField({
			name : 'ccdAcquireState', //ccdDetectorStateRBV?
			width : fieldWidth,
			readOnly : true,
			cls : 'ss-form-field-label'
		});

var ccdCheckForm = new Ext.FormPanel( {
    border : false,
    layout : 'table',
    defaults : {
        bodyStyle : 'padding: 2px'
    },
    layoutConfig : {
        columns : 3
    },
    items : [
    // ccdDetectorStateRBV?
    {html : "Acquiring: ", border : false}, {border : false, colspan : 1}, ccdAquireStateLbl,
    ccdChecktBtn, {
        border : false,
        colspan : 5
    }, saveSPEBtn_scan, {
        border : false,
        colspan : 1
    }, savePNGBtn_scan ],
    buttonAlign : 'left',
    width : 'auto'
});

var ccdCheckPanel = new Ext.Panel( {
    title : 'Check image',
    width : 'auto',
    collapsible : true,
    collapsed: true,
    items : [ ccdCheckForm ]
});

var ccdCheckLoad = function() {
    if (beamlineHeartbeatStore.getCount() > 0) {
        var record = beamlineHeartbeatStore.getAt(0);
        ccdCheckForm.getForm().loadRecord(record);

    }
};


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
	if (beamlineHeartbeatStore.getCount() > 0) {
		var record = beamlineHeartbeatStore.getAt(0);
		var mode = record.get('bsXrdMode');
		var fileName = record.get('ccdFileNameRBV');
		var fullFileName = record.get('ccdFullfilenameRBV');
		var acquire = record.get('ccdAcquire');
		ccdImagePanel.getComponent('ccdImg').setText(acquire);
		if ((mode == 'focus') && (acquire == 'Done') && (fullFileName.indexOf(fileName) != -1)) {
			ccdImagePanel.getComponent('ccdImg').setImageURL(beamlineBaseURL + '/ccdFocusImage?type=png&file='
					+ fileName);
		}
	}
};


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
