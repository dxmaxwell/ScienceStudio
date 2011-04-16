<?xml version="1.0" standalone="no"?>
<%-- 
	Copyright (c) Canadian Light Source, Inc. All rights reserved.
	- see license.txt for details.
	
	Description:
		spectrum-cdfml JSP page.
--%>
<%@ page language="java" contentType="application/xml; charset=UTF-8" pageEncoding="UTF-8" trimDirectiveWhitespaces="true" %>
<%@ include file="/WEB-INF/jsp/include/taglibs.jsp" %>
<CDF xmlns="http://cdf.gsfc.nasa.gov" name="spectrum.cdf">
   <cdfFileInfo fileFormat="SINGLE" majority="ROW" encoding="IBMPC" negToPosFp0="DISABLE"/>
   <cdfGAttributes>
      <attribute name="ScienceStudio">
         <entry entryNum="4" cdfDatatype="CDF_CHAR"><![CDATA[MCA:1.0]]></entry> 
      </attribute>
      <attribute name="SS:Created">
         <entry entryNum="0" cdfDatatype="CDF_EPOCH"><fmt:formatDate value="${now}" pattern="dd-MMM-yyyy HH:mm:ss.SSS"/><%-- 07-Sep-2010 20:55:26.020 --%></entry> 
      </attribute>
      <attribute name="SS:CreatedBy">
         <entry entryNum="0" cdfDatatype="CDF_CHAR"><![CDATA[SSVespersWebApplication]]]></entry> 
      </attribute>
      <attribute name="SS:Source">
         <entry entryNum="0" cdfDatatype="CDF_CHAR"><![CDATA[spectrum-cdfml.jsp]]></entry>
      </attribute>
      <attribute name="MCA:NElements">
         <entry entryNum="0" cdfDatatype="CDF_INT2">0</entry> 
      </attribute>
   </cdfGAttributes>
   <cdfVariables>
      <variable name="MCA:SumSpectrum">
         <cdfVarInfo cdfDatatype="CDF_UINT4" numElements="1" dim="1" dimSizes="2048" recVariance="VARY" dimVariances="VARY" compression="GZIP.7" numRecordsAllocate="302" blockingFactor="1"/>
         <cdfVAttributes>
            <attribute name="MCA:Name">
               <entry cdfDatatype="CDF_CHAR"><![CDATA[dxp1607-B21-04:mcaCorrected.VAL]]></entry>
            </attribute>
            <attribute name="MCA:Desc">
               <entry cdfDatatype="CDF_CHAR"><![CDATA[$(4ElemMCA)Corrected$(mcaSPECINDEX)]]></entry>
            </attribute>
            <attribute name="MCA:Unit">
               <entry cdfDatatype="CDF_CHAR"><![CDATA[count]]></entry>
            </attribute>
            <attribute name="MCA:SumSpectrumCrts">
               <entry cdfDatatype="CDF_CHAR"><![CDATA[DeadTime]]></entry>
            </attribute>
         </cdfVAttributes>
         <cdfVarData>
            <record recNum="0"><c:forEach items="${spectrum}" var="value" varStatus="status">${value}<c:if test="${not status.last}">${' '}</c:if></c:forEach></record>
         </cdfVarData>
      </variable>
   </cdfVariables>
</CDF>
