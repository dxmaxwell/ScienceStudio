/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *     SessionFormBacker class.
 *     
 */
package ca.sciencestudio.service.session.backers;

import java.util.Date;
import java.util.Calendar;
import java.util.Map;

import ca.sciencestudio.model.session.Session;
import ca.sciencestudio.util.convert.ConversionServiceHolder;
import ca.sciencestudio.util.convert.SimpleDateToStringConverter;
import ca.sciencestudio.util.rest.ValidationError;
import ca.sciencestudio.util.rest.ValidationResult;

/**
 * @author maxweld
 *
 */
public class SessionFormBacker extends Session {
	
	private static final long serialVersionUID = 1L;
	
	private static final String START_DAY_FIELD = "startDay";
	private static final String END_DAY_FIELD = "endDay";
	
	private static final String START_TIME_FIELD = "startTime";
	private static final String END_TIME_FIELD = "endTime";
	
	private static final String START_DATE_FIELD = "startDate";
	private static final String END_DATE_FIELD = "endDate";
	
	private static final String DAY_TIME_DELIMINATOR = " ";
	
	private static final SimpleDateToStringConverter converter = new SimpleDateToStringConverter("yyyy-MM-dd HH:mm:ss.SSSZ");
	
	private String startDay;
	private String startTime;
	private String endDay;
	private String endTime;
	
	public static ValidationResult transformResult(ValidationResult result) {
		Map<String, ValidationError> fieldErrors = result.getFieldErrors();
		if(fieldErrors.containsKey(START_DATE_FIELD)) {
			ValidationError error = fieldErrors.get(START_DATE_FIELD);
			fieldErrors.put(START_DAY_FIELD, error);
			fieldErrors.put(START_TIME_FIELD, error);
			fieldErrors.remove(START_DATE_FIELD);
		}
		if(fieldErrors.containsKey(END_DATE_FIELD)) {
			ValidationError error = fieldErrors.get(END_DATE_FIELD);
			fieldErrors.put(END_DAY_FIELD, error);
			fieldErrors.put(END_TIME_FIELD, error);
			fieldErrors.remove(END_DATE_FIELD);
		}
		return result;
	}
	
	public SessionFormBacker() {
		setStartDate(getStartDate());
		setEndDate(getEndDate());
	}
	
//	public SessionFormBacker(int projectId) {
//		setId(0);
//		setProjectId(projectId);
//		setLaboratoryId(0);
//		setName("");
//		setDescription("");
//		setProposal("");
//		setStartDate("");
//		setEndDate("");
//		setStartTime("");
//		setEndTime("");
//	}
	
	public SessionFormBacker(Session session) {
		super(session);
		setStartDate(session.getStartDate());
		setEndDate(session.getEndDate());
	}
	
	@Override
	public void setStartDate(Date startDate) {
		try {
			String[] daytime = converter.convert(startDate).split(DAY_TIME_DELIMINATOR, 2);
			startDay = daytime[0];
			startTime = daytime[1];
		}
		catch(IllegalArgumentException e) {
			startDay = null;
			startTime = null;
		}
		super.setStartDate(startDate);
	}

	@Override
	public void setEndDate(Date endDate) {
		try {
			String[] daytime = converter.convert(endDate).split(DAY_TIME_DELIMINATOR, 2);
			endDay = daytime[0];
			endTime = daytime[1];
		}
		catch(IllegalArgumentException e) {
			endDay = null;
			endTime = null;
		}
		super.setEndDate(endDate);
	}
	
	public String getStartDay() {
		return startDay;
	}
	public void setStartDay(String startDay) {
		this.startDay = startDay;		
		try {
			super.setStartDate(ConversionServiceHolder.getConversionService().convert(getStartDayTime(), Date.class));
		}
		catch(IllegalArgumentException e) {
			super.setStartDate(null);
		}
	}

	public String getStartTime() {
		return startTime;
	}
	public void setStartTime(String startTime) {
		this.startTime = startTime;
		try {
			super.setStartDate(ConversionServiceHolder.getConversionService().convert(getStartDayTime(), Date.class));
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
			super.setEndDate(ConversionServiceHolder.getConversionService().convert(getEndDayTime(), Date.class));
		}
		catch(IllegalArgumentException e) {
			super.setEndDate(null);
		}
	}

	public String getEndTime() {
		return endTime;
	}
	public void setEndTime(String endTime) {
		this.endTime = endTime;
		try {
			super.setEndDate(ConversionServiceHolder.getConversionService().convert(getEndDayTime(), Date.class));
		}
		catch(IllegalArgumentException e) {
			super.setEndDate(null);
		}
	}
	
	protected Calendar getCalendar() {
		return Calendar.getInstance();
	}
	
	protected String getStartDayTime() {
		return getDayTime(startDay, startTime);
	}
	
	protected String getEndDayTime() {
		return getDayTime(endDay, endTime);
	}
	
	protected String getDayTime(String day, String time) {
		String dayTime = null;
		
		if((day != null) && (day.length() > 0)) {
			dayTime = day;
		}
		
		if((time != null) && (time.length() > 0)) {
			if(dayTime != null) {
				dayTime += DAY_TIME_DELIMINATOR + time;
			} else {
				dayTime = time;
			}
		}
		
		return dayTime;
	}
}
