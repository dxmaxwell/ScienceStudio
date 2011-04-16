<%-- 
	Copyright (c) Canadian Light Source, Inc. All rights reserved.
	- see license.txt for details.
	
	Description:
		Error html fragment.
--%>
<%@ page language="java" contentType="text/xml; charset=UTF-8" pageEncoding="UTF-8" trimDirectiveWhitespaces="true"%>
<%@ include file="/WEB-INF/jsp/include/taglibs.jsp"%>
<div>
<script type="text/javascript">

Ext.onReady(function() {

	var errorPanel = new Ext.Panel({
	 	<c:choose>
		<c:when test="${not empty errorTitle}">
			title:'${errorTitle}',
	  	</c:when>
		<c:otherwise>
			title:'Science Studio Error',
	  	</c:otherwise>
		</c:choose>
		items: [{
		  	<c:choose>
			<c:when test="${not empty error}">
				html:'${error}'
		  	</c:when>
			<c:otherwise>
				html:'An Unspecified Error has Occurred'
		  	</c:otherwise>
			</c:choose>
		},{
		  	<c:choose>
			<c:when test="${not empty errorMessage}">
				html:'${errorMessage}'
		  	</c:when>
			</c:choose>
		}],
		border: false
	});

	addItemModelViewTab(errorPanel, true);
});
</script>
</div>
