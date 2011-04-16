/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *     AbstractScanDataController class.
 *     
 */
package ca.sciencestudio.data.service.controllers;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindException;

import ca.sciencestudio.data.cdf.CDFQuery;
import ca.sciencestudio.data.standard.StdCategories;
import ca.sciencestudio.data.standard.StdConverter;
import ca.sciencestudio.data.support.CDFQueryException;
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
public abstract class AbstractScanDataController implements StdConverter, StdCategories{
	
	@Autowired
	protected ScanDAO scanDAO;
	
	@Autowired
	protected ProjectDAO projectDAO;
	
	protected Log logger = LogFactory.getLog(getClass());
	
	protected Scan getScanWithSecurityCheck(int scanId, BindException errors) {
		
		Project project = projectDAO.getProjectByScanId(scanId);
		if(project == null) {
			errors.reject("project.notfound", "Project not found.");
			return null;
		}
		
		Object admin = AuthorityUtil.ROLE_ADMIN_DATA;
		Object group = AuthorityUtil.buildProjectGroupAuthority(project.getId());
		
		if(!SecurityUtil.hasAnyAuthority(group, admin)) {
			errors.reject("permission.denied", "Not permitted to read scan data.");
			return null;
		}
		
		Scan scan = scanDAO.getScanById(scanId);
		if(scan == null) {
			errors.reject("scan.notfound", "Scan not found.");
			return null;
		}

		return scan;
	}
	
	protected CDFQuery getCDFQueryWithSecurityCheck(int scanId, BindException errors) {
		
		Scan scan = getScanWithSecurityCheck(scanId, errors);
		if(errors.hasErrors()) {
			return null;
		}
		
		try {
			return new CDFQuery(scan);
		}
		catch(CDFQueryException e) {
			String msg = "Exception while constructing CDF query (ScanId:" + scanId + ").";
			errors.reject("cdfquery.error", msg);
			logger.warn(msg, e);
			return null;
		}
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
