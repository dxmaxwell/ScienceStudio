<%-- 
	Copyright (c) Canadian Light Source, Inc. All rights reserved.
	- see license.txt for details.
	
	Description:
		convertXRFDAFtoCDFML JSP page.
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" trimDirectiveWhitespaces="true"%>
<%@ include file="/WEB-INF/jsp/include/taglibs.jsp"%>
<html>
<head>
	<title>VESPERS XRF Conversion Utility</title>
	
	<script type="text/javascript">

	function clearErrorMessage() {
		var errorMessage = document.getElementById('errorMessage');
		if(errorMessage) { errorMessage.innerHTML = ''; }
	};
	
	</script>
	
</head>
<body>
	<div>
		<table style="width:100%;">
			<tr>
				<td style="width:33%;">
					<img src="../../img/scstudio_small.png"/>
				</td>
				<td style="width:34%;text-align:center;">
					<span style="font-size:x-large;font-weight:bold;">
						Convert XRF Raw Data to CDFML
					</span>
				</td>
				<td style="width:33%;">
				</td>
			</tr>
		</table>
	</div>
	
	<div>
		<form method="POST" enctype="multipart/form-data" onsubmit="clearErrorMessage();">
			<table style="width:650px;margin-left:auto;margin-right:auto;margin-top:50px;">
				<tr>
					<td style="text-align:right;">Project</td>
					<td><input name="projectName" type="text" size="30"
						<c:if test="${!empty projectName}">value="${projectName}"</c:if>
					/></td>
				</tr>
				<tr>
					<td style="text-align:right;">Session</td>
					<td><input name="sessionName" type="text" size="30"
						<c:if test="${!empty sessionName}">value="${sessionName}"</c:if>
					/></td>
				</tr>
				<tr>
					<td style="text-align:right;">Experiment</td>
					<td><input name="experimentName" type="text" size="30"
						<c:if test="${!empty experimentName}">value="${experimentName}"</c:if>
					/></td>
				</tr>
				<tr>
					<td style="text-align:right;">Sample</td>
					<td><input name="sampleName" type="text" size="30"
						<c:if test="${!empty sampleName}">value="${sampleName}"</c:if>
					/></td>
				</tr>
				<tr>
					<td style="text-align:right;">Scan</td>
					<td><input name="scanName" type="text" size="30"
						<c:if test="${!empty scanName}">value="${scanName}"</c:if>
					/></td>
				</tr>
				<tr>
					<td style="text-align:right;">Data File</td>
					<td><input name="dafDataFile" type="file" size="50"/></td>
				</tr>
				<tr>
					<td style="text-align:right;">Spectra File</td>
					<td><input name="dafSpecFile" type="file" size="50"/></td>
				</tr>
				<tr>
					<td colspan="2" id="errorMessage" style="text-align:center;color:red;font-weight:bold;">
						<c:if test="${!empty errorMessage}">${errorMessage}</c:if>
					</td>
				</tr>
				<tr>
					<td colspan="2" style="text-align:center;">
						<input type="submit" value="Convert"/>
					</td>
				</tr>	
			</table>
		</form>
	</div>
</body>
</html>