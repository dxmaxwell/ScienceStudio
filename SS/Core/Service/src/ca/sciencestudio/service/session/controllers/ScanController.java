/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *     ScanController class.
 *     
 */
package ca.sciencestudio.service.session.controllers;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import ca.sciencestudio.model.session.Scan;
import ca.sciencestudio.model.session.dao.ScanAuthzDAO;
import ca.sciencestudio.security.util.SecurityUtil;
import ca.sciencestudio.service.utilities.ModelPathUtils;

/**
 * @author maxweld
 *
 */
@Controller
public class ScanController {

	private ScanAuthzDAO scanAuthzDAO;
		
	@ResponseBody
	@RequestMapping(value = ModelPathUtils.SCAN_PATH + "*", params = "experiment")
	public List<Scan> getScanList(@RequestParam("experiment") String experimentGid) {
		String user = SecurityUtil.getPersonGid();
		return scanAuthzDAO.getAllByExperimentGid(user, experimentGid).get();
	}

	public ScanAuthzDAO getScanAuthzDAO() {
		return scanAuthzDAO;
	}
	public void setScanAuthzDAO(ScanAuthzDAO scanAuthzDAO) {
		this.scanAuthzDAO = scanAuthzDAO;
	}
}
