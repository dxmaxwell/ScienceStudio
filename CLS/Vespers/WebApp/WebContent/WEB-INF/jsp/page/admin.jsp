<%--
	Copyright (c) Canadian Light Source, Inc. All rights reserved.
	- see license.txt for details.
	
	Description:
		admin.jsp
--%>
<%@ include file="/WEB-INF/jsp/include/taglibs.jsp"%>
<html>
	<head>
		<title>${htmlHeaderTitle}</title>
	
		<%@ include file="/WEB-INF/jsp/include/ext-css.jsp"%>
		<link rel="stylesheet" type="text/css" href="<c:url value="/css/admin/formFields.css"/>" />
		
		<script type="text/javascript">
			var pageHeaderTitle = '${pageHeaderTitle}';
		</script>
		
		<%@ include file="/WEB-INF/jsp/include/ext-js.jsp"%>
		
		<script type="text/javascript" src="/ssstatic/js/activityMeter.js"></script>
		
		<script type="text/javascript" src="<c:url value="/js/admin/heartbeat.js"/>"></script>
		<script type="text/javascript" src="<c:url value="/js/admin/viewport-south.js"/>"></script>
		<script type="text/javascript" src="<c:url value="/js/admin/viewport-north.js"/>"></script>
		<script type="text/javascript" src="<c:url value="/js/admin/viewport-center.js"/>"></script>
		
		<script type="text/javascript">
		
			function initMainViewport() {
			  
				// Configure viewport //
				var mainViewPort = new Ext.Viewport({
			        layout:'border',
			    	items:[
						northPanel,
			    	    centerPanel,
			    	    southPanel
			    	]
			    });
				
				startHeartbeat();
			}
			
			Ext.onReady(initMainViewport);
		</script>
	</head>
	<body>
	</body>
</html>
