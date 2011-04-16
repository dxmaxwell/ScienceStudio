/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *     ContainerAwareJsonMarshaller class.
 *     
 */
package ca.sciencestudio.util.marshal;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

import org.apache.commons.lang.StringEscapeUtils;

import java.lang.reflect.Array;

/**
 * @author maxweld
 *
 */
public class ContainerAwareJsonMarshaller extends SpringDelegatingMarshaller implements JsonMarshaller {

	private static final String JSON_OBJECT_LEFT= "{";
	private static final String JSON_OBJECT_RIGHT = "}";
	
	private static final String JSON_ARRAY_LEFT = "[";
	private static final String JSON_ARRAY_RIGHT = "]";
	
	private static final String JSON_QUOTE_LEFT = "\"";
	private static final String JSON_QUOTE_RIGHT = "\"";
	
	private static final String JSON_RECORD_SEPARATOR = ",";
	
	private static final String JSON_NEWLINE = "\n";
	
	private static final String JSON_KEY_VALUE_SEPARATOR = ":";
	
	private static final String TAB_STRING = "\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t";
	
	public ContainerAwareJsonMarshaller() {
		super(Type.JSON);
	}
	
	@Override
	public String marshal(Object obj) {
		if(obj == null) {
			return null;
		}
		
		StringBuffer buffer = new StringBuffer();
		
		if(obj instanceof Map) {
			marshalMap(buffer, (Map<?,?>)obj, 0);
		}
		else if(obj instanceof Collection) {
			marshalCollection(buffer, (Collection<?>)obj, 0);
		}
		else if(obj.getClass().isArray()) {
			marshalCollection(buffer, arrayToCollection(obj), 0);
		}
		else {
			buffer.append(super.marshal(obj));
		}
		
		return buffer.toString();
	}
	
	protected void marshal(StringBuffer buffer, Object obj, int tabLevel) {
		
		if(obj == null) {
			buffer.append("null");
		}
		else if(obj instanceof Map) {
			marshalMap(buffer, (Map<?,?>)obj, tabLevel);
		}
		else if(obj instanceof Collection) {
			marshalCollection(buffer, (Collection<?>)obj, tabLevel);
		}
		else if(obj.getClass().isArray()) {
			marshalCollection(buffer, arrayToCollection(obj), tabLevel);
		}
		else if(obj instanceof String) {
			appendQuotedEscapedString(buffer, obj.toString());
		}
		else if(obj instanceof Number) {
			buffer.append(obj.toString());
		}
		else if(obj instanceof Boolean) {
			buffer.append(obj.toString());
		}
		else {
			buffer.append(super.marshal(obj));
		}
	}
	
	protected void marshalMap(StringBuffer buffer, Map<?,?> map, int tabLevel) {
		
		appendOpenObject(buffer, tabLevel);
		
		tabLevel++;
		boolean first = true;
		for(Map.Entry<?,?> entry : map.entrySet()) {
			if(first) {
				first = false;
			} else {
				buffer.append(JSON_RECORD_SEPARATOR);
				buffer.append(JSON_NEWLINE);
			}
			
			appendTabs(buffer, tabLevel);
			appendQuotedEscapedString(buffer, entry.getKey().toString());
			buffer.append(JSON_KEY_VALUE_SEPARATOR);
			
			marshal(buffer, entry.getValue(), tabLevel);
		}
		tabLevel--;
		
		appendCloseObject(buffer, tabLevel);
	}
	
	protected void marshalCollection(StringBuffer buffer, Collection<?> col, int tabLevel) {
		
		appendOpenArray(buffer, tabLevel);
		
		tabLevel++;
		boolean first = true;
		for(Object obj : col) {
			if(first) {
				first = false;
			} else {
				buffer.append(JSON_RECORD_SEPARATOR);
				buffer.append(JSON_NEWLINE);
			}
			
			marshal(buffer, obj, tabLevel);
		}
		tabLevel--;
		
		appendCloseArray(buffer, tabLevel);
	}
	
	protected Collection<Object> arrayToCollection(Object obj) {
		
		int length = Array.getLength(obj);
		Collection<Object> collection = new ArrayList<Object>(length);
		
		for(int idx = 0; idx < length; idx++) {
			Object element = Array.get(obj, idx);
			if(element != null) {
				collection.add(element);
			}
		}
		return collection;
	}
	
	protected void appendOpenObject(StringBuffer buffer, int tabLevel) {
		appendTabs(buffer, tabLevel);
		buffer.append(JSON_OBJECT_LEFT);
		buffer.append(JSON_NEWLINE);
	}
	
	protected void appendCloseObject(StringBuffer buffer, int tabLevel) {
		buffer.append(JSON_NEWLINE);
		appendTabs(buffer, tabLevel);
		buffer.append(JSON_OBJECT_RIGHT);
	}
	
	protected void appendOpenArray(StringBuffer buffer, int tabLevel) {
		appendTabs(buffer, tabLevel);
		buffer.append(JSON_ARRAY_LEFT);
		buffer.append(JSON_NEWLINE);
	}
	
	protected void appendCloseArray(StringBuffer buffer, int tabLevel) {
		buffer.append(JSON_NEWLINE);
		appendTabs(buffer, tabLevel);
		buffer.append(JSON_ARRAY_RIGHT);
	}
		
	protected  void appendQuotedEscapedString(StringBuffer buffer, String value) {
		buffer.append(JSON_QUOTE_LEFT);
		buffer.append(StringEscapeUtils.escapeJavaScript(value));
		buffer.append(JSON_QUOTE_RIGHT);
	}
	
	protected void appendTabs(StringBuffer buffer, int tabLevel) {
		tabLevel = Math.min(tabLevel, TAB_STRING.length()-1);
		buffer.append(TAB_STRING.substring(0, tabLevel));
	}
}
