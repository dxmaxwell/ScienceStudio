/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *    viewport-center.js
 */

var mainPanel = new Ext.Panel({
	id: 'MAIN_PANEL',
	autoLoad:{
		url:'sessions.html',
		scripts:true
	},
	border: false
});

var centerPanel = new Ext.Panel({
	region: 'center',
	items: [ mainPanel ],
	autoScroll: true
});
