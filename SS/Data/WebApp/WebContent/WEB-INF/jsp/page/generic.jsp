<%@ page language="java" trimDirectiveWhitespaces="true"%>
<%@ page  contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%-- 
	Copyright (c) Canadian Light Source, Inc. All rights reserved.
	- see license.txt for details.
	
	Description:
		Nanofab main JSP page.
--%>
<%@ include file="/WEB-INF/jsp/include/taglibs.jsp"%>
<html>
	<head>
		<title>Science Studio :: Data</title>
	
		<%@ include file="/WEB-INF/jsp/include/ext-css.jsp"%>
		<link rel="stylesheet" type="text/css" href="<c:url value="/css/generic/formFields.css"/>" />
		
		<%@ include file="/WEB-INF/jsp/include/ext-js.jsp"%>
		
		<script type="text/javascript">
			var scanGid = '${scan.gid}';
			var scanFormData = <hmc:write source="${scan}"/>;
			var genericServletPath = '<c:url value="/generic"/>';
		</script>
		
		<script type="text/javascript" src="<c:url value="/js/Ext.ss.data.ScanFilePanel.js"/>"></script>
		<script type="text/javascript" src="<c:url value="/js/Ext.ss.data.ScanFormPanel.js"/>"></script>
		<script type="text/javascript" src="<c:url value="/js/generic/viewport-center.js"/>"></script>
		<script type="text/javascript" src="<c:url value="/js/generic/viewport-east.js"/>"></script>
		
		<script type="text/javascript">
		
			function mainLayout() {
				
				var viewport = new Ext.Viewport({
					layout: 'border',
					items:[
					    eastPanel,
					    centerPanel
					]
				});
			};
		
			Ext.onReady(mainLayout);
		</script>
	</head>
	<body>
	</body>
</html>
