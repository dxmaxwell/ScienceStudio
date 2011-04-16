<%@ page language="java" trimDirectiveWhitespaces="true"%>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%-- 
	Copyright (c) Canadian Light Source, Inc. All rights reserved.
	- see license.txt for details.
	
	Description:
		Error JSP page.
--%>
<%@ include file="/WEB-INF/jsp/include/taglibs.jsp"%>
<html>
<head>
	<c:choose>
		<c:when test="${!empty errorTitle}">
			<title>${errorTitle}</title>
		</c:when>
		<c:otherwise>
			<title>"Science Studio Error Page</title>
		</c:otherwise>
	</c:choose>
</head>
<body>
	<c:choose>
		<c:when test="${!empty error}">
			<h3>${error}</h3>
		</c:when>
		<c:otherwise>
			<h3>An Unspecified Error has Occurred</h3>
		</c:otherwise>
	</c:choose>
	<c:choose>
		<c:when test="${!empty errorMessage}">
			<p>${errorMessage}</p>
		</c:when>
	</c:choose>
</body>
</html>
