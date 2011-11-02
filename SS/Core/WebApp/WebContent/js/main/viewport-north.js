/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *     viewport-west.js
 *     
 */

function northPanel() {
		
	var northPanel = new Ext.Panel({
    	region:'north',
    	defaults: {
    		border:false
    	},
    	layout: { 
    		type:'hbox',
    		align:'top'
    	},
    	items: [{
			xtype:'box',
    		autoEl: {
    			tag: 'img',
    			src: '/ssstatic/img/scstudio-logo-small.png'
    		}
		},{
			xtype:'box',
			flex:1
		},{
			xtype:'panel',
			defaults: {
				border:false
			},
			items: [{
				xtype:'box',
				html: 'Welcome: <span style="font-weight:bold;">' + username + '</span>'
			},{
				xtype:'panel',
				layout:'hbox',
				items: [{
					xtype:'box',
					flex:0.3
				},{
					xtype:'box',
					flex:1,
					autoEl: {
						tag: 'a',
						href: '#dummy',
						html: 'Profile',
						onclick: "return loadModelViewTab(ModelPathUtils.getPersonPath('/self.html'));"
					}
				},{
					xtype:'box',
					flex:1,
					autoEl: {
						tag: 'a',
						href: '/sslogin/authc/logout',
						html: 'Logout'
					}
				}]
			}],
			padding:'10px 5px'
		}],
		style: { 'font-size':'smaller' },
		margins:'4 4 4 4',
		height:60
    });
	
	return northPanel;
}