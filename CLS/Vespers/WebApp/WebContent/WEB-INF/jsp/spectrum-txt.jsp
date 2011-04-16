<%-- 
	Copyright (c) Canadian Light Source, Inc. All rights reserved.
	- see license.txt for details.
	
	Description:
		spectrum-txt JSP page.
		
		Eclipse reports a validation error for this file. (Why??)
--%>
<%@ page language="java" contentType="text/plain; charset=UTF-8" pageEncoding="UTF-8" trimDirectiveWhitespaces="true" %>
<%@ include file="/WEB-INF/jsp/include/taglibs.jsp" %>
<c:forEach items="${spectrum}" var="value">
${value}${'
'}
</c:forEach>