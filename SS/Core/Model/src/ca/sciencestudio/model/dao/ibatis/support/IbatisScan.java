/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *     IbatisScan class.
 *     
 */
package ca.sciencestudio.model.dao.ibatis.support;

import java.util.Date;

import ca.sciencestudio.model.Scan;

/**
 * @author maxweld
 *
 */
public class IbatisScan {
	
	public static final int DEFAULT_ID = 0;
	
	private int id = DEFAULT_ID;
	private String name = Scan.DEFAULT_NAME;
	private String dataUrl = Scan.DEFAULT_DATE_URL;
	private String parameters = Scan.DEFAULT_PARAMETERS;
	private Date startDate = Scan.DEFAULT_START_DATE;
	private Date endDate = Scan.DEFAULT_END_DATE;
	private int experimentId = Scan.DEFAULT_EXPERIMENT_ID;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
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
	
	public int getExperimentId() {
		return experimentId;
	}
	public void setExperimentId(int experimentId) {
		this.experimentId = experimentId;
	}
}
