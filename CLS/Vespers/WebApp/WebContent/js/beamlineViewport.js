/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *     beamlineViewport.js
 *
 */

/**
 *
 * @include "include.js"
 */

var sessionViewport;
var sessionTabPanel;

function initLayout() {

	var blSetupTab = new Ext.Panel({
				id : 'BEAMLINE_SETUP_TAB',
				title : 'Beamline Setup',
				closable : false,
				autoScroll : true,
				layout : 'border',
				items : [beamlineImage, beamlinePanel]
			});

	var expSetupTab = new Ext.Panel({
				id : 'EXPERIMENT_SETUP_TAB',
				title : 'Experiment Setup',
				closable : false,
				autoScroll : true,
				layout : 'border',
				items : [controlsPanel, imagePanel, experimentPanel]
			});

	var xrfTab = new Ext.Panel({
				id : 'XRF_TAB',
				title : 'XRF',
				closable : false,
				autoScroll : true,
				layout : 'border',
				items : [vtxDataPanel, vtxControlsPanel]
			});

	var xrf4Tab = new Ext.Panel({
				id : 'XRF4_TAB',
				title : 'XRF4',
				closable : false,
				autoScroll : true,
				layout : 'border',
				items : [fedDataPanel, fedControlsPanel]
			});
	
	var xrdTab = new Ext.Panel({
				id : 'XRD_TAB',
				title : 'XRD',
				closable : false,
				autoScroll : true,
				layout : 'border',
				items : [ccdConfigPanel, ccdImagePanel, ccdCollectionPanel],
				listeners : {
					activate : function(panel) {
						// common tasks
						ccdSetupRunner.start(ccdSetupTask);
						ccdStageRunner.start(ccdStageTask);
						ccdAcquisitionRunner.start(ccdAcquisitionTask);

							ccdAcquireRunner.start(ccdAcquireTask);
							ccdFocusImageRunner.start(ccdFocusImageTask);

							ccdFileRunner.start(ccdFileTask);
							ccdCheckRunner.start(ccdCheckTask);

					},
					deactivate : function(panel) {
						// common tasks
						ccdSetupRunner.stop(ccdSetupTask);
						ccdStageRunner.stop(ccdStageTask);
						ccdAcquisitionRunner.stop(ccdAcquisitionTask);

						// focus tasks
						ccdAcquireRunner.stop(ccdAcquireTask);
						ccdFocusImageRunner.stop(ccdFocusImageTask);

						// scan tasks
						ccdFileRunner.stop(ccdFileTask);
						ccdCheckRunner.stop(ccdCheckTask);

					}
				}
			});


	var camerasTab = new Ext.Panel({
				id : 'CAMERAS_TAB',
				title : 'Cameras',
				closable : false,
				autoScroll : true,
				layout : 'fit',
				items : [videoCameraTabPanel]
			});

	var helpTab = new Ext.Panel({
				id : 'HELP_TAB',
				title : 'Help',
				closable : false,
				autoScroll : true,
				layout : 'fit',
				items : [beamlineHelpTabPanel]
			});

	sessionTabPanel = new Ext.TabPanel({
				region : 'center',
				deferredRender : false,
				autoScroll : true,
				activeTab : 0,
				tabPosition : 'top',
				items : [blSetupTab, expSetupTab, xrfTab, xrf4Tab, xrdTab, camerasTab,
						helpTab]
			});

	sessionViewport = new Ext.Viewport({
				layout : 'border',
				items : [sessionTabPanel]
			});
}

Ext.onReady(initLayout);
