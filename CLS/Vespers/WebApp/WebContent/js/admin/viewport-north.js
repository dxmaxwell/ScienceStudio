/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *    viewport-north.js
 */

var northPanel = new Ext.Panel({
	region: 'north',
	layout: 'column',
	items:[{
		xtype: 'box',
		columnWidth: 0.2,
		autoEl:{
			tag: 'div',
			cn: [{
				tag: 'img',
				src: '/ssstatic/img/scstudio-logo-small.png',
				style: 'padding:5px;'
			}]
		}	
	},{
		xtype: 'box',
		columnWidth: 0.6,
		autoEl: {
			tag: 'div',
			html: pageHeaderTitle,
			style: 'text-align:center;padding:15px;font-size:x-large;font-weight:bold'
		}
	},{
		xtype: 'box',
		columnWidth: 0.2,
		autoEl: {
			tag: 'div',
			cn: [{
				tag: 'div',
				id: 'HEARTBEAT_ACTIVITY_METER'
			}],
			style: 'padding:20px 60px;'
		}
	}],	
	border: false,
	height: 60
});
