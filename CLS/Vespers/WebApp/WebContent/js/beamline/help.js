/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *     beamlineHelpPanels.js
 *     
 */


//	var generalHelpPanel = new Ext.Panel({
//		title: 'General',
//		items:[{
//			xtype: 'box',
//			autoEl: {
//				tag: 'iframe',
//				src: '../doc/d.html'
//			}
//		}],
//		frame: false
//	});

var experimentHelpPanel = new Ext.Panel({
	title: 'Experiment',
	items:[{
		xtype: 'box',
		autoEl: {
			tag: 'iframe',
			src: '../doc/experiment.html',
			style: 'height:100%;width:100%;'
		}
	}],
	frame: false
});

var helpTabPanel = new Ext.TabPanel({
	activeTab:0,
	tabPosition:'bottom',
	items:[
	       //generalHelpPanel,
	       experimentHelpPanel
	],
	deferredRender: false
});

var helpPanel = new Ext.Panel({
	id: 'HELP_PANEL',
	title: 'Help',
	closable: false,
	layout: 'fit',
	items: [ helpTabPanel ],
	autoScroll: true
});
