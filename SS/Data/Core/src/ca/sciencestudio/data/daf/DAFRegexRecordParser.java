/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *     DAFRegexRecordParser class.
 *     
 */
package ca.sciencestudio.data.daf;

import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import ca.sciencestudio.data.daf.DAFRecord;
import ca.sciencestudio.util.text.PeekableBufferedReader;

/**
 * @author maxweld
 *
 */
public class DAFRegexRecordParser implements DAFRecordParser {
	
	static private final int DEFAULT_EVENT_ID = 0;
	static private final String DEFAULT_PREFIX_REGEX = "";
	static private final String DEFAULT_POSTFIX_REGEX = "";

	private Pattern prefixPattern;
	private Pattern postfixPattern;
	private List<DAFRegexElementParser> elementParsers;
	
	public DAFRegexRecordParser() {
		this(DEFAULT_EVENT_ID);
	}
	
	public DAFRegexRecordParser(int eventId) {
		this(eventId, DEFAULT_PREFIX_REGEX, DEFAULT_POSTFIX_REGEX, new ArrayList<DAFRegexElementParser>());
	}
	
	public DAFRegexRecordParser(int eventId, String prefixRegex, List<DAFRegexElementParser> elementParsers) {
		this(eventId, prefixRegex, DEFAULT_POSTFIX_REGEX, elementParsers);
	}
	
	public DAFRegexRecordParser(int eventId, Pattern prefixPattern, List<DAFRegexElementParser> elementParsers) {
		this(eventId, prefixPattern, Pattern.compile(DEFAULT_POSTFIX_REGEX), elementParsers);
	}
	
	public DAFRegexRecordParser(int eventId, String prefixRegex, String postfixRegex, List<DAFRegexElementParser> elementParsers) {
		this(eventId, Pattern.compile(prefixRegex), Pattern.compile(postfixRegex), elementParsers);
	}
	
	public DAFRegexRecordParser(int eventId, Pattern prefixPattern, Pattern postfixPattern, List<DAFRegexElementParser> elementParsers) {
		this.prefixPattern = prefixPattern;
		this.postfixPattern = postfixPattern;
		this.elementParsers = elementParsers;
	}
	
	public DAFRecord parseRecord(Collection<DAFEvent> events, PeekableBufferedReader reader) throws IOException {

		if(events == null || reader == null) {
			return null;
		}
		
		Matcher matcher = prefixPattern.matcher(reader.peekLine());
		if(!matcher.find()) {
			return null;
		}
		
		Collection<String> values = new ArrayList<String>();
		for(DAFRegexElementParser elementParser : elementParsers) {
			List<String> elementValues = elementParser.parseElements(matcher);
			if(elementValues == null) {
				return null;
			}
			values.addAll(elementValues);
		}
		
		matcher.usePattern(postfixPattern);
		if(!matcher.find()) {
			return null;
		}
		
		DAFEvent event = null;
		for(DAFEvent e : events) {
			if(e.getNumberOfElements() == values.size()) {
				event = e;
				break;
			}
		}
		
		if(event == null) {
			return null;
		}
		
		reader.readLine();
		return new DAFRecord(event, values);
	}
	
	public int getTotalNumberOfElements() {
		int totalElements = 0;
		for(DAFRegexElementParser elementParser : elementParsers) {
			totalElements += elementParser.getNumberOfElements();
		}
		return totalElements;
	}

	public String getPrefixRegex() {
		return getPrefixPattern().pattern();
	}
	
	public void setPrefixRegex(String prefixRegex) {
		setPrefixPattern(Pattern.compile(prefixRegex));
	}
	
	public Pattern getPrefixPattern() {
		return prefixPattern;
	}
	
	public void setPrefixPattern(Pattern prefixPattern) {
		this.prefixPattern = prefixPattern;
	}

	public String getPostfixRegex() {
		return getPostfixPattern().pattern();
	}
	
	public void setPostfixRegex(String postfixRegex) {
		setPostfixPattern(Pattern.compile(postfixRegex));
	}
	
	public Pattern getPostfixPattern() {
		return postfixPattern;
	}

	public void setPostfixPattern(Pattern postfixPattern) {
		this.postfixPattern = postfixPattern;
	}

	public List<DAFRegexElementParser> getElementParsers() {
		return elementParsers;
	}

	public void setElementParsers(List<DAFRegexElementParser> elementParsers) {
		this.elementParsers = elementParsers;
	}
}
