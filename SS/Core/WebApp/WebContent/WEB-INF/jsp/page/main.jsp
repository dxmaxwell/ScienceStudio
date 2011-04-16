<%@ page language="java" trimDirectiveWhitespaces="true"%>
<%@ page  contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%-- 
	Copyright (c) Canadian Light Source, Inc. All rights reserved.
	- see license.txt for details.
	
	Description:
		Science Studio main page.
--%>
<%@ include file="/WEB-INF/jsp/include/taglibs.jsp"%>
<html>
<head>
	<%@ include file="/WEB-INF/jsp/include/ext-css.jsp" %>
	<link rel="stylesheet" type="text/css" href="<c:url value="/css/main/treeNodes.css"/>" />
	<link rel="stylesheet" type="text/css" href="<c:url value="/css/main/formFields.css"/>" />
	<link rel="stylesheet" type="text/css" href="<c:url value="/css/main/navTrail.css"/>" />
	
	<%@ include file="/WEB-INF/jsp/include/ext-js.jsp" %>
	<%@ include file="/WEB-INF/jsp/include/ext-core-js.jsp" %>
	
	<script type="text/javascript" src="<c:url value="/js/main/modelPathUtils.js"/>"></script>
	<script type="text/javascript" src="<c:url value="/js/main/tabPanelUtils.js"/>"></script>
	
	<script type="text/javascript" src="<c:url value="/js/main/viewport-center.js"/>"></script>
	<script type="text/javascript" src="<c:url value="/js/main/viewport-north.js"/>"></script>
	<script type="text/javascript" src="<c:url value="/js/main/viewport-west.js"/>"></script>

	<!-- Third-party JS source -->
	<script type="text/javascript" src="http://download.skype.com/share/skypebuttons/js/skypeCheck.js"></script>
	
	<script type="text/javascript">
	
		var username = '${username}';
	
		function initMainViewport() {
		  
			// Init Tool Tips //
			Ext.QuickTips.init();

			// Configure viewport //
			var mainViewPort = new Ext.Viewport({
		        layout:'border',
		    	items:[
					 northPanel(),
		    	     westPanel(),
		    	     centerPanel()
		    	]
		    });
		}
		
		Ext.onReady(initMainViewport);
	</script>
	 
	<title>Science Studio</title>
</head>
<body>
</body>
</html>
