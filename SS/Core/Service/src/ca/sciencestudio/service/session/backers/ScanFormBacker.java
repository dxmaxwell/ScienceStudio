/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *     ScanFormBacker class.
 *     
 */
package ca.sciencestudio.service.session.backers;

import java.text.ParseException;
import java.util.Date;

import ca.sciencestudio.model.session.Scan;
import ca.sciencestudio.model.session.dao.ScanDAO;
import ca.sciencestudio.util.web.BindAndValidateUtils;

/**
 * @author maxweld
 *
 */
public class ScanFormBacker {

	private int id;
	private int experimentId;
	private String name;
	private String dataUrl;
	private String endDate;
	private String startDate;
	private String parameters;
	
	public ScanFormBacker(Scan scan) {
		setId(scan.getId());
		setExperimentId(scan.getExperimentId());
		setName(scan.getName());
		setDataUrl(scan.getDataUrl());
		setRawEndDate(scan.getEndDate());
		setRawStartDate(scan.getStartDate());
		setParameters(scan.getParameters());
	}

	public Scan createScan(ScanDAO scanDAO) {
		Scan scan = scanDAO.createScan();
		scan.setId(getId());
		scan.setExperimentId(getExperimentId());
		scan.setName(getName());
		scan.setDataUrl(getDataUrl());
		scan.setEndDate(getRawEndDate());
		scan.setStartDate(getRawStartDate());
		scan.setParameters(getParameters());
		return scan;
	}
	
	public Date getRawEndDate() {
		try {
			return BindAndValidateUtils.DATE_FORMAT_ISO8601_LONG.parse(getEndDate());
		}
		catch(ParseException e) {
			return null;
		}
	}
	public void setRawEndDate(Date rawEndDate) {
		setEndDate(BindAndValidateUtils.DATE_FORMAT_ISO8601_LONG.format(rawEndDate));
	}
	
	public Date getRawStartDate() {
		try {
			return BindAndValidateUtils.DATE_FORMAT_ISO8601_LONG.parse(getStartDate());
		}
		catch(ParseException e) {
			return null;
		}
	}
	public void setRawStartDate(Date rawStartDate) {
		setStartDate(BindAndValidateUtils.DATE_FORMAT_ISO8601_LONG.format(rawStartDate));
	}
	
	public int getId() {
		return id;
	}
	private void setId(int id) {
		this.id = id;
	}

	public int getExperimentId() {
		return experimentId;
	}
	private void setExperimentId(int experimentId) {
		this.experimentId = experimentId;
	}

	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}

	public String getDataUrl() {
		return dataUrl;
	}
	public void setDataUrl(String dataUrl) {
		this.dataUrl = dataUrl;
	}

	public String getEndDate() {
		return endDate;
	}
	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	public String getStartDate() {
		return startDate;
	}
	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	public String getParameters() {
		return parameters;
	}
	public void setParameters(String parameters) {
		this.parameters = parameters;
	}
}
