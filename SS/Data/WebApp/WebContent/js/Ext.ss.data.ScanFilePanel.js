/**
 * Copyright (c) Canadian Light Source, Inc. All rights reserved. - see
 * license.txt for details.
 *
 * Description:
 * 		ScanFilePanel.js
 *
 */

Ext.namespace('Ext.ss.data');

Ext.ss.data.ScanFilePanel = function(config) {
	
	Ext.form.Field.prototype.msgTarget = 'side';
	
	var filePanelConfig = Ext.apply({}, config);
	
	var fileListUrl = 'files.json';
	if(filePanelConfig.fileListUrl) {
		fileListUrl = filePanelConfig.fileListUrl;
		delete filePanelConfig.fileListUrl;
	}
	
	var downloadUrl = 'download';
	if(filePanelConfig.downloadUrl) {
		downloadUrl = filePanelConfig.downloadUrl;
		delete filePanelConfig.downloadUrl;
	}
	
	var directoryStore = null;
	if(filePanelConfig.directoryStore) {
		directoryStore = filePanelConfig.directoryStore;
		delete filePanelConfig.directoryStore;
	}
	
	if(!directoryStore) {
		directoryStore = new Ext.data.JsonStore({
			url: fileListUrl,
			baseParams: {
				'type':'dir',
				'depth':100
			},
			root:'response',
			successProperty:'success',
			messageProperty:'globalError',
			fields:[
			    { name:'name', mapping:'fileProperties.name' },
			    { name:'path', mapping:'fileProperties.path' },
			    { name:'size', mapping:'fileProperties.size' }
			],
			listeners:{
				'exception':this.ss.handlers.dataStoreExceptionHandler
			}
		});
	}
	
	this.ss.fields.directory = new Ext.form.ComboBox({
		fieldLabel: 'Directory',
		store: directoryStore,
		emptyText: 'Select a Directory',
		mode: 'remote',
		editable:false,
		triggerAction: 'all',
		forceSelection: true,
		valueField: 'path',
		displayField: 'path',
		width: 250,
		listeners:{
			'beforequery':function(queryEvent) {
				delete queryEvent.combo.lastQuery;
			},
			'select': function(cbx, record, idx ) {
				var path = record.get('path');
				if(path) {
					fileStore.load({
						params: { 
							'dir':path 
						},
						add: false
					});
				}
			},
			scope:this
		}
	});
	
	this.ss.buttons.refresh = new Ext.Button({
		text:'Refresh',
		handler:function() {
			if(fileStore.lastOptions) {
				fileStore.reload();
			}
		},
		scope:this
	});
	
	var fileStore = null;
	if(filePanelConfig.fileStore) {
		fileStore = filePanelConfig.fileStore;
		delete filePanelConfig.fileStore;
	}
	
	if(!fileStore) {
		fileStore = new Ext.data.JsonStore({
			url: fileListUrl,
			baseParams:{
				'type':'file'
			},
			root: 'response',
		    successProperty: 'success',
		    messageProperty: 'globalError',
		    fields: [
		        { name:'name', mapping:'fileProperties.name' },
		        { name:'path', mapping:'fileProperties.path' },
		        { name:'size', mapping:'fileProperties.size' }
		    ],
		    sortInfo: { 
				field: 'name',
				direction: 'ASC'
			},
			listeners:{
				'exception':this.ss.handlers.dataStoreExceptionHandler
			}
		});
		delete filePanelConfig.fileUrl;
	}
	
	this.ss.buttons.addAll = new Ext.Button({
		text:'Add All',
		handler:function() {
			fileStore.each(function(record) {
				var path = record.get('path');
				var query = selFileStore.query('path', path);
				if(query.getCount() == 0) {	
					selFileStore.addSorted(record);
				} 
			}, this);
			selFileStore.commitChanges();
		},
		scope:this
	});
	
	var fileGridSM = new Ext.grid.RowSelectionModel({
		singleSelect:true,
		listeners:{
			'rowselect':function(sm, idx, record) {
				var path = record.get('path');
				var query = selFileStore.query('path', path);
				if(query.getCount() == 0) {	
					selFileStore.addSorted(record);
					selFileStore.commitChanges();
				} else {
					Ext.Msg.alert('Message', 'File has already been selected.');
				}
				sm.clearSelections();
			},
			scope:this	
		}
	});
	
	var fileGridPanel = new Ext.grid.GridPanel({
		sm: fileGridSM,
		store: fileStore,
		columns: [{
			header: 'Name',
			dataIndex: 'name',
			sortable: true
		}, { 
			header: 'Size',
			dataIndex: 'size',
			sortable: false,
			width: 25
	    }],
	    viewConfig: {
	        forceFit: true
		},
		buttons:[
		    this.ss.buttons.addAll
		],
		buttonAlign:'left',
		flex:1
	});
	
	var leftPanel = new Ext.Panel({
		title: 'Scan Data Files',
		layout:{
			type:'vbox',
			align:'stretch'
		},
		items: [{
			layout:'hbox',
			items: [{
				layout:'form',
				labelPad:10,
				labelWidth:50,
				items:[ this.ss.fields.directory ],
				margins:'5px',
				border:false
			},{
				items:[ this.ss.buttons.refresh ],
				margins:'5px',
				border:false
			}]
		},
			fileGridPanel
		],
		flex:1
	});
	
	var selFileStore = new Ext.data.ArrayStore({
		fields: [
		    'name', 'path', 'size'
		],
		sortInfo: { 
			field: 'path',
			direction: 'ASC'
		}
	});
	
	this.ss.buttons.removeAll = new Ext.Button({
		text:'Remove All',
		handler:function() {
			selFileStore.removeAll();
			selFileStore.commitChanges();
		},
		scope:this
	});
	
	this.ss.buttons.download = new Ext.Button({
		text:'Download',
		handler:function() {
			var records = selFileStore.getRange();
			if(records.length == 0) {
				Ext.Msg.alert('Information', 'Please select at least one data file.');
				return;
			}
			
			var html = '<html><head><title>Science Studio :: Download</title></head><body>';
			html += 'Download should begin automatically, if not then click ';
			html += '<a href="#" onclick="document.forms[0].submit();return false;">here</a>';
			html += '<form method="POST" action="' + downloadUrl + '">';
			for(var idx=0; idx<records.length; idx++) {			
				html += '<input type="hidden" name="paths" value="' + records[idx].get('path') + '"/>';
			}
			html += '</form></body></html>';
			
			var w = window.open('');
			if(w) {
				w.document.write(html);
			} else {
				Ext.Msg.alert('Error', 'Unable to open download window.');
				return;
			}
			
			if(w.document.forms.length > 0) {
				w.document.forms[0].submit();
			} else {
				Ext.Msg.alert('Error', 'Unable to submit download form.');
				return;
			}
		},
		scope:this
	});
	
	var selFileSM = new Ext.grid.RowSelectionModel({
		singleSelect: true,
		listeners:{
			'rowselect': function(sm, idx, record) {
				selFileStore.remove(record);
				selFileStore.commitChanges();
				sm.clearSelections();
			},
			scope:this
		}
	});
	
	var selFileGridPanel = new Ext.grid.GridPanel({
		sm: selFileSM,
		store: selFileStore,
		columns: [{
			header: 'Path',
			dataIndex: 'path',
			sortable: true 
		},{ 
			header: 'Size',
			dataIndex: 'size',
			sortable: false,
			width: 25
	    }],
	    viewConfig: {
	        forceFit: true
		},
		buttons:[
            this.ss.buttons.removeAll,
            '->', this.ss.buttons.download
		],
		buttonAlign:'left',
		flex:1
	});
	
	var rightPanel = new Ext.Panel({
		title: 'Selected Data Files',
		layout:{
			type:'hbox',
			align:'stretch'
		},
		items:[
		    selFileGridPanel
		],
		margins:'0px 5px',
		flex:1
	});
	
	filePanelConfig.layout = {
		type:'hbox',
		align:'stretch'
	};
	
	filePanelConfig.items = [
		leftPanel,
		rightPanel
	];
	
	Ext.ss.data.ScanFilePanel.superclass.constructor.call(this, filePanelConfig);
};

Ext.extend(Ext.ss.data.ScanFilePanel, Ext.Panel, {
	
	ss: {
		fields: {},
		buttons: {},
		handlers: {
			dataStoreExceptionHandler: function(dataProxy, type, action, options, response, arg) {
				if(type == 'response') {
					Ext.Msg.alert('Error', 'Network connection problem.');
				}
				else { 
					if(response && response.message) {
						Ext.Msg.alert('Message', response.message);
					}
					else {
						Ext.Msg.alert('Error', 'An unspecified error has occurred.');
					}
				}
			}
		}
	}
});
