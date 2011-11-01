/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *     Scan class.
 *     
 */
package ca.sciencestudio.model.session;

import java.util.Date;

import ca.sciencestudio.model.Model;
import ca.sciencestudio.model.session.validators.ScanValidator;
import ca.sciencestudio.util.Parameters;

/**
 * @author maxweld
 *
 */
public class Scan implements Model {
	
	private static final long serialVersionUID = 1L;
	
	public static final String GID_TYPE = "N";
	
	private String gid;
	private String experimentGid;
	private String name;
	private String dataUrl;
	private Parameters parameters;
	private Date startDate;
	private Date endDate;
	
	public Scan() {
		gid = ScanValidator.DEFAULT_GID;
		experimentGid = ScanValidator.DEFAULT_EXPERIMENT_GID;
		name = ScanValidator.DEFAULT_NAME;
		dataUrl = ScanValidator.DEFAULT_DATE_URL;
		parameters = new Parameters(ScanValidator.DEFAULT_PARAMETERS);
		startDate = ScanValidator.DEFAULT_START_DATE;
		endDate = ScanValidator.DEFAULT_END_DATE;
	}
	
	public Scan(Scan scan) {
		gid = scan.getGid();
		experimentGid = scan.getExperimentGid();
		name = scan.getName();
		dataUrl = scan.getDataUrl();
		parameters = new Parameters(scan.getParameters());
		startDate = scan.getStartDate();
		endDate = scan.getEndDate();
	}
	
	@Override
	public boolean equals(Object obj) {
		if(!(obj instanceof Scan)) {
			return false;
		}
		Scan scan = (Scan)obj;
		if(gid == null || !gid.equals(scan.getGid())) {
			return false;
		}
		else if(experimentGid == null || !experimentGid.equals(scan.getExperimentGid())) {
			return false;
		}
		else if(name == null || !name.equals(scan.getName())) {
			return false;
		}
		else if(dataUrl == null || !dataUrl.equals(scan.getDataUrl())) {
			return false;
		}
		else if(parameters == null || !parameters.equals(scan.getParameters())) {
			return false;
		}
		else if(startDate == null || !startDate.equals(scan.getStartDate())) {
			return false;
		}
		else if(endDate == null || !endDate.equals(scan.getEndDate())) {
			return false;
		}
		else {
			return true;
		}
	}
	
	@Override
	public String toString() {
		StringBuffer buffer = new StringBuffer();
		buffer.append("[ gid:").append(gid);
		buffer.append("\", experimentGid:\"").append(experimentGid);
		buffer.append("\", name:\"").append(name);
		buffer.append("\", dataUrl:\"").append(dataUrl);
		buffer.append("\", parameters:\"").append(parameters);
		buffer.append("\", startDate:\"").append(startDate);
		buffer.append("\", endDate:\"").append(endDate);
		buffer.append(" ]");
		return buffer.toString();
	}
	
	public String getGid() {
		return gid;
	}
	public void setGid(String gid) {
		this.gid = gid;
	}
	
	public String getExperimentGid() {
		return experimentGid;
	}
	public void setExperimentGid(String experimentGid) {
		this.experimentGid = experimentGid;
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
	
	public Parameters getParameters() {
		return parameters;
	}
	public void setParameters(Parameters parameters) {
		this.parameters = parameters;
	}

	public Date getStartDate() {
		return startDate;
	}
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}
	
	public Date getEndDate() {
		return endDate;
	}
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}
}
