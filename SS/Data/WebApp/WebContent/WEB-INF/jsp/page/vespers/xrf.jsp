<%-- 
	Copyright (c) Canadian Light Source, Inc. All rights reserved.
	- see license.txt for details.
	
	Description:
		VESPERS XRF main JSP page.
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" trimDirectiveWhitespaces="true"%>
<%@ include file="/WEB-INF/jsp/include/taglibs.jsp"%>
<html>
	<head>
		<title>Science Studio :: Data</title>
	
		<%@ include file="/WEB-INF/jsp/include/ext-css.jsp"%>
		<link rel="stylesheet" type="text/css" href="<c:url value="/css/vespers/xrf/formFields.css"/>" />
		
		<%@ include file="/WEB-INF/jsp/include/ext-js.jsp"%>
		
		<script type="text/javascript">
			var scanGid = '${scan.gid}';
			var scanFormData = <hmc:write source="${scan}"/>;
			
			var mapxyIPath = '<c:url value="/vespers/scan/${scan.gid}/data/mapxy/I.json"/>';
			var mapxyJPath = '<c:url value="/vespers/scan/${scan.gid}/data/mapxy/J.json"/>';
			var mcaSpectrumPath = '<c:url value="/vespers/scan/${scan.gid}/data/mca/spectrum.json"/>';
			
			var cdfmlDataPath = '<c:url value="/vespers/scan/${scan.gid}/file/data.cdfml"/>';
			var sampleImagePath = '<c:url value="/vespers/scan/${scan.gid}/file/sample.png"/>';
			
			var convertDAFtoCDFPath = '<c:url value="/vespers/scan/${scan.gid}/convert/DAF/CDF.json"/>';
			var convertCDFtoCDFMLPath = '<c:url value="/vespers/scan/${scan.gid}/convert/CDF/CDFML.json"/>';
		</script>
		
		<script type="text/javascript" src="/ssstatic/js/dygraph-1.2/dygraph-combined.js"></script>
		
		<script type="text/javascript" src="<c:url value="/js/Ext.ss.data.ScanFormPanel.js"/>"></script>
		<script type="text/javascript" src="<c:url value="/js/vespers/xrf/spectrumGraph.js"/>"></script>
		<script type="text/javascript" src="<c:url value="/js/vespers/xrf/viewport-center.js"/>"></script>
		<script type="text/javascript" src="<c:url value="/js/vespers/xrf/viewport-east.js"/>"></script>
		<script type="text/javascript" src="<c:url value="/js/vespers/xrf/conversion.js"/>"></script>
		
		<script type="text/javascript">
		
			function xrfMainLayout() {
				
				var viewport = new Ext.Viewport({
					layout: 'border',
					items:[
					    eastPanel,
					    centerPanel
					]
				});
				
				convert(convertDAFtoCDFPath, function() {}, this);
			};
		
			Ext.onReady(xrfMainLayout);
		</script>
	</head>
	<body>
	</body>
</html>
