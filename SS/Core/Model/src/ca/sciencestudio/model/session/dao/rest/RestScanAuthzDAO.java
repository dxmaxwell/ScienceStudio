/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *     RestScanDAO class.
 *     
 */
package ca.sciencestudio.model.session.dao.rest;

import ca.sciencestudio.model.session.Scan;
import ca.sciencestudio.model.session.dao.ScanAuthzDAO;
import ca.sciencestudio.model.session.dao.ScanBasicDAO;
import ca.sciencestudio.model.session.dao.rest.support.RestScan;
import ca.sciencestudio.model.dao.rest.AbstractRestModelAuthzDAO;

/**
 * @author maxweld
 *
 */
public class RestScanAuthzDAO extends AbstractRestModelAuthzDAO<Scan, RestScan> implements ScanAuthzDAO {
	
	@Override
	protected RestScan toRestModel(Scan scan) {
		RestScan restScan = new RestScan();
		restScan.setName(scan.getName());
		restScan.setDataUrl(scan.getDataUrl());
		restScan.setParameters(scan.getParameters());
		restScan.setStartDate(scan.getStartDate());
		restScan.setEndDate(scan.getEndDate());
		restScan.setExperimentId(scan.getExperimentId());
		return restScan;
	}

	@Override
	protected String getModelUrl() {
		return getBaseUrl() + "/scans";
	}

	@Override
	protected Class<Scan> getModelClass() {
		return Scan.class;
	}

	@Override
	protected Class<Scan[]> getModelArrayClass() {
		return Scan[].class;
	}
}
