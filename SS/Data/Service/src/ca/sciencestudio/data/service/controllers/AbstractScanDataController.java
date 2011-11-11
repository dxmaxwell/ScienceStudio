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

import ca.sciencestudio.data.cdf.CDFQuery;
import ca.sciencestudio.data.standard.StdCategories;
import ca.sciencestudio.data.standard.StdConverter;
import ca.sciencestudio.data.support.CDFQueryException;
import ca.sciencestudio.model.session.Scan;
import ca.sciencestudio.model.session.dao.ScanAuthzDAO;
import ca.sciencestudio.security.util.SecurityUtil;

/**
 * @author maxweld
 *
 */
public abstract class AbstractScanDataController implements StdConverter, StdCategories{
	
	protected ScanAuthzDAO scanAuthzDAO;
	
	protected Log logger = LogFactory.getLog(getClass());
		
	protected CDFQuery getCDFQueryByScanGid(String scanGid) throws Exception {
		
		String user = SecurityUtil.getPersonGid();
		
		Scan scan = scanAuthzDAO.get(user, scanGid).get();
		if(scan == null) {
			throw new Exception("Scan not found.");
		}
		
		try {
			return new CDFQuery(scan);
		}
		catch(CDFQueryException e) {
			String msg = "Exception while constructing CDF query (Scan:" + scanGid + ").";
			Exception error = new Exception(msg, e);
			logger.warn(msg, e);
			throw error;
		}
	}

	public ScanAuthzDAO getScanAuthzDAO() {
		return scanAuthzDAO;
	}
	public void setScanAuthzDAO(ScanAuthzDAO scanAuthzDAO) {
		this.scanAuthzDAO = scanAuthzDAO;
	}
}
