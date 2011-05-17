/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *     RestScanDAO class.
 *     
 */
package ca.sciencestudio.model.dao.rest;

import ca.sciencestudio.model.Scan;
import ca.sciencestudio.model.dao.ScanDAO;
import ca.sciencestudio.model.dao.rest.support.AbstractRestModelDAO;
import ca.sciencestudio.model.dao.rest.support.RestScan;

/**
 * @author maxweld
 *
 */
public class RestScanDAO extends AbstractRestModelDAO<Scan, RestScan> implements ScanDAO {
	
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
