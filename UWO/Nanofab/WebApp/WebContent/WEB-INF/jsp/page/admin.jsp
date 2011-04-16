<%@ include file="/WEB-INF/jsp/include/taglibs.jsp"%>
<%--
	Copyright (c) Canadian Light Source, Inc. All rights reserved.
	- see license.txt for details.
	
	Description:
		mainPage jsp page.
--%>
<html>
	<head>
		<title>${htmlHeaderTitle}</title>
	
		<%@ include file="/WEB-INF/jsp/include/ext-css.jsp"%>
		<link rel="stylesheet" type="text/css" href="<c:url value="/css/admin/formFields.css"/>" />
		
		<%@ include file="/WEB-INF/jsp/include/ext-js.jsp"%>
		
		<script type="text/javascript">
			var pageHeaderTitle = '${pageHeaderTitle}';
		</script>
		
		<script type="text/javascript" src="<c:url value="/js/admin/viewport-center.js"/>"></script>
		<script type="text/javascript" src="<c:url value="/js/admin/viewport-north.js"/>"></script>
		
		<script type="text/javascript">
		
			function mainPageLayout() {
				
				Ext.QuickTips.init();
	
				var viewport = new Ext.Viewport({
					layout:'border',
					items:[
					    northPanel,
					    centerPanel
					]
				});
			}
			
			Ext.onReady(mainPageLayout);
		</script>
		
	</head>
	<body>
	</body>
</html>
