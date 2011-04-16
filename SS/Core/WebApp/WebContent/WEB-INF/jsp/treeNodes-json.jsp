<%-- 
	Copyright (c) Canadian Light Source, Inc. All rights reserved.
	- see license.txt for details.
	
	Description:
		treeNodes-json JSP include.
--%>
<%@ page language="java"  contentType="application/json; charset=UTF-8" pageEncoding="UTF-8" trimDirectiveWhitespaces="true"%>
<%@ include file="/WEB-INF/jsp/include/taglibs.jsp" %>
<c:choose>
<c:when test="${empty treeNodes}">
[]
</c:when>
<c:otherwise>
<jsp:include page="/WEB-INF/jsp/include/marshal-json.jsp" flush="true">
	<jsp:param  name="source" value="treeNodes"/>
</jsp:include>
</c:otherwise>
</c:choose>
