/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *     viewport-center.js
 *     
 */

function centerPanel() {

	var homeTabPanel = new Ext.Panel({
		id: 'HOME_PANEL',
		title: 'Home',
		autoLoad: {
			url: 'home.html',
			scripts: true,
			text: 'Loading...'
		},
		closeable: false,
		autoScroll: true
	});
	
	var viewTabPanel = new Ext.Panel({
		id: 'VIEW_TAB',
		title: 'View',
		closable: false,
		autoScroll: true
	});
	
	var centerPanel = new Ext.TabPanel({
		id: 'CENTER_TAB_PANEL',
		region: 'center',
		deferredRender: false,
		enableTabScroll: true,
		activeTab: 0,
		items:[ homeTabPanel, viewTabPanel ],
		autoScroll: true,
		margins: '0 4 4 0'
	});
	
	return centerPanel;
}
   