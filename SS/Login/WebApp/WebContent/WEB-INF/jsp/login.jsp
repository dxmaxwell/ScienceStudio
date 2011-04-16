<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<%@ include file="/WEB-INF/jsp/include.jsp" %>
<%--
	Copyright (c) Canadian Light Source, Inc. All rights reserved.
	 - see license.txt for details.

	Description:
		login jsp file.
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
			<td style="width:70%">
				<img src="/ssstatic/img/scstudio-logo-medium.png"/>
				<div style="height:2px;width:70%;margin-top:20px;background-color:rgb(102,153,255);"></div>
			</td>
			<td style="width:30%;">
				<img src="/ssstatic/img/blank-logo-horiz-medium.png"/>
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
				<!-- Login Form -->
				<form method="post" action="login/check">
					<table style="padding:15px;background-color:rgb(255,215,95);">
						<tr>
							<th colspan="2">
								<div style="margin-bottom:15px;text-align:center;">
									Sign in with your<br/><span style="font-weight:bold;">Canadian Light Source</span> account
								</div>
							</th>
						</tr>
						
						<spring:bind path="credentials.username">
							<tr style="vertical-align:10px;">
								<td style="text-align:right;">Username:</td>
								<td><input id="USERNAME" name="j_username" type="text" size="20" value="${status.value}"/></td>
							</tr>
							<tr>
								<td style="color:rgb(255,215,95);">_</td>
								<td style="color:red;">${status.errorMessage}</td>
							</tr>
						</spring:bind>
						
						<spring:bind path="credentials.password">
							<tr style="margin-top:5px;">
								<td style="text-align:right;">Password:</td>
								<td><input id="PASSWORD" name="j_password" type="password" size="20" value="${status.value}"/></td>
							</tr>
							<tr>
								<td style="color:rgb(255,215,95);">_</td>
								<td style="color:red;">${status.errorMessage}</td>
							</tr>
						</spring:bind>
						
						<spring:bind path="credentials.domain">
							<tr>
								<td colspan="2" style="text-align:center;">
									<c:choose>
										<c:when test="${!empty domains}">
											<select name="j_domain">
												<c:forEach items="${domains}" var="domain"> 
													<option value="${domain.key}" 
														<c:if test="${status.value == domain.key}">
															selected="selected"
														</c:if>>
														${domain.value}
													</option>
												</c:forEach>
											</select>
										</c:when>
										<c:otherwise>
											<input type="hidden" name="domain" value="${status.value}"/>
										</c:otherwise>
									</c:choose>				
								</td>
							</tr>
							<tr>
								<td colspan="2">
									<span style="color:rgb(255,215,95);">_</span>
									<span style="color:red;">${status.errorMessage}</span>
								</td>
							</tr>
						</spring:bind>
						
						<tr>
							<td colspan=2 style="text-align:center;">
								<input type="submit" value="Sign In"/>
							</td>
						</tr>
						
						<c:if test="${not empty casLoginUrl}">
							<tr>
								<td colspan=2 style="text-align:center;padding:20px 0px 0px 0px">
									Or Sign&nbsp;In using <a href="${casLoginUrl}">CAS</a>
								</td>
							</tr>
						</c:if>
					</table>
				</form>
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
