/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *     DAFStandardRecordParser class.
 *     
 */
package ca.sciencestudio.data.daf;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
//import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import ca.sciencestudio.data.daf.DAFEvent;
import ca.sciencestudio.data.daf.DAFRecordParser;
import ca.sciencestudio.util.text.PeekableBufferedReader;

/**
 * @author maxweld
 *
 */
public class DAFStandardRecordParser implements DAFRecordParser {
	
	static private final Pattern EVENT_ID_PATTERN = Pattern.compile("^(\\d+),");
	static private final Pattern EVENT_VALUE_PATTERN = Pattern.compile("\\s*([^,]*)\\s*,?");
	
	public DAFRecord parseRecord(Collection<DAFEvent> events, PeekableBufferedReader reader) throws IOException {
		
		if(events == null || reader == null) {
			return null;
		}
		
		Matcher matcher = EVENT_ID_PATTERN.matcher(reader.peekLine());
		if(!matcher.find()) {
			return null;
		}
		
		int eventId;
		try {
			eventId = Integer.parseInt(matcher.group(1));
		}
		catch(NumberFormatException e) {
			return null;
		}
		
		DAFEvent event = null;
		for(DAFEvent e : events) {
			if(e.getId() == eventId) {
				event = e;
			}
		}
		
		if(event == null) {
			return null;
		}
		
		int numberOfElements = event.getNumberOfElements();
		Collection<String> values = new ArrayList<String>(numberOfElements);
		
		values.add(matcher.group(1));
		
		matcher.usePattern(EVENT_VALUE_PATTERN);
		for(int idx=1; idx<numberOfElements; idx++) {
			if(!matcher.find()) {
				return null;
			}
			values.add(matcher.group(1));
		}
		
		reader.readLine();
		return new DAFRecord(event, values);
	}
}
