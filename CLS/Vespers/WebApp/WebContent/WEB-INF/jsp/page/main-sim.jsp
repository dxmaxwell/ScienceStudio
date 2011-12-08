<%--
	Copyright (c) Canadian Light Source, Inc. All rights reserved.
	- see license.txt for details.

	Description:
		Beamline simulation main JSP page.
--%>
<%@ include file="/WEB-INF/jsp/include/taglibs.jsp"%>
<html>
	<head>
		<title>Science Studio :: VESPERS Simulation</title>
		
		<%@ include file="/WEB-INF/jsp/include/ext-css.jsp"%>
		<link rel="stylesheet" type="text/css" href="<c:url value="/css/beamline/formFields.css"/>" />
		<link rel="stylesheet" type="text/css" href="<c:url value="/css/beamline/sessionStatus.css"/>" />
		
		<%@ include file="/WEB-INF/jsp/include/ext-js.jsp"%>
		<script type="text/javascript" src="/ssstatic/js/numberUtils.js"></script>
		<script type="text/javascript" src="/ssstatic/js/activityMeter.js"></script>
		<script type="text/javascript" src="/ssstatic/js/dygraph-1.2/dygraph-combined.js"></script>
		
		<script type="text/javascript" src="<c:url value="/js/beamline/heartbeat.js"/>"></script>
		<script type="text/javascript" src="<c:url value="/js/beamline/help.js"/>"></script>
		<script type="text/javascript" src="<c:url value="/js/beamline/beamline-sim.js"/>"></script>
		<script type="text/javascript" src="<c:url value="/js/beamline/experiment.js"/>"></script>
		<script type="text/javascript" src="<c:url value="/js/beamline/sampleImage.js"/>"></script>
		<script type="text/javascript" src="<c:url value="/js/beamline/vortexDetector.js"/>"></script>
		
		<script type="text/javascript" src="<c:url value="/js/beamline/viewport-north.js"/>"></script>
		<script type="text/javascript" src="<c:url value="/js/beamline/viewport-center-sim.js"/>"></script>
		
		<script type="text/javascript">
			
			var personGid = '${personGid}';
		
			function initMainPage() {
				
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
						 northPanel,
			    	     centerPanel
			    	]
			    });
				
				startHeartbeat();
			}
			
			Ext.onReady(initMainPage);
		
		</script>
		
	</head>
	<body>
	</body>
</html>
