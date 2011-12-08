<%--
	Copyright (c) Canadian Light Source, Inc. All rights reserved.
	- see license.txt for details.

	Description:
		Beamline main JSP page.
--%>
<%@ include file="/WEB-INF/jsp/include/taglibs.jsp"%>
<html>
	<head>
		<title>Science Studio :: VESPERS</title>
		
		<%@ include file="/WEB-INF/jsp/include/ext-css.jsp"%>
		<link rel="stylesheet" type="text/css" href="<c:url value="/css/beamline/formFields.css"/>" />
		<link rel="stylesheet" type="text/css" href="<c:url value="/css/beamline/sessionStatus.css"/>" />
		
		<%@ include file="/WEB-INF/jsp/include/ext-js.jsp"%>
		<script type="text/javascript" src="/ssstatic/js/numberUtils.js"></script>
		<script type="text/javascript" src="/ssstatic/js/activityMeter.js"></script>
		<script type="text/javascript" src="/ssstatic/js/dygraph-1.2/dygraph-combined.js"></script>
		
		<script type="text/javascript" src="<c:url value="/js/Ext.ss.cls.AxisPTZCameraPanel.js"/>"></script>
		<!--<script type="text/javascript" src="/ssvespers/js/Ext.ux.CCDImageCanvas.js"></script>-->
		
		<script type="text/javascript" src="<c:url value="/js/beamline/heartbeat.js"/>"></script>
		<script type="text/javascript" src="<c:url value="/js/beamline/help.js"/>"></script>
		<script type="text/javascript" src="<c:url value="/js/beamline/cameras.js"/>"></script>
		<script type="text/javascript" src="<c:url value="/js/beamline/beamline.js"/>"></script>
		<script type="text/javascript" src="<c:url value="/js/beamline/experiment.js"/>"></script>
		<script type="text/javascript" src="<c:url value="/js/beamline/sampleImage.js"/>"></script>
		<script type="text/javascript" src="<c:url value="/js/beamline/fourElementDetector.js"/>"></script>
		
		<script type="text/javascript" src="<c:url value="/js/beamline/viewport-north.js"/>"></script>
		<script type="text/javascript" src="<c:url value="/js/beamline/viewport-center.js"/>"></script>
		
		<!--<script type="text/javascript" src="/ssvespers/js/vortexDetectorPanels.js"></script>-->
		<!--<script type="text/javascript" src="/ssvespers/js/ccdDetectorPanels.js"></script>-->
		<!--<script type="text/javascript" src="/ssvespers/js/deviceFunctions.js"></script>-->
		<!--<script type="text/javascript" src="/ssvespers/js/taskRunners.js"></script>-->
		<!--<script type="text/javascript" src="/ssvespers/js/canvastext.js"></script>-->
		
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
