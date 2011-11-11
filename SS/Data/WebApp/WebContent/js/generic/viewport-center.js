/**
 * Copyright (c) Canadian Light Source, Inc. All rights reserved. - see
 * license.txt for details.
 *
 * Description:
 * 		viewport-center.js
 *
 */


var scanFilePanel = new Ext.ss.data.ScanFilePanel({
	fileListUrl: genericServletPath + '/scan/' + scanGid + '/file/list.json',
	downloadUrl: genericServletPath + '/scan/' + scanGid + '/file/download',
	style: {
		'margin':'20px'
	},
	border:false,
	height:400,
	width:800
});

var centerPanel = new Ext.Panel({
	region:'center',
	items: [ scanFilePanel ]
});
