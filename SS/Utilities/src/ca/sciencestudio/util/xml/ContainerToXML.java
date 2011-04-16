/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *     XMLConverter class.
 *     
 */
package ca.sciencestudio.util.xml;

import java.util.Set;
import java.util.Map;
import java.util.Date;
import java.util.List;
import java.util.HashMap;
import java.util.Collection;
import java.util.Collections;

import java.lang.reflect.Array;
import java.text.SimpleDateFormat;

import org.apache.commons.lang.StringEscapeUtils;

import ca.sciencestudio.util.xml.XMLMarshaller;

/**
 * @author maxweld
 *
 */
public abstract class ContainerToXML {	
	
	private static final String XML_TAG_OPEN_LEFT = "<";
	private static final String XML_TAG_OPEN_RIGHT = ">";
	
	private static final String XML_TAG_CLOSE_LEFT = "</";
	private static final String XML_TAG_CLOSE_RIGHT = ">";
	
	private static final String XML_TAG_ATTRIBUTE_LEFT = "=\"";
	private static final String XML_TAG_ATTRIBUTE_RIGHT = "\"";
	
	private static final String CDATA_BLOCK_LEFT = "<![CDATA[";
	private static final String CDATA_BLOCK_RIGHT = "]]>";

	//private static final String XML_TAG_NAME_MAP_ENTRY
	private static final String XML_TAG_NAME_ARRAY_VALUE = "data";
	private static final String XML_TAG_NAME_LIST_ELEMENT = "data";
	private static final String XML_TAG_NAME_COLLECTION_ELEMENT = "data";
	
	private static final String XML_TAG_ATTRIBUTE_MAP_SIZE = "size";
	private static final String XML_TAG_ATTRIBUTE_LIST_SIZE = "size";
	private static final String XML_TAG_ATTRIBUTE_LIST_INDEX = "index";
	private static final String XML_TAG_ATTRIBUTE_ARRAY_INDEX = "index";
	private static final String XML_TAG_ATTRIBUTE_ARRAY_LENGTH = "size";
	private static final String XML_TAG_ATTRIBUTE_COLLECTION_SIZE = "size";
	
	private static final String EMPTY_STRING = "";
	private static final String SPACE_STRING = " ";
	private static final String NEWLINE_STRING = "\n";
	private static final String TAB_STRING = "\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t";
	
	// Object //
	public static String objectToXML(Object object) {
		return objectToXML(object, null, null, 0);
	}

	public static String objectToXML(Object object, String tagName) {
		return objectToXML(object, tagName, null, 0);
	}
	
	public static String objectToXML(Object object, String tagName, Map<String,String> tagAttributes) {
		return objectToXML(object, tagName, tagAttributes, 0);
	}
	
	public static String objectToXML(Object object, String tagName, Map<String,String> tagAttributes, int tabLevel) {
		
		if(object instanceof Date) {
			return dateToXML((Date)object, tagName, tagAttributes, tabLevel);
		}
		
		if(object instanceof String) {
			return stringToXML((String)object, tagName, tagAttributes, tabLevel);
		}
		
		if(object instanceof Number) {
			return numberToXML((Number)object, tagName, tagAttributes, tabLevel);
		}
		
		if(object instanceof Boolean) {
			return booleanToXML((Boolean)object, tagName, tagAttributes, tabLevel);
		}
		
		if(object instanceof Map) {
			return mapToXML((Map<?,?>)object, tagName, tagAttributes, tabLevel);
		}
		
		if(object instanceof List) {
			return listToXML((List<?>)object, tagName, tagAttributes, tabLevel);
		}
		
		if(object instanceof Collection) {
			return collectionToXML((Collection<?>)object, tagName, tagAttributes, tabLevel);	
		}
		
		if((object != null) && object.getClass().isArray()) {
			return arrayToXML(object, tagName, tagAttributes, tabLevel);
		}
		
		if(tagName == null) {
			if(object == null) {
				tagName = Object.class.getSimpleName().toLowerCase();
			} else {
				tagName = object.getClass().getSimpleName().toLowerCase();
			}
		}
		
		if(object == null) {
			object = "null";
		}
		
		StringBuilder builder = new StringBuilder();
		builder.append(getOpenTag(tagName, tagAttributes, tabLevel));
		builder.append(CDATA_BLOCK_LEFT);
		builder.append(object);
		builder.append(CDATA_BLOCK_RIGHT);
		builder.append(getCloseTagLn(tagName, 0));
		return builder.toString();
	}
	
	// Date //
	public static String dateToXML(Date date) {
		return dateToXML(date, null, null, 0);
	}
	
	public static String dateToXML(Date date, String tagName) {
		return dateToXML(date, tagName, null, 0);
	}
	
	public static String dateToXML(Date date, String tagName, Map<String,String> tagAttributes) {
		return dateToXML(date, tagName, tagAttributes, 0);
	}
	
	public static String dateToXML(Date date, String tagName, Map<String,String> tagAttributes, int tabLevel) {
		
		if(tagName == null) {
			tagName = Date.class.getSimpleName().toLowerCase();
		}
		
		if(date == null) {
			date = new Date(0L);
		}
		
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(XMLMarshaller.DEFAULT_DATE_FORMAT);
		String dateString = simpleDateFormat.format(date);
		
		StringBuilder builder = new StringBuilder();
		builder.append(getOpenTag(tagName, tagAttributes, tabLevel));
		builder.append(StringEscapeUtils.escapeXml(dateString));
		builder.append(getCloseTagLn(tagName, 0));
		return builder.toString();
	}
	
	// String //
	public static String stringToXML(String string) {
		return stringToXML(string, null, null, 0);
	}
	
	public static String stringToXML(String string, String tagName) {
		return stringToXML(string, tagName, null, 0);
	}
	
	public static String stringToXML(String string, String tagName, Map<String,String> tagAttributes) {
		return stringToXML(string, tagName, tagAttributes, 0);
	}
	
	public static String stringToXML(String string, String tagName, Map<String,String> tagAttributes, int tabLevel) {
		
		if(tagName == null) {
			tagName = String.class.getSimpleName().toLowerCase();
		}
		
		if(string == null) {
			string = EMPTY_STRING;
		}
		
		StringBuilder builder = new StringBuilder();
		builder.append(getOpenTag(tagName, tagAttributes, tabLevel));
		builder.append(StringEscapeUtils.escapeXml(string));
		builder.append(getCloseTagLn(tagName, 0));
		return builder.toString();
	}
	
	// Number //
	public static String numberToXML(Number number) {
		return numberToXML(number, null, null, 0);
	}
	
	public static String numberToXML(Number number, String tagName) {
		return numberToXML(number, tagName, null, 0);
	}
	
	public static String numberToXML(Number number, String tagName, Map<String,String> tagAttributes) {
		return numberToXML(number, tagName, tagAttributes, 0);	
	}
	
	public static String numberToXML(Number number, String tagName, Map<String,String> tagAttributes, int tabLevel) {
		
		if(tagName == null) {
			if(number == null) {
				tagName = Number.class.getSimpleName().toLowerCase();
			} else {
				tagName = number.getClass().getSimpleName().toLowerCase();
			}
		}
		
		if(number == null) {
			number = 0;
		}
		
		StringBuilder builder = new StringBuilder();
		builder.append(getOpenTag(tagName, tagAttributes, tabLevel));
		builder.append(number);
		builder.append(getCloseTagLn(tagName, 0));
		return builder.toString();
	}
	
	// Boolean //
	public static String booleanToXML(Boolean booleen) {
		return booleanToXML(booleen, null, null, 0);	
	}
	
	public static String booleanToXML(Boolean booleen, String tagName) {
		return booleanToXML(booleen, tagName, null, 0);
	}
	
	public static String booleanToXML(Boolean booleen, String tagName, Map<String,String> tagAttributes) {
		return booleanToXML(booleen, tagName, tagAttributes, 0);
	}
	
	public static String booleanToXML(Boolean booleen, String tagName, Map<String,String> tagAttributes, int tabLevel) {
		
		if(tagName == null) {
			tagName = Boolean.class.getSimpleName().toLowerCase();
		}
		
		if(booleen == null) {
			booleen = false;
		}
		
		StringBuilder builder = new StringBuilder();
		builder.append(getOpenTag(tagName, tagAttributes, tabLevel));
		builder.append(booleen);
		builder.append(getCloseTagLn(tagName, 0));
		return builder.toString();
	}
	
	// Map<Object,Object> //
	public static String mapToXML(Map<?,?> map) {
		return mapToXML(map, null, null, 0);
	}
	
	public static String mapToXML(Map<?,?> map, String tagName) {
		return mapToXML(map, tagName, null, 0);
	}
	
	public static String mapToXML(Map<?,?> map, String tagName, Map<String,String> tagAttributes) {
		return mapToXML(map, tagName, tagAttributes, 0);
	}
	
	public static String mapToXML(Map<?,?> map, String tagName, Map<String,String> tagAttributes, int tabLevel) {
		
		if(tagName == null) { 
			tagName = Map.class.getSimpleName().toLowerCase();
		}
		
		if(map == null) {
			map = Collections.emptyMap();
		}
		
		if(tagAttributes == null) {
			tagAttributes = new HashMap<String,String>();
		} else {
			tagAttributes = new HashMap<String,String>(tagAttributes);
		}
		tagAttributes.put(XML_TAG_ATTRIBUTE_MAP_SIZE, String.valueOf(map.size()));
		
		StringBuilder builder = new StringBuilder();
		builder.append(getOpenTagLn(tagName, tagAttributes, tabLevel));	
		for(Map.Entry<?,?> entry : map.entrySet()) {
			builder.append(objectToXML(entry.getValue(), entry.getKey().toString(), null, tabLevel+1)); 
		}
		builder.append(getCloseTagLn(tagName, tabLevel));
		return builder.toString();
	}
	
	// List<Object> //
	public static String listToXML(List<?> list) {
		return listToXML(list, null, null, 0);
	}
	
	public static String listToXML(List<?> list, String tagName) {
		return listToXML(list, tagName, null, 0);
	}
	
	public static String listToXML(List<?> list, String tagName, Map<String,String> tagAttributes) {
		return listToXML(list, tagName, tagAttributes, 0);
	}
	
	public static String listToXML(List<?> list, String tagName, Map<String,String> tagAttributes, int tabLevel) {
		
		if(tagName == null) {
			tagName = List.class.getSimpleName().toLowerCase();
		}
		
		if(list == null) {
			list = Collections.emptyList();
		}
		
		if(tagAttributes == null) {
			tagAttributes = new HashMap<String,String>();
		} else {
			tagAttributes = new HashMap<String,String>(tagAttributes);
		}
		tagAttributes.put(XML_TAG_ATTRIBUTE_LIST_SIZE, String.valueOf(list.size()));
		
		int index = 0;
		String childTagName = XML_TAG_NAME_LIST_ELEMENT;
		Map<String,String> childTagAttributes = new HashMap<String,String>();
		
		StringBuilder buffer = new StringBuilder();
		buffer.append(getOpenTagLn(tagName, tagAttributes, tabLevel));
		for(Object element : list) {
			childTagAttributes.put(XML_TAG_ATTRIBUTE_LIST_INDEX, String.valueOf(index++));
			buffer.append(objectToXML(element, childTagName, childTagAttributes, tabLevel+1));	
		}
		buffer.append(getCloseTagLn(tagName, tabLevel));
		return buffer.toString();
	}

	// Collection<Object> //
	public static String collectionToXML(Collection<?> collection, String tagName, Map<String,String> tagAttributes, int tabLevel) {
		
		if(tagName == null) {
			if(collection instanceof Set) {
				tagName = Set.class.getSimpleName().toLowerCase();
			} else {
				tagName = Collection.class.getSimpleName().toLowerCase();
			}
		}
			
		if(collection == null) {
			collection = Collections.emptySet();
		}
		
		if(tagAttributes == null) {
			tagAttributes = new HashMap<String,String>();
		} else {
			tagAttributes = new HashMap<String,String>(tagAttributes);
		}
		tagAttributes.put(XML_TAG_ATTRIBUTE_COLLECTION_SIZE, String.valueOf(collection.size()));
		
		String childTagName = XML_TAG_NAME_COLLECTION_ELEMENT;
		
		StringBuilder builder = new StringBuilder();
		builder.append(getOpenTagLn(tagName, tagAttributes, tabLevel));
		for(Object element : collection) {
			builder.append(objectToXML(element, childTagName, null, tabLevel+1));	
		}
		builder.append(getCloseTagLn(tagName, tabLevel));
		return builder.toString();
	}
	
	// Object[] //
	public static final String arrayToXML(Object array) {
		return arrayToXML(array, null, null, 0);
	}
	
	public static final String arrayToXML(Object array, String tagName) {
		return arrayToXML(array, tagName, null, 0);
	}
	
	public static final String arrayToXML(Object array, String tagName, Map<String,String> tagAttributes) {
		return arrayToXML(array, tagName, tagAttributes, 0);
	}
	
	public static final String arrayToXML(Object array, String tagName, Map<String,String> tagAttributes, int tabLevel) {
		
		if(tagName == null) {
			tagName = Array.class.getSimpleName().toLowerCase();
		}
		
		if(array == null) {
			array = new Object[0];
		}
		
		if(!array.getClass().isArray()) {
			Object[] temp = new Object[1];
			temp[0] = array;
			array = temp;
		}
		
		if(tagAttributes == null) {
			tagAttributes = new HashMap<String,String>();
		} else {
			tagAttributes = new HashMap<String,String>(tagAttributes);
		}
		int length = Array.getLength(array);
		tagAttributes.put(XML_TAG_ATTRIBUTE_ARRAY_LENGTH, String.valueOf(length));
		
		String childTagName = XML_TAG_NAME_ARRAY_VALUE;
		Map<String,String> childTagAttributes = new HashMap<String,String>();
		
		StringBuilder buffer = new StringBuilder();
		buffer.append(getOpenTagLn(tagName, tagAttributes, tabLevel));
		for(int idx=0; idx<length; idx++) {
			childTagAttributes.put(XML_TAG_ATTRIBUTE_ARRAY_INDEX, String.valueOf(idx));
			buffer.append(objectToXML(Array.get(array, idx), childTagName, childTagAttributes, tabLevel+1));	
		}
		buffer.append(getCloseTagLn(tagName, tabLevel));
		return buffer.toString();
	}
	
	// helpers //	
	protected static String getOpenTagLn(String tagName, Map<String,String> tagAttributes, int tabLevel) {
		return (getOpenTag(tagName, tagAttributes, tabLevel) + NEWLINE_STRING);
	}
	
	protected static String getOpenTag(String tagName, Map<String,String> tagAttributes, int tabLevel) {
		
		StringBuilder builder = new StringBuilder(getTabs(tabLevel));
		builder.append(XML_TAG_OPEN_LEFT);
		builder.append(tagName);
		
		if(tagAttributes != null) {
			for(Map.Entry<String,String> entry : tagAttributes.entrySet()) {
				builder.append(SPACE_STRING);
				builder.append(entry.getKey());
				builder.append(XML_TAG_ATTRIBUTE_LEFT);
				builder.append(StringEscapeUtils.escapeXml(entry.getValue()));
				builder.append(XML_TAG_ATTRIBUTE_RIGHT);
			}
		}
		
		builder.append(XML_TAG_OPEN_RIGHT);
		return builder.toString();
	}
	
	protected static String getCloseTagLn(String tagName, int tabLevel) {
		return (getCloseTag(tagName, tabLevel) + NEWLINE_STRING);
	}
	
	protected static String getCloseTag(String tagName, int tabLevel) {
		StringBuilder builder = new StringBuilder(getTabs(tabLevel));
		builder.append(XML_TAG_CLOSE_LEFT);
		builder.append(tagName);
		builder.append(XML_TAG_CLOSE_RIGHT);
		return builder.toString();
	}
	
	protected static String getTabs(int tabLevel) {
		tabLevel = Math.min(tabLevel, TAB_STRING.length()-1);
		return TAB_STRING.substring(0, tabLevel);
	}
}
