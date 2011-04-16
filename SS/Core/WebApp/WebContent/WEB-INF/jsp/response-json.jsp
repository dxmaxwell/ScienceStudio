<%-- 
	Copyright (c) Canadian Light Source, Inc. All rights reserved.
	- see license.txt for details.
	
	Description:
		Response JSON format.
 --%>
<%@ page language="java" contentType="application/json; charset=UTF-8" pageEncoding="UTF-8" trimDirectiveWhitespaces="true" %>
<%@ include file="/WEB-INF/jsp/include/taglibs.jsp"%>
{
	<c:choose>
	<c:when test="${empty response}">
	"response":{},
	</c:when>
	<c:otherwise>
	"response":
		<jsp:include page="/WEB-INF/jsp/include/marshal-json.jsp" flush="true">
			<jsp:param  name="source" value="response"/>
		</jsp:include>,
	</c:otherwise>
	</c:choose>
	<c:choose>
	<c:when test="${not empty errors.fieldErrors or not empty errors.globalErrors}">	
	"success":false,
	"errors":{
		<c:forEach items="${errors.fieldErrors}" var="error" varStatus="status">
			"<spring:message text="${error.field}" javaScriptEscape="true"/>":"<spring:message code="${error.code}" text="${error.defaultMessage}" javaScriptEscape="true"/>"<c:if test="${not status.last}">,</c:if>
		</c:forEach>
	},
	"globalErrors": [
		<c:forEach items="${errors.globalErrors}" var="error" varStatus="status">
			"<spring:message code="${error.code}" text="${error.defaultMessage}" javaScriptEscape="true"/>"<c:if test="${not status.last}">,</c:if>
		</c:forEach>
	]
	</c:when>
	<c:otherwise>
	"success":true
	</c:otherwise>
	</c:choose>
}
