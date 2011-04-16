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
	reader: new Ext.data.JsonReader({
		root:'response',
		successProperty:'success',
		fields:[
		    { name:'name', mapping:'scanForm.name' },
		    { name:'dataUrl', mapping:'scanForm.dataUrl' },
		    { name:'endDate', mapping:'scanForm.endDate' },
		    { name:'startDate', mapping:'scanForm.startDate' },
		    { name:'parameters', mapping:'scanForm.parameters' }
		]
	}),
	border: false,
	padding:'5px 5px 5px 5px'
});

function scanFormPanelLoad() {	
	scanFormPanel.getForm().load({
		method:'GET',
		url:'../../scan/' + scanId + '/form.json',
		failure:function(form, action) {
			if(action.failureType === Ext.form.Action.CONNECT_FAILURE) {
				Ext.Msg.alert('Error', 'Network connection problem.');
			}
			else if(action.failureType === Ext.form.Action.LOAD_FAILURE) {
				if(action.reader.jsonData && action.reader.jsonData.globalError) {
					Ext.Msg.alert('Error', action.reader.jsonData.globalError);
				} else {
					Ext.Msg.alert('Error', 'An unspecified error has occurred.');
				}
			}
		}
	});
};

var sampleImagePanel = new Ext.Panel({
	items:[{
		xtype:'box',
		autoEl:{
			tag:'a',
			target:'_blank',
			href:'../../scan/' + scanId + '/file/sample.png',
			cn:[{
				tag:'img',
				alt:'Sample Image Not Available',
				src:'../../scan/' + scanId + '/file/sample.png',
				style:'width:100%;'
			}]
		}
	}],
	border:false
});

var eastPanel = new Ext.Panel({
	region:'east',
	split:true,
	autoScroll:true,
	collapsible:true,
	items:[
	    scanFormPanel,
	    sampleImagePanel
	],
	minWidth:200,
	width:500
});
