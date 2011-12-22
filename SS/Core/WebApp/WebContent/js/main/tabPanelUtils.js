/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *     tab panel utilities
 *     
 */


/*
 * Functions for manipulating Model View tab.
 */

function addItemModelViewTab(componentOrConfig, doLayout) {
	var target = Ext.getCmp('VIEW_TAB');
	if(target && target.add) {
		target.add(componentOrConfig);
		if(doLayout && target.doLayout) {
			target.doLayout();
		}
	}
	return false;
}

function loadModelViewTab(viewUrl) {
	if(viewUrl) {
		var target = Ext.getCmp('VIEW_TAB');
		if(target && target.load) {
			target.removeAll(true);
			target.load({
				url: viewUrl,
				nocache: false,
				text: "Loading...",
			    timeout: 30,
			    scripts: true
			});
		
			if(target.findParentByType) {
				var tabPanel = target.findParentByType('tabpanel');
				tabPanel.setActiveTab(target);
			}
		}
	}
	
	return false;
}

/*
 * Functions for manipulating Home tab.
 */
function addItemHomeTab(componentOrConfig, doLayout) {
	var target = Ext.getCmp('HOME_PANEL');
	if(target && target.add) {
		target.add(componentOrConfig);
		if(doLayout && target.doLayout) {
			target.doLayout();
		}
	}
	return false;
}

function activateHomeTab() {
	var target = Ext.getCmp('HOME_PANEL');
	if(target && target.findParentByType) {
		var tabPanel = target.findParentByType('tabpanel');
		tabPanel.setActiveTab(target);
	}
	return false;
}


/*
 * Functions for manipulating Laboratory View tab.
 */

var labViewWindows = {};

function openLabViewTab(sessionGid, useWindow) {
	
	if(sessionGid) {
		var labViewPanelId = 'LAB_VIEW_PANEL_' + sessionGid;
	
		var centerTabPanel = Ext.getCmp('CENTER_TAB_PANEL');
		if(centerTabPanel) {
			var labViewPanel = centerTabPanel.findById(labViewPanelId);
			if(labViewPanel) {
				centerTabPanel.setActiveTab(labViewPanel);
				return false;
			}
		}
		
		var labViewWindowId = 'SS_LAB_VIEW_WINDOW_' + sessionGid;
		
		var labViewWindow = labViewWindows[labViewWindowId];
		if(labViewWindow && !labViewWindow.closed) {
			labViewWindow.focus();
			return;
		}
		
		var labViewUrl = "laboratory/view.html?session=" + sessionGid;
			
		if(useWindow) {
			labViewWindow = window.open(labViewUrl, labViewWindowId);
			labViewWindows[labViewWindowId] = labViewWindow;
			labViewWindow.focus();
		}
		else {
			var labViewPanel = new Ext.Panel({
				xtype: 'box',
				id: labViewPanelId,
				title: 'Session',
				items: [{
					xtype: 'box',
					autoEl: {
						tag: 'iframe',
						src: labViewUrl,
						style: 'height:100%;width:100%;border-style:hidden;'
					}
				}],
	    		closable:true,
	    		autoScroll:true
			});
			centerTabPanel.add(labViewPanel);
			centerTabPanel.setActiveTab(labViewPanel);
		}
	}
}

/*
 * Functions for manipulating Data View  tab.
 */

var dataViewWindows = {};

function openDataViewTab(scanGid, useWindow) {
	
	if(scanGid) {
		var dataViewPanelId = 'DATA_VIEW_PANEL_' + scanGid;
	
		var centerTabPanel = Ext.getCmp('CENTER_TAB_PANEL');
		if(centerTabPanel) {
			var dataViewPanel = centerTabPanel.findById(dataViewPanelId);
			if(dataViewPanel) {
				centerTabPanel.setActiveTab(dataViewPanel);
				return false;
			}
		}
		
		var dataViewWindowId = 'SS_DATA_VIEW_WINDOW_' + scanGid;
		
		var dataViewWindow = dataViewWindows[dataViewWindowId];
		if(dataViewWindow && !dataViewWindow.closed) {
			dataViewWindow.focus();
			return;
		}
		
		var dataViewUrl = "scan/data/view.html?scan=" + scanGid;
			
		if(useWindow) {
			dataViewWindow = window.open(dataViewUrl, dataViewWindowId);
			dataViewWindows[dataViewWindowId] = dataViewWindow;
			dataViewWindow.focus();
		}
		else {
			var dataViewPanel = new Ext.Panel({
				xtype: 'box',
				id: dataViewPanelId,
				title: 'Data',
				items: [{
					xtype: 'box',
					autoEl: {
						tag: 'iframe',
						src: dataViewUrl,
						style: 'height:100%;width:100%;border-style:hidden;'
					}
				}],
	    		closable:true,
	    		autoScroll:true
			});
			centerTabPanel.add(dataViewPanel);
			centerTabPanel.setActiveTab(dataViewPanel);
		}
	}
}
