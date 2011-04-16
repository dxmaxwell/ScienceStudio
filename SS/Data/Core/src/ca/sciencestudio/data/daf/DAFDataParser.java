/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *     DAFDataParser class.
 *     
 */
package ca.sciencestudio.data.daf;

import java.lang.Iterable;

import java.io.File;
import java.io.Reader;
import java.io.FileReader;
import java.io.IOException;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.ArrayList;
import java.util.TimeZone;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import ca.sciencestudio.data.daf.DAFEvent;
import ca.sciencestudio.data.daf.DAFRecord;
import ca.sciencestudio.data.support.DataFormatException;
import ca.sciencestudio.util.text.PeekableBufferedReader;

/**
 * @author maxweld
 *
 */
public class DAFDataParser implements Iterator<DAFRecord>, Iterable<DAFRecord>{
	
	static private final String DAF_START_TIME_TIME_ZONE_ID = "America/Regina";
	
	static private final String REGEX_MAGIC = "^# CLS Data Acquisition";
	static private final DateFormat DATE_FORMAT_START_TIME = new SimpleDateFormat("# EEE MMM d HH:mm:ss yyyy");
	
	private Date startTime;
	private PeekableBufferedReader reader;
	private DAFRecordParser recordParser;
	private Collection<DAFRecordParser> customRecordParsers;
	
	private DAFRecord nextRecord = null;
	private List<DAFEvent> events = new ArrayList<DAFEvent>();
	
	protected Log log = LogFactory.getLog(getClass());
	
	static {
		DATE_FORMAT_START_TIME.setTimeZone(TimeZone.getTimeZone(DAF_START_TIME_TIME_ZONE_ID));		
	}
	
	public DAFDataParser(String path) throws DataFormatException, IOException {
		this(new File(path));
	}
	
	public DAFDataParser(File file) throws DataFormatException, IOException {
		this(new FileReader(file));
	}
	
	public DAFDataParser(Reader reader) throws DataFormatException, IOException {
		initialize(reader);
	}
	
	public DAFDataParser(String path, DAFRecordParser recordParser) throws DataFormatException, IOException {
		this(new File(path), recordParser);
	}
	
	public DAFDataParser(File file, DAFRecordParser recordParser) throws DataFormatException, IOException {
		this(new FileReader(file), recordParser);
	}

	public DAFDataParser(Reader reader, DAFRecordParser recordParser) throws DataFormatException, IOException {
		initialize(reader, recordParser);
	}
	
	public DAFDataParser(String path, Collection<DAFRecordParser> customRecordParsers) throws DataFormatException, IOException {
		this(new File(path), customRecordParsers);
	}
	
	public DAFDataParser(File file, Collection<DAFRecordParser> customRecordParsers) throws DataFormatException, IOException {
		this(new FileReader(file), customRecordParsers);
	}
	
	public DAFDataParser(Reader reader, Collection<DAFRecordParser> customRecordParsers) throws DataFormatException, IOException {
		initialize(reader, new DAFStandardRecordParser(), customRecordParsers);
	}
	
	public DAFDataParser(String path, DAFRecordParser recordParser, Collection<DAFRecordParser> customRecordParsers) throws DataFormatException, IOException {
		this(new File(path), recordParser, customRecordParsers);
	}
	
	public DAFDataParser(File file, DAFRecordParser recordParser, Collection<DAFRecordParser> customRecordParsers) throws DataFormatException, IOException {
		this(new FileReader(file), recordParser, customRecordParsers);
	}
	
	public DAFDataParser(Reader reader, DAFRecordParser recordParser, Collection<DAFRecordParser> customRecordParsers) throws DataFormatException, IOException {
		initialize(reader, recordParser, customRecordParsers);
	}
	
	protected void initialize(Reader reader) throws DataFormatException, IOException {
		initialize(reader, new DAFStandardRecordParser(), new ArrayList<DAFRecordParser>());
	}
	
	protected void initialize(Reader reader, DAFRecordParser recordParser) throws DataFormatException, IOException {
		initialize(reader, recordParser, new ArrayList<DAFRecordParser>());
	}
	
	protected void initialize(Reader reader, Collection<DAFRecordParser> customRecordParsers) throws DataFormatException, IOException {
		initialize(reader, new DAFStandardRecordParser(), customRecordParsers);
	}
	
	protected void initialize(Reader reader, DAFRecordParser recordParser, Collection<DAFRecordParser> customRecordParsers) throws DataFormatException, IOException {
		this.startTime = new Date(0L);
		this.recordParser = recordParser;
		this.customRecordParsers = customRecordParsers;
		this.reader = new PeekableBufferedReader(reader);
		initialize();
	}
	
	protected void initialize() throws DataFormatException, IOException {
		
		String line = reader.readLine();
		
		if(line == null) {
			throw new DataFormatException("While initializing, first line not found.");
		}
		
		if(!line.matches(REGEX_MAGIC)) {
			throw new DataFormatException("While initializing, unexpected first line.");
		}
		
		try {
			startTime = DATE_FORMAT_START_TIME.parse(reader.peekLine());
			reader.readLine();
		}
		catch(ParseException e) {
			// nothing to do //
		}
		
		while(reader.peekLine() != null) {
			
			if(parseUpdateEvent()) {
				// nothing to do //
			}
			else if(parseNewEvent()) {
				// nothing to do //
			}
			else if(parseRecords()) {
				break;
			}
			else if(parseCustomRecords()) {
				break;
			}
			else {
				reader.readLine();
			}
		}
		
		if(events.isEmpty()) {
			throw new DataFormatException("While initializing, no events found.");
		}
	}
	
	protected boolean parseNewEvent() throws IOException {
		DAFEvent event = DAFEvent.parseEvent(reader);
		if(event == null) {
			return false;
		}
		else {
			events.add(event);
			return true;
		}
	}
	
	protected boolean parseUpdateEvent() throws IOException {
		for(DAFEvent event : events) {
			if(event.parseUpdate(reader)) {
				return true;
			}
		}
		return false;
	}
	
	protected boolean parseRecords() throws IOException {
		DAFRecord nextRecord = recordParser.parseRecord(getEvents(), reader);
		if(nextRecord != null) {
			this.nextRecord = nextRecord;
			return true;
		}
		return false;
	}
	
	protected boolean parseCustomRecords() throws IOException {
		for(DAFRecordParser customRecordParser : customRecordParsers) {
			DAFRecord nextRecord = customRecordParser.parseRecord(getEvents(), reader);
			if(nextRecord != null) {
				this.nextRecord = nextRecord;
				return true;
			}
		}
		return false;
	}
	
	public DAFRecord next() {
		
		DAFRecord nextRecord = this.nextRecord; 
		this.nextRecord = null;
		
		try {
			while(reader.peekLine() != null) {
					
				if(parseRecords()) {
					break;
				}
				else if(parseCustomRecords()) {
					break;
				}
				else {
					reader.readLine();
				}
			}
		}
		catch(IOException e) {
			log.warn("While reading next record, IO Exception thrown.", e);
		}
		
		return nextRecord;
	}
	
	public boolean hasNext() {
		return (nextRecord != null);
	}
	
	public void remove() {
		throw new UnsupportedOperationException();
	}

	public Iterator<DAFRecord> iterator() {
		return this;
	}
	
	public int getNumberOfEvents() {
		return events.size();
	}
	
	public List<DAFEvent> getEvents() {
		return Collections.unmodifiableList(events);
	}
	
	public int getEventIndexById(int eventId) {
		int eventIdx = 0;
		for(DAFEvent event : events) {
			if(event.getId() == eventId) {
				return eventIdx;
			}
			eventIdx++;
		}
		return -1;
	}
	
	public DAFEvent getEventById(int eventId) {
		try {
			return events.get(getEventIndexById(eventId));
		}
		catch(IndexOutOfBoundsException e) {
			return null;
		}
	}

	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}
}
