/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *     viewport-center.js
 *     
 */

//
//var xrfTab = new Ext.Panel({
//		id : 'XRF_TAB',
//		title : 'XRF',
//		closable : false,
//		autoScroll : true,
//		layout : 'border',
//		items : [vtxDataPanel, vtxControlsPanel]
//	});
//
//
//var xrdTab = new Ext.Panel({
//		id : 'XRD_TAB',
//		title : 'XRD',
//		closable : false,
//		autoScroll : true,
//		layout : 'border',
//		items : [ccdConfigPanel, ccdImagePanel, ccdCollectionPanel],
//		listeners : {
//			activate : function(panel) {
//				// common tasks
//				ccdSetupRunner.start(ccdSetupTask);
//				ccdStageRunner.start(ccdStageTask);
//				ccdAcquisitionRunner.start(ccdAcquisitionTask);
//
//					ccdAcquireRunner.start(ccdAcquireTask);
//					ccdFocusImageRunner.start(ccdFocusImageTask);
//
//					ccdFileRunner.start(ccdFileTask);
//					ccdCheckRunner.start(ccdCheckTask);
//
//			},
//			deactivate : function(panel) {
//				// common tasks
//				ccdSetupRunner.stop(ccdSetupTask);
//				ccdStageRunner.stop(ccdStageTask);
//				ccdAcquisitionRunner.stop(ccdAcquisitionTask);
//
//				// focus tasks
//				ccdAcquireRunner.stop(ccdAcquireTask);
//				ccdFocusImageRunner.stop(ccdFocusImageTask);
//
//				// scan tasks
//				ccdFileRunner.stop(ccdFileTask);
//				ccdCheckRunner.stop(ccdCheckTask);
//
//			}
//		}
//	});

var centerPanel = new Ext.TabPanel({
	id:'CENTER_TAB_PANEL',
	region : 'center',
	activeTab : 0,
	items: [
	    beamlinePanel,
	    experimentPanel,
	    fourElementDetectorPanel,
	    camerasPanel,
	    helpPanel
	    //xrfTab,
	    //xrdTab,
	],
	deferredRender: false,
	autoScroll : true
});

