<%--
	Copyright (c) Canadian Light Source, Inc. All rights reserved.
	- see license.txt for details.

	Description:
		Beamline main JSP page.
--%>
<%@ include file="/WEB-INF/jsp/include/taglibs.jsp"%>
<html>
	<head>
		<title>Science Studio :: import</title>
		
		<%@ include file="/WEB-INF/jsp/include/ext-css.jsp"%>
		<link rel="stylesheet" type="text/css" href="<c:url value="/css/beamline/formFields.css"/>" />
		<link rel="stylesheet" type="text/css" href="<c:url value="/css/beamline/sessionStatus.css"/>" />
		
		<%@ include file="/WEB-INF/jsp/include/ext-js.jsp"%>
		<script type="text/javascript" src="/ssstatic/js/numberUtils.js"></script>
		<script type="text/javascript" src="/ssstatic/js/canvastext.js"></script>
		<script type="text/javascript">
			
			var sessionGid = '${sessionGid}';
		
			function initMainPage() { 
				
				<sec:authorize ifContainsNone="FACILITY_ADMIN_SESSIONS">
				Ext.Msg.alert("Access problem", "Your current role does not allow to access the resource.", function() {
					var centerTabPanel = Ext.getCmp('CENTER_TAB_PANEL');
					if (centerTabPanel) {
						var labViewPanelId = 'LAB_VIEW_PANEL_' + sessionGid;
						var labViewPanel = centerTabPanel.findById(labViewPanelId);
						if(labViewPanel) {
							centerTabPanel.remove(labViewPanel, true);
							return;
						}
					} else {
						// it is a new window 
						window.close();
						return; // not needed? 
					}
					 
				});
				</sec:authorize>
				
				Ext.QuickTips.init();
				
				Ext.apply(Ext.QuickTips.getQuickTip(), {
					maxWidth: 200,
					minWidth: 100,
					showDelay: 2000,
					trackMouse: false
				});
				
				var viewport = new Ext.Viewport({
			        layout:'border',
			    	items:[
			    	     centerPanel
			    	]
			    });
				
				
				
			}
			
			Ext.onReady(initMainPage);
		
		</script>
		<script type="text/javascript" src="<c:url value="/js/explore.js"/>"></script>
		<script type="text/javascript" src="<c:url value="/js/viewport-center.js"/>"></script>
		
	</head>
	<body>
	</body>
</html>
