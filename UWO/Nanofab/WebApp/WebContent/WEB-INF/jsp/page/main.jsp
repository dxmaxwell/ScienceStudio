<%@ include file="/WEB-INF/jsp/include/taglibs.jsp"%>
<%--
	Copyright (c) Canadian Light Source, Inc. All rights reserved.
	- see license.txt for details.

	Description:
		Nanofab main JSP page.
--%>
<html>
	<head>
		<title>Science Studio :: LEO1540XB</title>
		
		<%@ include file="/WEB-INF/jsp/include/ext-css.jsp"%>
		<link rel="stylesheet" type="text/css" href="<c:url value="/css/main/formFields.css"/>" />
		<link rel="stylesheet" type="text/css" href="<c:url value="/css/main/sessionStatus.css"/>" />
		
		<%@ include file="/WEB-INF/jsp/include/ext-js.jsp"%>
		<script type="text/javascript" src="/ssstatic/js/numberUtils.js"></script>
		<script type="text/javascript" src="/ssstatic/js/activityMeter.js"></script>
		<script type="text/javascript" src="/ssstatic/js/errorHandlers.js"></script>

		<script type="text/javascript" src="<c:url value="/js/main/heartbeat.js"/>"></script>
		<script type="text/javascript" src="<c:url value="/js/main/viewport-north.js"/>"></script>
		<script type="text/javascript" src="<c:url value="/js/main/viewport-center.js"/>"></script>
		
		<script type="text/javascript">
		
			var personGid = '${personGid}';
		
			function mainPageLayout() {
				
				var viewport = new Ext.Viewport( {
			        layout: 'border',
			        items: [ northPanel, centerPanel ]
			    });
				
				startHeartbeat();
			};
			
			Ext.onReady(mainPageLayout);
		</script>
	</head>
	<body>
	</body>
</html>
