/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *     videoCameraPanel.js
 *     
 */

var camera101Panel = new Ext.ss.cls.AxisPTZCameraPanel({
	url: 'camera',
	name: 'camera101',
	title: 'camera101',
	imageWidth:704,
	imageHeight:480,
	frame: false
});

var camera201Panel = new Ext.ss.cls.AxisPTZCameraPanel({
	url: 'camera',
	name:'camera201',
	title: 'camera201',
	imageWidth:704,
	imageHeight:480,
	frame: false
});

var camera202Panel = new Ext.ss.cls.AxisPTZCameraPanel({
	url: 'camera',
	name: 'camera202',
	title: 'camera202',
	imageWidth:704,
	imageHeight:480,
	frame: false
});

var camerasTabPanel = new Ext.TabPanel({
	activeTab:0,
	tabPosition:'bottom',
	items:[
	    camera101Panel,
	    camera201Panel,
	    camera202Panel
	]
});

var camerasPanel = new Ext.Panel({
	id: 'CAMERA_PANEL',
	title: 'Cameras',
	closable: false,
	layout: 'fit',
	items: [camerasTabPanel],
	autoScroll: true
});
