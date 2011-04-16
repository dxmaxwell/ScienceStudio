<%-- 
	Copyright (c) Canadian Light Source, Inc. All rights reserved.
	- see license.txt for details.
	
	Description:
		newRegistration JSP page.
		
		Depreciated, but useful for future development.
--%>
<%@ include file="/WEB-INF/jsp/include/taglibs.jsp"%>
<html>
	<head>
		<title>Science Studio Registration</title>
		<%@ include file="/WEB-INF/jsp/include/ext-css.jsp"%>
		<link rel="stylesheet" type="text/css" href="/ss/css/mainPage.css" />
		
		<%@ include file="/WEB-INF/jsp/include/ext-js.jsp"%>
		<script type="text/javascript" src="/ss/js/Ext.ss.cls.XmlErrorReader.js"></script>
		<script type="text/javascript" src="/ss/js/Ext.ss.cls.NewRegistrationFormPanel.js"></script>
		<script>
			function layout() {
				var registrationFrom = new Ext.ss.cls.NewRegistrationFormPanel('${successUrl}', {
					title: 'Science Studio Registration',
					width: 500,
					height: 'auto',
					applyTo: 'mainPanel'
				});
			};
			Ext.onReady(layout);
		</script>
	
	</head>
	<body>
		<div id="mainPanel"></div>
		<a style="padding:10px;" href="${successUrl}">Back</a>
	</body>
</html>
