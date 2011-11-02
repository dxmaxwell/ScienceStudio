<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<%@ include file="/WEB-INF/jsp/include.jsp" %>
<%--
	Copyright (c) Canadian Light Source, Inc. All rights reserved.
	 - see license.txt for details.

	Description:
		CAS login jsp file.
--%>
<html>
<head>
	<title>Science Studio :: Login</title>
	<link rel="stylesheet" type="text/css" href="/ssstatic/js/ext-3.3/resources/css/ext-all-notheme.css"/>
	<!-- Scripts are included at the bottom of the page to improve page loading speed. -->
</head>
<body>
	<table style="width:100%;">
		<tr>
			<td style="width:60%">
				<img src="/ssstatic/img/scstudio-logo-medium.png"/>
				<div style="height:2px;width:70%;margin-top:20px;background-color:rgb(102,153,255);"></div>
			</td>
			<td style="width:40%;">
				<img src="/ssstatic/img/cls-logo-horiz-medium.png"/>
			</td>
		</tr>
		<tr>
			<td><!-- List Science Studio Features -->	
				<div style="margin-top:10px;">
					<img style="width:90px" src="/ssstatic/img/humanO2/beaker-128.png"/>
					<span style="vertical-align:40px;">
						<span style="font-weight:bold;">Experiment</span> remotely using scientific instruments.
					</span>
				</div>
				
				<div style="margin-top:10px">
					<img style="width:80px;padding-left:10px;" src="/ssstatic/img/humanO2/folder-128.png"/>
					<span style="vertical-align:40px;">
						<span style="font-weight:bold;">Organize</span> your samples and experimental data. 
					</span>	
				</div>
				
				<div style="margin-top:10px">
					<img style="width:80px;padding-left:10px;" src="/ssstatic/img/humanO2/earth-128.png"/>
					<span style="vertical-align:40px;">
						<span style="font-weight:bold;">Share</span> experimental data with your research team. 
					</span>	
				</div>
			</td>
			<td>
				<!-- Facility List -->
				<div style="font-size:large;">
					Select a Central Authentication Service
				</div>
				<c:forEach items="${facilityList}" var="facility">
				<c:if test="${not empty facility.authcUrl}">
				<div style="margin:15px 0 0 10px;font-size:x-large;">
					<div>
						<a href="<c:url value="${facility.authcUrl}/login">
									<c:param name="service" value="${service}"/>
								 </c:url>">
							${facility.longName}
						</a>
					</div>
					<div style="margin:0 0 0 10px;font-size:smaller;">
					 	${facility.location}
					</div>
				</div>
				</c:if>
				</c:forEach>
				
				<!-- Facility List -->
				<div id="COMPAT_WARNING" class="x-hidden" style="margin:20px 0px;"><!-- Compatibility Warning -->
					<img style="width:60px;" src="/ssstatic/img/humanO2/warning-128.png"/>
					<span style="vertical-align:20px;font-weight:bold;">
						<a href="http://www.mozilla.com/firefox">Firefox (3.6+)</a> is recommended.
					</span>
					<p style="font-size;smaller;">
						Other standards-compliant browsers are known to work,<br/> however only Firefox is thoroughly tested and supported.
					</p> 
				</div>
			</td>
		</tr>
	</table>
	<script type="text/javascript" src="/ssstatic/js/ext-3.3/adapter/ext/ext-base.js"></script>
	<script type="text/javascript" src="/ssstatic/js/ext-3.3/pkgs/ext-core.js"></script>
	<script type="text/javascript">
		Ext.onReady(function() {
			var compatElement = Ext.get('COMPAT_WARNING');
			if(compatElement && !Ext.isGecko3) {
				compatElement.removeClass('x-hidden');
			}
		});	
	</script>
</body>
</html>
