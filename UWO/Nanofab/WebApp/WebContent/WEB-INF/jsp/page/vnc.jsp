<%@ include file="/WEB-INF/jsp/include/taglibs.jsp"%>
<%--
	Copyright (c) Canadian Light Source, Inc. All rights reserved.
	- see license.txt for details.

	Description:
		vncViewer jsp page.
--%>
<html>
<head>
<title>Science Studio :: LEO1540XB</title>
</head>
<body>
	<object codetype="application/java" archive="<c:url value="/jar/TightVncViewer.jar"/>" 
			classid="java:VncViewer.class" style="width:${vncWidth};height:${vncHeight};">
		<param name="PORT" value="${vncPort}"/>
		<param name="PASSWORD" value="${vncPassword}"/>
		<param name="Open new window" value="No"/>
	</object>	
</body>
</html>
