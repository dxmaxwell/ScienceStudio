/**
 * Copyright (c) Canadian Light Source, Inc. All rights reserved. - see
 * license.txt for details.
 *
 * Description:
 * 		viewport-center.js
 *
 */

var spectrumScaleDate = [
   [ 'Linear', 'LINEAR' ],
   [ 'Logarithmic', 'LOG10' ]
];

var spectrumScaleStore = new Ext.data.SimpleStore({
	fields:[ 'display', 'value' ],
	data:spectrumScaleDate
});

var spectrumScaleCmbBox = new Ext.form.ComboBox({
	fieldLabel: 'Scale',
	store: spectrumScaleStore,
	value: spectrumScaleDate[0][1],
	mode: 'local',
	editable : false,
	forceSelection: true,
	triggerAction: 'all',
	displayField: 'display',
	valueField: 'value',
	width: 100
});

spectrumScaleCmbBox.on('select', spectrumGraphScaleUpdate);

var spectrumPointXStore = new Ext.data.ArrayStore({
	url: '../../scan/' + scanId + '/data/mapxy/I.json',
	restful:true,
	root:'response.I',
	success:'success',
	fields:[ 'I' ]
});

var spectrumPointXCmbBox = new Ext.form.ComboBox({
	fieldLabel: 'X',
	name: 'scanPoint',
	store: spectrumPointXStore,
	mode: 'remote',
	editable: false,
	forceSelection: true,
	triggerAction: 'all',	
	displayField: 'I',
	width: 50
});

spectrumPointXCmbBox.on('select', spectrumGraphDataUpdate);

var spectrumPointYStore = new Ext.data.ArrayStore({
	url: '../../scan/' + scanId + '/data/mapxy/J.json',
	restful:true,
	root:'response.J',
	success:'success',
	fields:[ 'J' ]
});

var spectrumPointYCmbBox = new Ext.form.ComboBox({
	fieldLabel: 'Y',
	name: 'scanPoint',
	store: spectrumPointYStore,
	mode: 'remote',
	editable: false,
	forceSelection: true,
	triggerAction: 'all',
	displayField: 'J',
	width: 50
});

spectrumPointYCmbBox.on('select', spectrumGraphDataUpdate);

var downloadCdfmlBtn = new Ext.Button({
	text:'Download CDFML'
});

downloadCdfmlBtn.on('click', function() {
	convert('CDF', 'CDFML', downloadCdfml, this);
});

function downloadCdfml() {
	window.location = '../../scan/' + scanId + '/file/data.cdfml';
}

var spectrumControlPanel = new Ext.Panel({
	layout: { 
		type:'hbox',
		align:'middle',
		padding:'10px'
	},
	defaults:{
		layout: 'form',
		labelAlign:'right',
		border:false
	},
	items:[
	       {
		items:[{
			xtype:'label',
			text:'Select Data Point',
			cls:'x-form-item x-form-item-label',
			border:false
		}]
	},{
		labelWidth:20,
		items:[ spectrumPointXCmbBox ]
	},{
		labelWidth:20,
		items:[ spectrumPointYCmbBox ]
	},{
		
		items:[ spectrumScaleCmbBox ]
	},{
		// spacer //
		flex:1
	},{
		items:[ downloadCdfmlBtn ]
	}],
	border:false
});

var spectrumGraphBox = new Ext.BoxComponent({
	autoEl:'div'
});

var spectrumGraphPanel = new Ext.Panel({
	region:'center',
	items: [ spectrumGraphBox ],
	border:false
});

var centerPanel = new Ext.Panel({
	title:'XRF Spectrum',
	region:'center',
	layout:'border',
	autoScroll:true,
	items:[
	    spectrumGraphPanel,
	{
		region:'south',
	    items:[ spectrumControlPanel ],
	    height:50
	}]
});
