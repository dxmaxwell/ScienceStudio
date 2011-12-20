/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *     explore.js
 */

/**********************experiment and scan name*/

var all_scans, scanId;


var experimentStore = new Ext.data.JsonStore({
	url: 'experiments.json?sessionGid=' + sessionGid,
	root: 'experiments',
	fields: [{
		name:'display', mapping:'name'
	},{
		name:'value', mapping:'gid'
	}]
});

var experimentFld = new Ext.form.ComboBox({
	hiddenName: 'experimentGid',
	fieldLabel: 'Experiment', 
	allowBlank: false,
	store: experimentStore,
	mode: 'remote',
	queryParam: '_dq',
	triggerAction: 'all',
	forceSelection: true,
	editable: false,
	displayField: 'display',
	valueField: 'value',
	listWidth: 100,
	width: 150
});

experimentFld.on('beforequery', function(queryEvent) {
	delete queryEvent.combo.lastQuery;
});

var origNameFld = new Ext.form.TextField({
	name: 'original',
	fieldLabel: 'Original', 
	readOnly: true,
	allowBlank : false, 
	width: 150
});

var scanNameFld = new Ext.form.TextField({
	name: 'scanName',
	fieldLabel: 'Scan name', 
	allowBlank : false, 
	width: 150
});

var importBtn = new Ext.Button({
	text: 'Import'
});
importBtn.on('click', function() {
	Ext.Ajax.request( {
        url : 'ss',
        method : 'POST',
        params : {
            scanName : scanNameFld.getValue(),
            path : all_scans.resource+'/'+scanId,
            experimentGid: experimentFld.getValue(),
            sessionGid : sessionGid
        },
//        success : function(response, options) {
//        	Ext.Msg.alert('Success', 'created scan ' + scanNameFld.getValue());
//        },
        failure : function(response, options) {
        	Ext.Msg.alert('Failure', 'cannot create scan ' + scanNameFld.getValue() + ' reason: ' + response.responseText);
        }
    });
});

var experimentFormPanel = new Ext.FormPanel({
	border : false,
    labelPad: 5,
	labelWidth: 100,
    layout : 'table',
    layoutConfig : {
        columns : 1
    },
    defaults: {
		layout: 'form',
		border: false,
		bodyStyle: 'padding: 2px'
    },
	items: [{items:[origNameFld]}, 
	        {items:[experimentFld]}, 
	        {items:[scanNameFld]}
	        ],
    buttons : {formBind: true, items: [ importBtn]} ,
    buttonAlign : 'right'
});


/***************************explore grid and scan details*/


var fromJsonStore = new Ext.data.JsonStore( {
    fields : [ {
        name : 'id'
    }, {
        name : 'lastModified',
        type : 'date',
        dateFormat : 'U'
    } ],
    root : 'scans'
});

var fromImageJsonStore = new Ext.data.JsonStore( {
    fields : [ {
        name : 'name',
        type : 'string'
    }, {
        name : 'start',
        type : 'int'
    }, {
        name : 'end',
        type : 'int'
    } , {
        name : 'type',
        type : 'string'
    }
    ],
    root : 'sets'
});

var fromGrid = new Ext.grid.GridPanel( {
    store : fromJsonStore,
    columns : [ {
        id : 'id',
        header : "scan id",
        sortable : true,
        width : 100,
        dataIndex : 'id'
    }, {
        header : "last modified",
        sortable : true,
        width : 150,
        dataIndex : 'lastModified'
    }

    ],
    autoScroll : true,
    stripeRows : true,
    viewConfig : {
        forceFit : true
    },
    enableDragDrop : false,
    height : 250,
    disabled : true,
    frame : false
});

var fromImageGrid = new Ext.grid.GridPanel( {
    border : false,
    store : fromImageJsonStore,
    columns : [ {
        id : 'name',
        header : "name",
        width : 50,
        sortable : true,
        dataIndex : 'name'
    }, {
        header : "start",
        sortable : false,
        width : 50
    }, {
        header : "end",
        sortable : false,
        width : 50
    }, {
        header : "type",
        sortable : false,
        width : 50
    }
    ],
    autoScroll : true,
    stripeRows : true,
    viewConfig : {
        forceFit : true
    },
    height : 250,
    frame : false
});

fromGrid.addListener('cellclick', function(grid, r, c, e) {
    var record = grid.getStore().getAt(r);
    var id = record.id;
    scanId = id;
    origNameFld.setValue(id);
    fromImageGrid.getStore().removeAll();
    fromScanWindow.setTitle(id + ' : ' + 'fetching');
    // fetch the scan information and update the image number and list grid
        Ext.Ajax.request( {
            url : 'origin',
            method : 'GET',
            params : {
                id : id, 
                sessionGid : sessionGid
            },
            success : function(response, options) {
                fromScanInfo = Ext.util.JSON.decode(response.responseText);
                fromScanWindow.setTitle(id + ' : ' + fromScanInfo.num_sets + ' sets');
                fromImageGrid.getStore().loadData(fromScanInfo, false);
            },
            failure : function(response, options) {
                fromScanWindow.setTitle(id + ' : ' + 'unknown');
                fromImageGrid.getStore().removeAll();
            }
        });

        if (!fromScanWindow.isVisible())
            fromScanWindow.show();
        fromScanWindow.expand(true);
        var fromPosition = fromWindow.getPosition(true);
        fromScanWindow.setPosition(fromPosition[0] + fromWindow.getWidth(), fromPosition[1]);
    });


function updateGrid(grid) {
    Ext.Ajax.request( {
        url : 'origin',
        method : 'GET',
        params : {
        	sessionGid : sessionGid
        },
        success : function(response, options) {
            grid.enable();
            all_scans = Ext.util.JSON.decode(response.responseText);
            grid.getStore().loadData(all_scans, false);

        },
        failure : function(response, options) {
            grid.disable();
            Ext.Msg.alert('Failure', 'cannot reach import server.');
            grid.getStore().removeAll();
        }
    });
}

var fromWindow = new Ext.Window( {
    title : 'Explore',
    id : 'from',
    width : 300,
    height : 300,
    draggable : true,
    closable : false,
    layout : 'fit',
    tools:[{
    	id: 'refresh', 
    	qtip:'refresh the explore grid',
    	handler : function(){
        	updateGrid(fromGrid, server);
        }
    }],
    items : [ fromGrid ]

});

var importWindow = new Ext.Window( {
    title : 'Import',
    id : 'import',
    width : 300,
    height : 150,
    draggable : true,
    closable : false,
    layout : 'fit',
    items : [ experimentFormPanel ]

});

var fromScanWindow = new Ext.Window( {
    title : 'scan on import server',
    id : 'fromScan',
    width : 300,
    height : 150,
    draggable : true,
    closable : false,
    layout : 'fit',
    collapsible : true,
    items : [ fromImageGrid ]
});

fromWindow.addListener('move', function(win, x, y) {
    if (fromScanWindow.isVisible()) {
        fromScanWindow.setPosition(x + win.getWidth(), y);
    }
    importWindow.setPosition(x, y+win.getHeight());
});

var explorePanel = new Ext.Panel( {
	title: 'Import data',
    autoScroll : true,
    height : 'auto',
    width : 'auto',
    labelAlign : 'right',
    labelWidth : 120,
    layout : 'anchor',
    frame : true,
    items : [ fromWindow, fromScanWindow, importWindow ]
});

explorePanel.addListener('afterlayout', function(panel) {
    fromWindow.show();
    fromWindow.setPosition(10, 10);
    var fromPosition = fromWindow.getPosition(true);
    importWindow.show();
    importWindow.setPosition(fromPosition[0], fromPosition[1] + fromWindow.getHeight());
    updateGrid(fromGrid);
});

