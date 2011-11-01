/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *     RestScanAuthzDAO class.
 *     
 */
package ca.sciencestudio.model.session.dao.rest;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.springframework.web.client.RestClientException;

import ca.sciencestudio.model.session.Scan;
import ca.sciencestudio.model.session.dao.ScanAuthzDAO;
import ca.sciencestudio.model.session.dao.rest.support.RestScan;
import ca.sciencestudio.model.dao.Data;
import ca.sciencestudio.model.dao.SimpleData;
import ca.sciencestudio.model.dao.rest.AbstractRestModelAuthzDAO;
import ca.sciencestudio.util.exceptions.ModelAccessException;

/**
 * @author maxweld
 *
 */
public class RestScanAuthzDAO extends AbstractRestModelAuthzDAO<Scan> implements ScanAuthzDAO {
	
	public static final String SCAN_MODEL_PATH = "/scans";
	
	@Override
	public Data<List<Scan>> getAllByExperimentGid(String user, String experimentGid) {
		List<Scan> scans;
		try {
			scans = Arrays.asList(getRestTemplate().getForObject(getRestUrl("", "user={user}", "experiment={experiment}"), getModelArrayClass(), user, experimentGid));
		}
		catch(RestClientException e) {
			logger.warn("Rest Client exception while getting Model list: " + e.getMessage());
			return new SimpleData<List<Scan>>(new ModelAccessException(e));
		}
		
		if(logger.isDebugEnabled()) {
			logger.debug("Get all Scans by Experiment GID: " + experimentGid + ", size: " + scans.size());
		}
		return new SimpleData<List<Scan>>(Collections.unmodifiableList(scans));
	}
	
	@Override
	protected RestScan toRestModel(Scan scan) {
		RestScan restScan = new RestScan();
		restScan.setName(scan.getName());
		restScan.setExperimentGid(scan.getExperimentGid());
		restScan.setDataUrl(scan.getDataUrl());
		restScan.setParameters(scan.getParameters());
		restScan.setStartDate(scan.getStartDate());
		restScan.setEndDate(scan.getEndDate());
		return restScan;
	}

	@Override
	protected String getModelPath() {
		return SCAN_MODEL_PATH;
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
