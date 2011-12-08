<%-- 
	Copyright (c) Canadian Light Source, Inc. All rights reserved.
	- see license.txt for details.
	
	Description:
		Include Ext JS framework.
--%>
<%@ page trimDirectiveWhitespaces="true" %>
<script type="text/javascript" src="/ssstatic/js/ext-3.3/adapter/ext/ext-base.js"></script>
<script type="text/javascript" src="/ssstatic/js/ext-3.3/ext-all.js"></script>

<script type="text/javascript">
	Ext.BLANK_IMAGE_URL = '/ssstatic/js/ext-3.3/resources/images/default/s.gif';
	
	Date.patterns = {
	    ISO8601Full:"Y-m-d H:i:s.uO",
	    ISO8601Long:"Y-m-d H:i:s",
	    ISO8601Shrt:"Y-m-d H:i",
	    ISO8601Date:"Y-m-d",
	    ISO8601Short:"Y-m-d",
	    ISO8601TimeFull:"H:i:s.uO",
	    ISO8601TimeLong:"H:i:s",
	    ISO8601TimeShrt:"H:i"
	};
	
	Date.altFormats =  
		Date.patterns.ISO8601Full + '|' +
		Date.patterns.ISO8601Long + '|' +
		Date.patterns.ISO8601Shrt + '|' +
		Date.patterns.ISO8601Date;
</script>