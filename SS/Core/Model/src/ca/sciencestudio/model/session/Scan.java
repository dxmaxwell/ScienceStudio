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
import ca.sciencestudio.model.utilities.GID;

/**
 * @author maxweld
 *
 */
public class Scan implements Model {
	
	private static final long serialVersionUID = 1L;
	
	public static final String GID_TYPE = "N";
	
	// Maximum field length should match database limits. //
	public static final int MAX_LENGTH_NAME = 100;
	public static final int MAX_LENGTH_DATE_URL = 1000;
	public static final int MAX_LENGTH_PARAMETERS = 2000;
	////////////////////////////////////////////////////////
	
	public static final String DEFAULT_GID = GID.DEFAULT_GID;
	public static final String DEFAULT_EXPERIMENT_GID = GID.DEFAULT_GID;
	public static final String DEFAULT_NAME = "";
	public static final String DEFAULT_DATE_URL= "";
	public static final String DEFAULT_PARAMETERS= "";
	public static final Date DEFAULT_START_DATE = new Date(0);
	public static final Date DEFAULT_END_DATE = new Date(0);
	
	private String gid = DEFAULT_GID;
	private String experimentGid = DEFAULT_EXPERIMENT_GID;
	private String name = DEFAULT_NAME;
	private String dataUrl = DEFAULT_DATE_URL;
	private String parameters = DEFAULT_PARAMETERS;
	private Date startDate = DEFAULT_START_DATE;
	private Date endDate = DEFAULT_END_DATE;
	
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
	
	public String getParameters() {
		return parameters;
	}
	public void setParameters(String parameters) {
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
