<%-- 
	Copyright (c) Canadian Light Source, Inc. All rights reserved.
	- see license.txt for details.
	
	Description:
		Person html fragment.
--%>
<%@ page language="java"  contentType="text/xml; charset=UTF-8" pageEncoding="UTF-8" trimDirectiveWhitespaces="true"%>
<%@ include file="/WEB-INF/jsp/include/taglibs.jsp"%>
<div>
<script type="text/javascript">
Ext.onReady(function() {
<%--

	Contact Administrator Message
--%>
	var messagePanel = new Ext.Panel({
		title:"Message",
		html: "Please contact the Science Studio administrator to update your personal information.",
		bodyStyle: { "padding":"5px" },
		style: { "margin":"10px" },
		width: 700
	});

	addItemModelViewTab(messagePanel);
<%--

	Edit Person Form
--%>
	var personFormPanel = new Ext.ss.core.PersonFormPanel({
		url: ModelPathUtils.getPersonPath('self') + '/form/edit.json',
		method: 'POST',
		defaults: {
			disabled:true,
		},
		buttonDefaults: {
			hidden:true
		},
		submitText: 'Save',
		labelAlign: 'right',
		buttonAlign: 'center',
		waitMsg:'Saving project...',
		waitMsgTarget: true,
		border: false
	});

	<c:if test="${not empty personFormBacker}">
	var personFormValues = 
		<jsp:include page="/WEB-INF/jsp/include/marshal-json.jsp" flush="true">  
			<jsp:param name="source" value="personFormBacker"/>
		</jsp:include>;
	
	personFormPanel.getForm().setValues(personFormValues.personFormBacker);
	</c:if>

	var panel = new Ext.Panel({
		title:'Person',
		items: [ personFormPanel ],
		style: { "margin":"10px" },
		width: 400
	});

	addItemModelViewTab(panel, true);
});
</script>
</div>