/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *     viewport-center.js
 *     
 */


var centerPanel = new Ext.TabPanel({
	id:'CENTER_TAB_PANEL',
	region : 'center',
	activeTab : 0,
	items: [
	    explorePanel
	],
	deferredRender: false,
	autoScroll : true
});

