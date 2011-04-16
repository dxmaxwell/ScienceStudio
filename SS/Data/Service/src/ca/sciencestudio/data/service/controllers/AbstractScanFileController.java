/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *     AbstractScanDataFileController class.
 *
 */
package ca.sciencestudio.data.service.controllers;

import java.io.File;
import java.io.IOException;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.ServletContextAware;

import ca.sciencestudio.data.standard.StdScanParams;
import ca.sciencestudio.model.project.Project;
import ca.sciencestudio.model.project.dao.ProjectDAO;
import ca.sciencestudio.model.session.Scan;
import ca.sciencestudio.model.session.dao.ScanDAO;
import ca.sciencestudio.security.util.AuthorityUtil;
import ca.sciencestudio.security.util.SecurityUtil;

/**
 * @author maxweld
 *
 */
public class AbstractScanFileController implements ServletContextAware, StdScanParams {
	
	@Autowired
	protected ScanDAO scanDAO;
	
	@Autowired
	protected ProjectDAO projectDAO;
	
	protected ServletContext servletContext;
	
	protected Log logger = LogFactory.getLog(getClass());
	
	protected Scan getScanWithSecurityCheck(int scanId, HttpServletResponse response) {
		
		Project project = projectDAO.getProjectByScanId(scanId);
		if(project == null) {
			sendError(response, HttpServletResponse.SC_NOT_FOUND);
			return null;
		}
		
		Object admin = AuthorityUtil.ROLE_ADMIN_DATA;
		Object group = AuthorityUtil.buildProjectGroupAuthority(project.getId());
		
		if(!(SecurityUtil.hasAnyAuthority(group, admin))) {
			sendError(response, HttpServletResponse.SC_UNAUTHORIZED);
			return null;
		}
		
		Scan scan = scanDAO.getScanById(scanId);
		if(scan == null) {
			sendError(response, HttpServletResponse.SC_NOT_FOUND);
			return null;
		}
		
		return scan;
	}
	
	protected void sendError(HttpServletResponse response, int statusCode) {
		sendError(response, statusCode, null);
	}
	
	protected void sendError(HttpServletResponse response, int statusCode, String statusMsg) {
		try {
			if((statusMsg == null) || (statusMsg.length() == 0)) {
				response.sendError(statusCode);
			} else {
				response.sendError(statusCode, statusMsg);
			}
		}
		catch(IOException e) {
			logger.warn("Exception while sending status code " + statusCode, e);
		}
	}

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

	public ScanDAO getScanDAO() {
		return scanDAO;
	}
	public void setScanDAO(ScanDAO scanDAO) {
		this.scanDAO = scanDAO;
	}

	public ProjectDAO getProjectDAO() {
		return projectDAO;
	}
	public void setProjectDAO(ProjectDAO projectDAO) {
		this.projectDAO = projectDAO;
	}
}
