<%-- 
	Copyright (c) Canadian Light Source, Inc. All rights reserved.
	- see license.txt for details.
	
	Description:
		Include Ext JS framework.
--%>
<%@ page trimDirectiveWhitespaces="true" %>
<script type="text/javascript" src="/ssstatic/js/ext-3.4/adapter/ext/ext-base.js"></script>
<script type="text/javascript" src="/ssstatic/js/ext-3.4/ext-all.js"></script>
<!-- <script type="text/javascript" src="/ssstatic/js/ext-3.4/adapter/ext/ext-base-debug.js"></script> -->
<!-- <script type="text/javascript" src="/ssstatic/js/ext-3.4/ext-all-debug.js"></script> -->

<script type="text/javascript" src="/ssstatic/js/ext-3.4/ux/Ext.ux.Portal.js"></script>
<script type="text/javascript" src="/ssstatic/js/ext-3.4/ux/Ext.ux.PortalColumn.js"></script>
<script type="text/javascript" src="/ssstatic/js/ext-3.4/ux/Ext.ux.Portlet.js"></script>

<script type="text/javascript">
	Ext.BLANK_IMAGE_URL = '/ssstatic/js/ext-3.4/resources/images/default/s.gif';
	
	Date.patterns = {
	    ISO8601Full:"Y-m-d H:i:s.u T",
	    ISO8601Long:"Y-m-d H:i:s",
	    ISO8601Shrt:"Y-m-d H:i",
	    ISO8601Date:"Y-m-d",
	    ISO8601Short:"Y-m-d",
	    ISO8601TimeFull:"H:i:s.u T",
	    ISO8601TimeLong:"H:i:s",
	    ISO8601TimeShrt:"H:i"
	};
	
	Date.altFormats = Date.altDateFormats = 'c|m/d/Y|n/j/Y|n/j/y|m/j/y|n/d/y|m/j/Y|n/d/Y|m-d-y|m-d-Y|m/d|m-d|md|mdy|mdY|d|Y-m-d|n-j|n/j';
	Date.altTimeFormats = 'H:i:s.uO|g:ia|g:iA|g:i a|g:i A|h:i|g:i|H:i|ga|ha|gA|h a|g a|g A|gi|hi|gia|hia|g|H|gi a|hi a|giA|hiA|gi A|hi A';
	
</script>
