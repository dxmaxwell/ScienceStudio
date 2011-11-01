/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *     RestScan class.
 *     
 */
package ca.sciencestudio.model.session.dao.rest.support;

import java.util.Date;

import ca.sciencestudio.util.Parameters;

/**
 * @author maxweld
 *
 */
public class RestScan {
	
	private String name;
	private String experimentGid;
	private String dataUrl;
	private Parameters parameters;
	private Date startDate;
	private Date endDate;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
		
	public String getExperimentGid() {
		return experimentGid;
	}
	public void setExperimentGid(String experimentGid) {
		this.experimentGid = experimentGid;
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
