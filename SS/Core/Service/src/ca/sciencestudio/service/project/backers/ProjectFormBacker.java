/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *     ProjectFormBacker class.
 *     
 */
package ca.sciencestudio.service.project.backers;

import java.util.Date;
import java.util.Map;

import ca.sciencestudio.model.project.Project;
import ca.sciencestudio.util.convert.ConversionServiceHolder;
import ca.sciencestudio.util.convert.SimpleDateToStringConverter;
import ca.sciencestudio.util.rest.ValidationError;
import ca.sciencestudio.util.rest.ValidationResult;

/**
 * @author maxweld
 *
 */
public class ProjectFormBacker extends Project { 
	
	private static final long serialVersionUID = 1L;

	private static final String START_DAY_FIELD = "startDay";
	private static final String END_DAY_FIELD = "endDay";
	
	private static final String START_DATE_FIELD = "startDate";
	private static final String END_DATE_FIELD = "endDate";
	
	private static final SimpleDateToStringConverter converter = new SimpleDateToStringConverter("yyyy-MM-dd");

	private String startDay;
	private String endDay;
	
	public static ValidationResult transformResult(ValidationResult result) {
		Map<String, ValidationError> fieldErrors = result.getFieldErrors();
		if(fieldErrors.containsKey(START_DATE_FIELD)) {
			fieldErrors.put(START_DAY_FIELD, fieldErrors.get(START_DATE_FIELD));
			fieldErrors.remove(START_DATE_FIELD);
		}
		if(fieldErrors.containsKey(END_DATE_FIELD)) {
			fieldErrors.put(END_DAY_FIELD, fieldErrors.get(END_DATE_FIELD));
			fieldErrors.remove(END_DATE_FIELD);
		}
		return result;
	}

	public ProjectFormBacker() {
		setStartDate(getStartDate());
		setEndDate(getEndDate());
	}

	public ProjectFormBacker(Project project) {
		super(project);
		setStartDate(getStartDate());
		setEndDate(getEndDate());
	}
	
	@Override
	public void setStartDate(Date startDate) {
		try {
			startDay = converter.convert(startDate);
		}
		catch(IllegalArgumentException e) {
			startDay = null;
		}
		super.setStartDate(startDate);
	}
	
	@Override
	public void setEndDate(Date endDate) {
		try {
			endDay = converter.convert(endDate);
		}
		catch(IllegalArgumentException e) {
			endDay = null;
		}
		super.setEndDate(endDate);
	}
	
	public String getStartDay() {
		return startDay;
	}
	public void setStartDay(String startDay) {		
		this.startDay = startDay;		
		try {
			super.setStartDate(ConversionServiceHolder.getConversionService().convert(startDay, Date.class));
		}
		catch(IllegalArgumentException e) {
			super.setStartDate(null);
		}
	}
	
	public String getEndDay() {
		return endDay;
	}
	public void setEndDay(String endDay) {
		this.endDay = endDay;
		try {
			super.setEndDate(ConversionServiceHolder.getConversionService().convert(endDay, Date.class));
		}
		catch(IllegalArgumentException e) {
			super.setEndDate(null);
		}
	}
}
