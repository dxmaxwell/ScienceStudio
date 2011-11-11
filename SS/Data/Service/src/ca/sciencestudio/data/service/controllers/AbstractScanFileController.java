/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *     AbstractScanDataFileController class.
 *
 */
package ca.sciencestudio.data.service.controllers;

import java.io.File;

import javax.servlet.ServletContext;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.context.ServletContextAware;

import ca.sciencestudio.data.standard.StdScanParams;
import ca.sciencestudio.model.session.dao.ScanAuthzDAO;

/**
 * @author maxweld
 *
 */
public class AbstractScanFileController implements ServletContextAware, StdScanParams {
	
	protected ScanAuthzDAO scanAuthzDAO;
	
	protected ServletContext servletContext;
	
	protected Log logger = LogFactory.getLog(getClass());
	
	protected String getContentType(String file) {
		return servletContext.getMimeType(file);
	}
	
	protected String getContentType(File file) {
		return getContentType(file.getName());
	}
	
	protected String getContentTypeWithExt(String ext) {
		if(ext.startsWith(".")) {
			return getContentType("file" + ext);
		} else {
			return getContentType("file." + ext);
		}
	}
	
	public void setServletContext(ServletContext servletContext) {
		this.servletContext = servletContext;
	}

	public ScanAuthzDAO getScanAuthzDAO() {
		return scanAuthzDAO;
	}
	public void setScanAuthzDAO(ScanAuthzDAO scanAuthzDAO) {
		this.scanAuthzDAO = scanAuthzDAO;
	}
}
