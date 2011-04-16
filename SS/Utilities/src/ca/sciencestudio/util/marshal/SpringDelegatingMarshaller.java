/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *     SpringDelegatingMarshaller class.
 *     
 */
package ca.sciencestudio.util.marshal;

import java.io.IOException;
import java.io.StringWriter;

import javax.xml.transform.stream.StreamResult;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.springframework.oxm.Marshaller;

/**
 * @author maxweld
 *
 */
public abstract class SpringDelegatingMarshaller implements ca.sciencestudio.util.marshal.Marshaller {

	private Type type;
	private Marshaller marshaller;
	
	protected Log logger = LogFactory.getLog(getClass());

	public SpringDelegatingMarshaller(Type type) {
		this.type  = type;
	}
	
	@Override
	public Type getType() {
		return type;
	}
	
	@Override
	public String marshal(Object object) {
		try {
			StringWriter streamWriter = new StringWriter(); 
			marshaller.marshal(object, new StreamResult(streamWriter));
			return streamWriter.toString();
		}
		catch (IOException e) {
			logger.warn("IOException while marshalling data to string.", e);
			return null;
		}
	}

	public Marshaller getMarshaller() {
		return marshaller;
	}
	public void setMarshaller(Marshaller marshaller) {
		this.marshaller = marshaller;
	}
}
