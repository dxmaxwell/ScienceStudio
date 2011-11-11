/**
 * Copyright (c) Canadian Light Source, Inc. All rights reserved. - see
 * license.txt for details.
 *
 * Description:
 * 		viewport-east.js
 *
 */


var scanFormPanel = new Ext.ss.data.ScanFormPanel({
	title:'Scan',
	labelAlign: 'right',
	defaults: {
		disabled:true
	},
	buttonDefaults: {
		hidden:true
	},
	border: false,
	padding:'5px 5px 5px 5px'
});

if(scanFormData.startDate) {
	var startDate = Date.parseDate(scanFormData.startDate, 'c');
	if(startDate) {
		scanFormData.startDate = startDate.format(Date.patterns.ISO8601Long);
	}
}

if(scanFormData.endDate) {
	var endDate = Date.parseDate(scanFormData.endDate, 'c');
	if(endDate) {
		scanFormData.endDate = endDate.format(Date.patterns.ISO8601Long);
	}
}

scanFormPanel.getForm().setValues(scanFormData);


var eastPanel = new Ext.Panel({
	region:'east',
	split:true,
	autoScroll:true,
	//collapsible:true,
	items:[
	    scanFormPanel,
	],
	minWidth:200,
	width:500
});
