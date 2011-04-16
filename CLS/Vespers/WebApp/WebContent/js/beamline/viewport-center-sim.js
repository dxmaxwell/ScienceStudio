/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *     viewport-center-sim.js
 *     
 */

var centerPanel = new Ext.TabPanel({
	id:'CENTER_TAB_PANEL',
	region : 'center',
	activeTab : 0,
	items: [
	    beamlinePanel,
	    experimentPanel,
	    vortexDetectorPanel,
	    helpPanel
	],
	deferredRender: false,
	autoScroll : true
});
