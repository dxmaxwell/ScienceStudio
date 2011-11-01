/** Copyright (c) Canadian Light Source, Inc. All rights reserved.
 *   - see license.txt for details.
 *
 *  Description:
 *     ValidationError class.
 *     
 */
package ca.sciencestudio.util.rest;

import org.springframework.context.MessageSourceResolvable;

/**
 * @author maxweld
 * 
 *
 */
public class ValidationError implements MessageSourceResolvable {

	public static final String DEFAULT_MESSAGE = "An unknown error has occurred.";
	
	private String[] codes;
	private Object[] arguments;
	private String defaultMessage;
	
	public ValidationError() {
		this(DEFAULT_MESSAGE);
	}
	
	public ValidationError(String defaultMessage) { 
		this.codes = new String[0];
		this.arguments = new Object[0];
		this.defaultMessage = defaultMessage;
	}
	
	public ValidationError(MessageSourceResolvable messageSourceResolvable) {
		 codes = messageSourceResolvable.getCodes();
		 arguments = messageSourceResolvable.getArguments();
		 defaultMessage = messageSourceResolvable.getDefaultMessage();
	}

	public String[] getCodes() {
		return codes;
	}
	public void setCodes(String[] codes) {
		this.codes = codes;
	}

	public Object[] getArguments() {
		return arguments;
	}
	public void setArguments(Object[] arguments) {
		this.arguments = arguments;
	}

	public String getDefaultMessage() {
		return defaultMessage;
	}
	public void setDefaultMessage(String defaultMessage) {
		this.defaultMessage = defaultMessage;
	}
}
