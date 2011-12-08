/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *      AxisPTZCameraPanel class.
 *     
 */
Ext.namespace('Ext.ss.cls');

Ext.ss.cls.AxisPTZCameraPanel = function(config) {
	
	var btnWidth = 60;
	
	var panelConfig = Ext.apply({}, config);
	
	this.ss = {
		imageElement: document.createElement('img')
	};
	
	var cameraUrl = '/';
	if(panelConfig.url) {
		cameraUrl = panelConfig.url;
		delete panelConfig.url;
	}
	
	var cameraName = 'unknown';
	if(panelConfig.name) {
		cameraName = panelConfig.name;
		delete panelConfig.name;
	}
	
	this.ss.imageUrl = cameraUrl + '/' + cameraName + '/image.jpg';
	this.ss.videoUrl = cameraUrl + '/' + cameraName + '/video.mjpg';
	this.ss.controlUrl = cameraUrl + '/' + cameraName + '/control.json';
	
	var width = 0;
	if(panelConfig.imageWidth) {
		width = panelConfig.imageWidth;
		delete panelConfig.imageWidth;
	}
	
	var height = 0;
	if(panelConfig.imageHeight) {
		height = panelConfig.imageHeight;
		delete panelConfig.imageHeight;
	}
	
	this.ss.imageElement.panel = this;
	this.ss.imageElement.src = this.ss.imageUrl;
	this.ss.imageElement.onclick = function(event) {
		var x = event.layerX - event.target.offsetLeft;
		var y = event.layerY - event.target.offsetTop;
		this.panel.handlers.doCenterRelative.call(this.panel, x+','+y);
	};
	
	this.ss.imagePanel = new Ext.Panel();
	this.ss.imagePanel.on('render', this.handlers.renderImagePanel, this);
	
	this.ss.videoStartBtn = new Ext.Button({
		text:'Start',
		handler:this.handlers.clickVideoStartBtn,
		scope:this,
		minWidth:btnWidth
	});
	
	this.ss.videoStopBtn = new Ext.Button({
		text:'Refresh',
		handler:this.handlers.clickVideoStopBtn,
		scope:this,
		minWidth:btnWidth
	});
	
	this.ss.videoPanel = new Ext.Panel({
		title:'Video',
		layout:'table',
		layoutConfig: {
			columns:5
		},
		items:[
		       { width:10 },
		       this.ss.videoStartBtn,
		       { width:5 },
		       this.ss.videoStopBtn,
		       { width:10 }
		],
		frame:true,
		height:'auto',
		width:'auto'
	});
	
	
	this.ss.zoomInBtn = new Ext.Button({
		text:'In',
		handler:this.handlers.clickZoomInBtn,
		scope:this,
		minWidth:btnWidth
	});
	
	this.ss.zoomOutBtn = new Ext.Button({
		text:'Out',
		handler:this.handlers.clickZoomOutBtn,
		scope:this,
		minWidth:btnWidth
	});

	this.ss.zoomPanel = new Ext.Panel({
		title:'Zoom',
		layout:'table',
		layoutConfig: {
			columns:5
		},
		items:[
		    { width: 10 },
		    this.ss.zoomInBtn,
		    { width: 5 },
		    this.ss.zoomOutBtn,
		    { width: 10 }
		],
		frame: true,
		height: 'auto',
		width: 'auto'
	});
	
	this.ss.focusNearBtn = new Ext.Button({
		text:'Near',
		handler:this.handlers.clickFocusNearBtn,
		scope:this,
		minWidth:btnWidth
	});
	
	this.ss.focusFarBtn = new Ext.Button({
		text:'Far',
		handler:this.handlers.clickFocusFarBtn,
		scope:this,
		minWidth:btnWidth
	});

	this.ss.focusPanel = new Ext.Panel({
		title:'Focus',
		layout:'table',
		layoutConfig: {
			columns:5
		},
		items:[
		    { width: 10 },
		    this.ss.focusNearBtn,
		    { width: 5 },
		    this.ss.focusFarBtn,
		    { width: 10 }
		],
		frame: true,
		height: 'auto',
		width: 'auto'
	});
	
	this.ss.irisOpenBtn = new Ext.Button({
		text:'Open',
		handler:this.handlers.clickIrisOpenBtn,
		scope:this,
		minWidth:btnWidth
	});
	
	this.ss.irisCloseBtn = new Ext.Button({
		text:'Close',
		handler:this.handlers.clickIrisCloseBtn,
		scope:this,
		minWidth:btnWidth
	});

	this.ss.irisPanel = new Ext.Panel({
		title:'Iris',
		layout:'table',
		layoutConfig: {
			columns:5
		},
		items:[
		    { width: 10 },
		    this.ss.irisOpenBtn,
		    { width: 5 },
		    this.ss.irisCloseBtn,
		    { width: 10 }
		],
		frame: true,
		height: 'auto',
		width: 'auto'
	});
	
	this.ss.axisPGTZCameraPanel = new Ext.Panel({
		layout:'table',
		layoutConfig:{
			columns:2
		},
		items:[
		    this.ss.imagePanel,
		    {
		    	style: 'padding:10px;',
		    	items:[
		    	    this.ss.videoPanel,
		    	    { height: 10, border: 0, frame: false },
		    	    this.ss.zoomPanel
		    	    //{ height: 10, border: 0, frame: false },
		    	    //this.ss.focusPanel,
		    	    //{ height: 10, border: 0, frame: false },
		    	    //this.ss.irisPanel
		    	],
		    	frame: false,
		    	border: false,
		    	width: 'auto',
		    	height: 'auto'
		    }
		],
		frame: false,
		border: false,
		height: 'auto',
		width: 'auto'
	});
	
	panelConfig.items = [
			this.ss.axisPGTZCameraPanel
	];
	
	Ext.ss.cls.AxisPTZCameraPanel.superclass.constructor.call(this, panelConfig);
};

Ext.extend(Ext.ss.cls.AxisPTZCameraPanel, Ext.Panel, {
	
	handlers: {
		renderImagePanel:function() {
			this.ss.imagePanel.getEl().appendChild(this.ss.imageElement);
			this.ss.imagePanel.un('render', this.handlers.renderImagePanel, this);
		},
	
		clickVideoStartBtn:function() {
			this.ss.imageElement.src = this.ss.videoUrl + '?_dc=' + Math.round(Math.random() * 100000);
			this.ss.videoStartBtn.disable();
			this.ss.videoStopBtn.setText('Stop');
		},
		
		clickVideoStopBtn:function() {
			this.ss.imageElement.src = this.ss.imageUrl + '?_dc=' + Math.round(Math.random() * 100000);
			this.ss.videoStartBtn.enable();
			this.ss.videoStopBtn.setText('Refresh');
		},
		
		clickZoomInBtn:function() {
			this.handlers.doZoomRelative.call(this, 1000);
		},

		clickZoomOutBtn:function() {
			this.handlers.doZoomRelative.call(this, -1000);
		},
		
		clickFocusNearBtn:function() {
			this.handlers.doFocusRelative.call(this, -2500);
		},
		
		clickFocusFarBtn:function() {
			this.handlers.doFocusRelative.call(this, 2500);
		},
		
		clickIrisCloseBtn:function() {
			this.handlers.doIrisRelative.call(this, -250);
		},
		
		clickIrisOpenBtn:function() {
			this.handlers.doIrisRelative.call(this, 250);
		},
		
		doZoomRelative:function(zoom) {
			if(!this.ss.videoStartBtn.disabled) {
				this.handlers.clickVideoStartBtn.call(this);
			}
			Ext.Ajax.request({
				url:this.ss.controlUrl,
				params: { rzoom:zoom },
				callback:this.handlers.controlRequestCallback,
				scope:this
			});
		},
		
		doFocusRelative:function(focus) {
			if(!this.ss.videoStartBtn.disabled) {
				this.handlers.clickVideoStartBtn.call(this);
			}
			Ext.Ajax.request({
				url:this.ss.controlUrl,
				params: { rfocus:focus },
				callback:this.handlers.controlRequestCallback,
				scope:this
			});
		},
		
		doIrisRelative:function(iris) {
			if(!this.ss.videoStartBtn.disabled) {
				this.handlers.clickVideoStartBtn.call(this);
			}
			Ext.Ajax.request({
				url:this.ss.controlUrl,
				params: { riris:iris },
				callback:this.handlers.controlRequestCallback,
				scope:this
			});
		},
		
		doCenterRelative:function(rcenter) {
			if(!this.ss.videoStartBtn.disabled) {
				this.handlers.clickVideoStartBtn.call(this);
			}
			Ext.Ajax.request({
				url:this.ss.controlUrl,
				params: { rcenter:rcenter },
				callback:this.handlers.controlRequestCallback,
				scope:this
			});
		},
		
		controlRequestCallback:function(options, success, response) {
			var json = response.responseJson||Ext.decode(response.responseText, true);
			if(json && !json.success) {
				if(json.message) {
					Ext.Msg.alert("Error", message);
				} else {
					Ext.Msg.alert("Error", 'An unspecified error has occurred.');
				}
			}
		}
	}
});
