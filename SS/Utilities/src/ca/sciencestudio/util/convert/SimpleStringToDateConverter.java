/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *     SimpleStringToDateConverter class.
 *
 */
package ca.sciencestudio.util.convert;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

import org.springframework.core.convert.converter.Converter;

/**
 * @author maxweld
 * 
 *
 */
public class SimpleStringToDateConverter implements Converter<String, Date> {

	private Collection<DateFormat> dateFormats;
	
	@Override
	public Date convert(String source) {
		for(DateFormat dateFormat: dateFormats) {
			try {
				// Clone DateFormat for thread safety. //
				Object clone = dateFormat.clone();
				return ((DateFormat)clone).parse(source);
			}
			catch(ParseException e) {
				continue;
			}
		}
		
		throw new IllegalArgumentException("No DateFormat found to convert '" + source + "' to java.util.Date.");
	}

	public void setPatterns(Collection<String> patterns) {
		dateFormats = new ArrayList<DateFormat>(patterns.size());
		for(String pattern : patterns) {
			dateFormats.add(new SimpleDateFormat(pattern));
		}
	}
	
	public Collection<DateFormat> getDateFormats() {
		return dateFormats;
	}
	public void setDateFormats(Collection<DateFormat> dateFormats) {
		this.dateFormats = dateFormats;
	}	
}
