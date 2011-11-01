/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *     IbatisScan class.
 *     
 */
package ca.sciencestudio.model.session.dao.ibatis.support;

import java.util.Date;

import org.springframework.http.MediaType;

import ca.sciencestudio.model.session.validators.ScanValidator;
import ca.sciencestudio.model.utilities.GID;

/**
 * @author maxweld
 *
 */
public class IbatisScan {
	
	public static final int DEFAULT_ID = GID.DEFAULT_ID;
	public static final int DEFAULT_EXPERIMENT_ID = GID.DEFAULT_ID;
	public static final String DEFAULT_PARAMETERS = "{}";
	public static final String DEFAULT_PARAMETERS_TYPE = MediaType.APPLICATION_JSON.toString();
	
	private int id = DEFAULT_ID;
	private int experimentId = DEFAULT_EXPERIMENT_ID;
	private String name = ScanValidator.DEFAULT_NAME;
	private String dataUrl = ScanValidator.DEFAULT_DATE_URL;
	private String parameters = DEFAULT_PARAMETERS;
	private String parametersType = DEFAULT_PARAMETERS_TYPE;
	private Date startDate = ScanValidator.DEFAULT_START_DATE;
	private Date endDate = ScanValidator.DEFAULT_END_DATE;
	
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
	
	public String getParametersType() {
		return parametersType;
	}
	public void setParametersType(String parametersType) {
		this.parametersType = parametersType;
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
