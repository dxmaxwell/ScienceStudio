<%@ include file="/WEB-INF/jsp/include.jsp"%>
<%--
	Copyright (c) Canadian Light Source, Inc. All rights reserved.
	 - see license.txt for details.

	Description:
		CAS logout JSP file.
--%>
<html>
	<head>
		<title>Science Studio :: Logout</title>
		<link rel="stylesheet" type="text/css" href="/ssstatic/js/ext-3.3/resources/css/ext-all-notheme.css"/>
		<style type="text/css">
			p {
				margin:30px 0px;
				text-align:center;
				font-size: large;
			}
			
			div {
				margin:100px auto;
				width:600px;
				border-style:solid;
				border-color:rgb(102,153,255);
				border-width:2px;
				/*background-color:rgb(255, 247, 223);*/
			}
		</style>
		<!-- Scripts are included at the bottom of the page to improve page loading speed. -->
	</head>
	<body onload="cleanUp(); redirect();"  >
		<div>
			<p>
				<img src="/ssstatic/img/scstudio-logo-medium.png"/>
			</p>
			<p>
				Thank you for using Science Studio.
			</p>
			<p style="font-weight:bold;">
				Click here to <a href="<c:url value="${loginUrl}">
									<c:if test="${not empty authenticator}">
										<c:param name="authc" value="${authenticator}"/>
									</c:if>
								</c:url>">Sign&nbsp;In</a> again.
			</p>
			<p>
				Or Sign Out of the Central Authentication Service
				<c:forEach items="${facilityList}" var="facility">
				<c:if test="${not empty facility.authcUrl}">
				<p style="font-size:x-large;">
					<a href="<c:url value="${facility.authcUrl}/logout">
								<c:param name="service" value="${loginUrl}"/>
							</c:url>">
						${facility.longName}
					</a>
				</p>
				</c:if>
				</c:forEach>
			</p>
		</div>
	
		<script type="text/javascript">
			function redirect(page, timeout) {
				setTimeout('window.location = "${redirectUrl}";', 600000);
			}
		
			function cleanUp() {
				deleteCookie('${cookieName}', '${cookiePath}');
			}
		
			function deleteCookie(name, path) {
				if(getCookie(name)) {
					document.cookie = name + '=;path=' + path + ';expires=Thu, 01-Jan-1970 00:00:01 GMT';
				}
			}
			
			function getCookie(name) {
				var start = document.cookie.indexOf(name + '=');
				var len = start + name.length + 1;
				if((!start) && (name != document.cookie.substring(0, name.length))) {
					return null;
				}
				if(start == -1)
					return null;
				var end = document.cookie.indexOf(';', len);
				if(end == -1)
					end = document.cookie.length;
				return unescape(document.cookie.substring(len, end));
			}
		</script>
	</body>
</html>
