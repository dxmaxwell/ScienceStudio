/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *     viewport-center.js
 *     
 */

var sessionStatusLeftPanel = new Ext.BoxComponent({
	columnWidth:0.15,
	autoEl: {
		tag: 'div',
		cn: [
		//{
		//	tag:'img',
		//	src:'../img/uwo-logo-icon-xsmall.png'
		//},
		{		
			tag: 'span',
			html: 'LEO1540XB',	
			style: {
				'vertical-align': '5px',
				'font-weight':'bold',
				'font-size':'large'
			}
		}]
	},
	style: {		
		'opacity': '1.0'	
	}
});

var sessionStatusCenterPanel = new Ext.BoxComponent({
	columnWidth:0.70,
	autoEl: {
		tag: 'div',
		html: '<span style="color:white;">_</span>'
	},
	style: {
		'text-align':'center',
		'padding': '4px 0px',
		'font-size':'large',
		'opacity': '1.0'
	}
});

var sessionStatusRightPanel = new Ext.BoxComponent({
	columnWidth:0.15,
	id: 'SESSION_STATUS_RIGHT_PANEL',
	autoEl: {
		tag:'div',
		cn: {
			tag: 'div',
			id: 'HEARTBEAT_ACTIVITY_METER',	
			html:'Heartbeat'
		}
	},
	style: {
		'padding': '8px 0px',
		'opacity': '1.0'
	}
}); 

var sessionStatusPanel = new Ext.Panel({
	layout: 'column',
	items:[
	    sessionStatusLeftPanel,
	    sessionStatusCenterPanel,
	    sessionStatusRightPanel
	]
});

function showSessionStopped() {
	if(sessionStatusPanel.body && !sessionStatusPanel.body.hasClass('ss-session-stopped')) {
	
		if(sessionStatusCenterPanel.el) {
			sessionStatusCenterPanel.el.update("Session Stopped");
		}
	
		sessionStatusPanel.body.removeClass('ss-session-observe');
		sessionStatusPanel.body.removeClass('ss-session-control');
		sessionStatusPanel.body.addClass('ss-session-stopped');
	}
};

function showSessionObserver() {
	if(sessionStatusPanel.body && !sessionStatusPanel.body.hasClass('ss-session-observe')) {
	
		if(sessionStatusCenterPanel.el) {
			sessionStatusCenterPanel.el.update("Session Observer");
		}
	
		sessionStatusPanel.body.removeClass('ss-session-stopped');
		sessionStatusPanel.body.removeClass('ss-session-control');
		sessionStatusPanel.body.addClass('ss-session-observe');
	}
};

function showSessionController() {
	if(sessionStatusPanel.body && !sessionStatusPanel.body.hasClass('ss-session-control')) {
		
		if(sessionStatusCenterPanel.el) {
			sessionStatusCenterPanel.el.update("Session Controller");
		}
	
		sessionStatusPanel.body.removeClass('ss-session-stopped');
		sessionStatusPanel.body.removeClass('ss-session-observe');
		sessionStatusPanel.body.addClass('ss-session-control');
	}
};

var northPanel = new Ext.Panel({
	region: 'north',
	items:[
	    sessionStatusPanel
	],
	height:30,
	width:'100%'
});
