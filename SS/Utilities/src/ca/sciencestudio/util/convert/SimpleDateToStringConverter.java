/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *     SimpleStringToDateConverter class.
 *
 */
package ca.sciencestudio.util.convert;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.core.convert.converter.Converter;

/**
 * @author maxweld
 * 
 *
 */
public class SimpleDateToStringConverter implements Converter<Date, String> {

	public static final String ISO8601_PATTERN = "yyyy-MM-dd'T'HH:mm:ss.SSSZ";
	
	private DateFormat dateFormat;
	
	public SimpleDateToStringConverter() {
		setPattern(ISO8601_PATTERN);
	}
	
	public SimpleDateToStringConverter(String pattern) {
		setPattern(pattern);
	}
	
	public SimpleDateToStringConverter(DateFormat dateFormat) {
		setDateFormat(dateFormat);
	}
	
	public void setPattern(String pattern) {
		setDateFormat(new SimpleDateFormat(pattern));
	}
	
	public void setDateFormat(DateFormat dateFormat) {
		this.dateFormat = dateFormat;
	}
	
	@Override
	public String convert(Date date) {
		try {
			return dateFormat.format(date);
		}
		catch(Exception e) {
			throw new IllegalArgumentException(e);
		}
	}
}
