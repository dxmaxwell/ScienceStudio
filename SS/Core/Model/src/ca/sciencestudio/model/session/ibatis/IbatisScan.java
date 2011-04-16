/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *     IbatisScan class.
 *     
 */
package ca.sciencestudio.model.session.ibatis;

import java.util.Date;

import ca.sciencestudio.model.session.Scan;

/**
 * @author maxweld
 *
 */
public class IbatisScan implements Cloneable, Scan {
	
	private static final long serialVersionUID = 1L;
	
	private int id;
	private String name;
	private String dataUrl;
	private String parameters;
	private Date startDate;
	private Date endDate;
	private int experimentId;
	
	@Override
	public int getId() {
		return id;
	}
	@Override
	public void setId(int id) {
		this.id = id;
	}
	
	@Override
	public String getName() {
		return name;
	}
	@Override
	public void setName(String name) {
		this.name = name;
	}
	
	@Override
	public String getDataUrl() {
		return dataUrl;
	}
	@Override
	public void setDataUrl(String dataUrl) {
		this.dataUrl = dataUrl;
	}
	
	@Override
	public String getParameters() {
		return parameters;
	}
	@Override
	public void setParameters(String parameters) {
		this.parameters = parameters;
	}
	
	@Override
	public Date getStartDate() {
		return startDate;
	}
	@Override
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}
	
	@Override
	public Date getEndDate() {
		return endDate;
	}
	@Override
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}
	
	@Override
	public int getExperimentId() {
		return experimentId;
	}
	@Override
	public void setExperimentId(int experimentId) {
		this.experimentId = experimentId;
	}
	
	public Scan clone() {
		try {
			return (Scan) super.clone();
		}
		catch (CloneNotSupportedException e) {
			return null;
		}
	}
}
