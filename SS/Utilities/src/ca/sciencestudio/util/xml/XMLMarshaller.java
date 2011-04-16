/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *     XMLMarshaller class.
 *     
 */
package ca.sciencestudio.util.xml;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.converters.basic.DateConverter;

/**
 * @author medrand
 *
 */
public class XMLMarshaller {
	
	public static final String DEFAULT_DATE_FORMAT = "MM/dd/yyyy";
	public static final String TIMESTAMP_DATE_FORMAT = "MM/dd/yyyy HH:mm:ss";
	public static final String[] ALTERNATE_DATE_FORMATS = { "MM-dd-yyyy" };
	
	private XStream xstream;
	
	protected XMLMarshaller() {
		xstream = new XStream();
		xstream.registerConverter(new DateConverter(DEFAULT_DATE_FORMAT, ALTERNATE_DATE_FORMATS, false));
	}
	
	public static synchronized XMLMarshaller getInstance() {
		return new XMLMarshaller();
	}
	
	public String marshal(Object obj) {
		return xstream.toXML(obj);
	}
	
	public Object unmarshal(String xml) {
		return xstream.fromXML(xml);
	}
	
	public void setAlias(String name, Class<?> type) {
		xstream.alias(name, type);
	}
	
	public void setUseAttributeFor(Class<?> definedIn, String fieldName) {
		xstream.useAttributeFor(definedIn, fieldName);
	}
	
	public void registerDateConverter(String dateFormat) {
		xstream.registerConverter(new DateConverter(dateFormat,ALTERNATE_DATE_FORMATS, false));
	}
}
