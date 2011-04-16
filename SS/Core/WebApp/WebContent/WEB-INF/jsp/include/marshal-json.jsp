<%--
	Copyright (c) Canadian Light Source, Inc. All rights reserved.
	 - see license.txt for details.

	Description:
		marshal-json jsp file.
--%>
<%@ page trimDirectiveWhitespaces="true" %>
<%@ page import="ca.sciencestudio.util.marshal.JsonMarshaller"%>
<%@ page import="org.springframework.web.context.WebApplicationContext" %>
<%@ page import="org.springframework.web.context.support.WebApplicationContextUtils" %>
<% 
	WebApplicationContext applicationContext = WebApplicationContextUtils.getRequiredWebApplicationContext(application);
	JsonMarshaller marshaller = applicationContext.getBean(JsonMarshaller.class);
%>
<%= marshaller.marshal(pageContext.findAttribute(request.getParameter("source"))) %>
