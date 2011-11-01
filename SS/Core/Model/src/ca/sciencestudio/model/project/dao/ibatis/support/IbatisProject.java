/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *     IbatisProject class.
 *     
 */
package ca.sciencestudio.model.project.dao.ibatis.support;

import java.util.Date;

import ca.sciencestudio.model.project.validators.ProjectValidator;

/**
 * @author maxweld
 *
 */
public class IbatisProject {
	
	public static final int DEFAULT_ID = 0;
	public static final int DEFAULT_FACILITY_ID = 0;
	public static final String DEFAULT_STATUS = "";
	
	private int id = DEFAULT_ID;
	private String name = ProjectValidator.DEFAULT_NAME;
	private String description = ProjectValidator.DEFAULT_DESCRIPTION;
	private int facilityId = DEFAULT_FACILITY_ID;
	private Date startDate = ProjectValidator.DEFAULT_START_DATE;
	private Date endDate = ProjectValidator.DEFAULT_END_DATE;
	private String status = DEFAULT_STATUS;
	
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
	
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	
	public int getFacilityId() {
		return facilityId;
	}
	public void setFacilityId(int facilityId) {
		this.facilityId = facilityId;
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
	
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
}
