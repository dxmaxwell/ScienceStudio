/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *     HttpMessageConverterWriteTag class.
 *     
 */
package ca.sciencestudio.util.tags;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;
import javax.servlet.jsp.tagext.TryCatchFinally;

import org.apache.commons.io.IOUtils;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;

import ca.sciencestudio.util.http.ByteArrayHttpOutputMessage;
import ca.sciencestudio.util.http.HttpMessageConvertersHolder;

/**
 * @author maxweld
 *
 */
public class HttpMessageConverterWriteTag extends TagSupport implements TryCatchFinally {

	private static final long serialVersionUID = 1L;

	private static String defaultType = "application/json";
	
	public static String getDefaultType() {
		return defaultType;
	}
	public static void setDefaultType(String defaultType) {
		HttpMessageConverterWriteTag.defaultType = defaultType;
	}
	
	private Object source = null;
	
	private String type = defaultType;
	
	@Override
	public int doStartTag() throws JspException {
		
		MediaType mediaType = MediaType.parseMediaType(type);
		
		for(HttpMessageConverter<Object> messageConverter : HttpMessageConvertersHolder.getMessageConverters()) {
			if(messageConverter.canWrite(source.getClass(), mediaType)) {
				ByteArrayHttpOutputMessage outputMessage = new ByteArrayHttpOutputMessage();
				try {
					messageConverter.write(source, mediaType, outputMessage);
				}
				catch(IOException e) {
					throw new JspException("Exception while writing source object.", e);
				}
				
				try {
					byte[] message = outputMessage.toByteArray();
					InputStream messageInputStream = new ByteArrayInputStream(message);
					IOUtils.copy(messageInputStream, pageContext.getOut());
				}
				catch(IOException e) {
					throw new JspException("Exception while writing message to JSP page.", e);
				}
				
				return SKIP_BODY;
			}
		}
		
		throw new JspException("HTTP Message Converter not found for class: " + source.getClass().getName() + ": and type: " + type);
	}
	
	@Override
	public void doCatch(Throwable e) throws Throwable {
		throw e;
	}
	
	@Override
	public void doFinally() {
		// nothing to do //
	}
	
	public Object getSource() {
		return source;
	}
	public void setSource(Object source) {
		this.source = source;
	}
	
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
}
